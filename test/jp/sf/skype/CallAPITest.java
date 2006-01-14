/*******************************************************************************
 * Copyright (c) 2006 Koji Hisano <hisano@gmail.com>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     Koji Hisano - initial API and implementation
 *******************************************************************************/
package jp.sf.skype;

import java.util.Date;
import junit.framework.TestCase;

public class CallAPITest extends TestCase {
    public void testCallAndFinish() throws Exception {
        TestUtils.showMessageDialog(TestData.getFriendId() + "�ɔ��M���s����2�b��ɐ؂�邩�ǂ����m�F���Ă�������");
        Friend friend = Skype.getContactList().getFriend(TestData.getFriendId());
        Call call = friend.call();
        Thread.sleep(2000);
        call.finish();
        TestUtils.showCheckDialog(TestData.getFriendId() + "�ɔ��M���s����2�b��ɐ؂�܂������H");
    }

    public void testFinishEndedCall() throws Exception {
        Friend friend = Skype.getContactList().getFriend(TestData.getFriendId());
        Call call = friend.call();
        call.finish();
        Thread.sleep(1000);
        try {
            call.finish();
        } catch (CommandFailedException e) {
            assertEquals(24, e.getCode());
            assertEquals("Cannot hangup inactive call", e.getMessage());
        }
    }

    public void testHoldAndResume() throws Exception {
        TestUtils.showMessageDialog(TestData.getFriendId() + "�ɔ��M���s����10�b�ȓ��ɉ�b���J�n���Ă�������");
        Friend friend = Skype.getContactList().getFriend(TestData.getFriendId());
        Call call = friend.call();
        Thread.sleep(10000);
        TestUtils.showMessageDialog("5�b�Ԓʘb�����f���ꂽ���5�b�Ԓʘb���ĊJ����邩�m�F���Ă�������");
        call.hold();
        Thread.sleep(5000);
        call.resume();
        Thread.sleep(5000);
        call.finish();
        TestUtils.showCheckDialog("5�b�Ԓʘb�����f���ꂽ���5�b�Ԓʘb���ĊJ����܂������H");
    }

    public void testCallProperty() throws Exception {
        Date startTime = new Date();
        TestUtils.showMessageDialog(TestData.getFriendId() + "�ɔ��M���s����10�b�ȓ��ɉ�b���J�n���Ă�������");
        Friend friend = Skype.getContactList().getFriend(TestData.getFriendId());
        Call call = friend.call();
        Thread.sleep(10000);
        TestUtils.showMessageDialog("�����ؒf��Ɋe��v���p�e�B�̒l���e�X�g����܂�");
        call.finish();
        Date endTime = new Date();
        assertTrue(call.getStartTime().getTime() - startTime.getTime() <= endTime.getTime() - startTime.getTime());
        assertTrue(call.getDuration() <= endTime.getTime() - startTime.getTime());
        assertEquals(TestData.getFriendId(), call.getPartnerId());
        assertEquals(TestData.getFriendDisplayName(), call.getPartnerDisplayName());
    }

    public void testCallReceived() throws Exception {
        final Call[] result = new Call[1];
        Skype.addCallReceivedListener(new CallReceivedListener() {
            public void callReceived(Call call) {
                result[0] = call;
            }
        });
        TestUtils.showMessageDialog("�����ɑ΂���" + TestData.getFriendId() + "�ɔ��M���˗����Ē��M������_�C�A���O����Đؒf���Ă�������");
        assertEquals(TestData.getFriendId(), result[0].getPartnerId());
    }
}
