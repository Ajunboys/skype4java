/*******************************************************************************
 * Copyright (c) 2006 Koji Hisano <hisano@gmail.com> - UBION Inc. Developer
 * Copyright (c) 2006 UBION Inc. <http://www.ubion.co.jp/>
 * 
 * Copyright (c) 2006 Skype Technologies S.A. <http://www.skype.com/>
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Common Public License v1.0 which accompanies
 * this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: Koji Hisano - initial API and implementation
 ******************************************************************************/
package com.skype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.skype.connector.Connector;
import com.skype.connector.ConnectorException;

public final class Call {
    /*
     * ���M�̏�� ROUTING (�o�H�T��) > RINGING (�Ăяo����) > INPROGRESS (��b�J�n) > FINISHED
     * (��b�I��) > MISSED (���M�����L�����Z��) > REFUSED (��M�����L�����Z��)
     */
    public enum Status {
        UNPLACED, ROUTING, EARLYMEDIA, FAILED, RINGING, INPROGRESS, ONHOLD, FINISHED, MISSED, REFUSED, BUSY, CANCELLED, VM_BUFFERING_GREETING, VM_PLAYING_GREETING, VM_RECORDING, VM_UPLOADING, VM_SENT, VM_CANCELLED, VM_FAILED
    }

    public enum Type {
        INCOMING_PSTN, OUTGOING_PSTN, INCOMING_P2P, OUTGOING_P2P;
    }

    public enum VideoStatus {
        NOT_AVAILABLE, AVAILABLE, STARTING, REJECTED, RUNNING, STOPPING, PAUSED;
    }

    private enum VideoEnabled {
        VIDEO_NONE, VIDEO_SEND_ENABLED, VIDEO_RECV_ENABLED, VIDEO_BOTH_ENABLED;
    }

    private final String id;
    private final List<CallStatusChangedListener> listeners = Collections.synchronizedList(new ArrayList<CallStatusChangedListener>());
    
    private SkypeExceptionHandler exceptionHandler;

    Call(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object compared) {
        if (compared instanceof Call) {
            return id.equals(((Call) compared).id);
        }
        return false;
    }

    public String getId() {
        return id;
    }

//    public void addCallStatusChangedListener(CallStatusChangedListener listener) {
//        Utils.checkNotNull("listener", listener);
//        listeners.add(listener);
//    }
//
//    public void removeCallStatusChangedListener(CallStatusChangedListener listener) {
//        Utils.checkNotNull("listener", listener);
//        listeners.remove(listener);
//    }

    void fireStatusChanged(Status status) {
        CallStatusChangedListener[] listeners = this.listeners.toArray(new CallStatusChangedListener[0]); // �C�x���g�ʒm���Ƀ��X�g���ύX�����\�������邽��
        for (CallStatusChangedListener listener : listeners) {
            try {
                listener.statusChanged(this);
            } catch (SkypeException e) {
                Utils.handleUncaughtException(e, exceptionHandler);
            }
        }
    }

    public void hold() throws SkypeException {
        setStatus("ONHOLD");
    }

    public void resume() throws SkypeException {
        setStatus("INPROGRESS");
    }

    public void finish() throws SkypeException {
        setStatus("FINISHED");
    }

    public void answer() throws SkypeException {
        setStatus("INPROGRESS");
    }

    public void cancel() throws SkypeException {
        setStatus("FINISHED");
    }

    private void setStatus(String status) throws SkypeException {
        try {
            String response = Connector.getInstance().executeWithId("SET CALL " + getId() + " STATUS " + status, "CALL " + getId() + " STATUS ");
            Utils.checkError(response);
        } catch (ConnectorException e) {
            Utils.convertToSkypeException(e);
        }
    }

    public void forward() throws SkypeException {
        try {
            Connector.getInstance().setDebug(true);
            String response = Connector.getInstance().execute("ALTER CALL " + getId() + " END FORWARD_CALL");
            Utils.checkError(response);
        } catch (ConnectorException e) {
            Utils.convertToSkypeException(e);
        }
    }

    public Date getStartTime() throws SkypeException {
        return Utils.parseUnixTime(getProperty("TIMESTAMP"));
    }

    public User getPartner() throws SkypeException {
        return new User(getPartnerId());
    }

    public String getPartnerId() throws SkypeException {
        return getProperty("PARTNER_HANDLE");
    }

    public String getPartnerDisplayName() throws SkypeException {
        return getProperty("PARTNER_DISPNAME");
    }

    public Type getType() throws SkypeException {
        return Type.valueOf(getProperty("TYPE"));
    }

    public Status getStatus() throws SkypeException {
        return Status.valueOf(Utils.getPropertyWithCommandId("CALL", getId(), "STATUS")); // prevent
                                                                                            // event
                                                                                            // notification
    }

    public int getDuration() throws SkypeException {
        return Integer.parseInt(getProperty("DURATION"));
    }

    public int getErrorCode() throws SkypeException {
        return Integer.parseInt(getProperty("FAILUREREASON"));
    }

    public void setReceiveVideoEnabled(boolean on) throws SkypeException {
        String value = on ? "START_VIDEO_SEND" : "STOP_VIDEO_SEND";
        try {
            String response = Connector.getInstance().execute("ALTER CALL " + getId() + " " + value);
            Utils.checkError(response);
        } catch (ConnectorException e) {
            Utils.convertToSkypeException(e);
        }
    }

    public boolean isReceiveVideoEnabled() throws SkypeException {
        VideoEnabled enabled = VideoEnabled.valueOf(getProperty("VIDEO_STATUS"));
        switch (enabled) {
        case VIDEO_NONE:
        case VIDEO_SEND_ENABLED:
            return false;
        case VIDEO_RECV_ENABLED:
        case VIDEO_BOTH_ENABLED:
            return true;
        default:
            return false;
        }
    }

    public void setSendVideoEnabled(boolean on) throws SkypeException {
        String value = on ? "START_VIDEO_RECEIVE" : "STOP_VIDEO_RECEIVE";
        try {
            String response = Connector.getInstance().execute("ALTER CALL " + getId() + " " + value);
            Utils.checkError(response);
        } catch (ConnectorException e) {
            Utils.convertToSkypeException(e);
        }
    }

    public boolean isSendVideoEnabled() throws SkypeException {
        VideoEnabled enabled = VideoEnabled.valueOf(getProperty("VIDEO_STATUS"));
        switch (enabled) {
        case VIDEO_NONE:
        case VIDEO_RECV_ENABLED:
            return false;
        case VIDEO_SEND_ENABLED:
        case VIDEO_BOTH_ENABLED:
            return true;
        default:
            return false;
        }
    }

    public VideoStatus getReceiveVideoStatus() throws SkypeException {
        return VideoStatus.valueOf(getProperty("VIDEO_RECEIVE_STATUS"));
    }

    public VideoStatus getSendVideoStatus() throws SkypeException {
        return VideoStatus.valueOf(getProperty("VIDEO_SEND_STATUS"));
    }

    private String getProperty(String name) throws SkypeException {
        return Utils.getProperty("CALL", getId(), name);
    }
}