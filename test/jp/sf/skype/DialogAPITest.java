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

import java.io.File;
import junit.framework.TestCase;

public class DialogAPITest extends TestCase {
    public void testShowMainWindow() throws Exception {
        Skype.showSkypeWindow();
        TestUtils.showCheckDialog("Skype�̃��C���E�B���h�E���őO�ʂɕ\������Ă��܂����H");
        Skype.hideSkypeWindow();
        TestUtils.showCheckDialog("Skype�̃��C���E�B���h�E���ŏ�������Ă��܂����H");
    }

    public void testShowAddFriendWindow() throws Exception {
        Skype.showAddFriendWindow();
        TestUtils.showCheckDialog("�R���^�N�g�֒ǉ��E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�ɃR���^�N�g�֒ǉ��E�B���h�E����Ă�������");
        Skype.showAddFriendWindow(TestData.getFriendId());
        TestUtils.showCheckDialog(TestData.getFriendId() + "�����炩���ߐݒ肳�ꂽ��Ԃ�[�R���^�N�g�֒ǉ�]�E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO��[�R���^�N�g�֒ǉ�]�E�B���h�E����Ă�������");
    }

    public void testShowChatWindow() throws Exception {
        Skype.showChatWindow(TestData.getFriendId());
        TestUtils.showCheckDialog(TestData.getFriendId() + "�Ƃ̃`���b�g�E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�Ƀ`���b�g�E�B���h�E����Ă�������");
        Skype.showChatWindow(TestData.getFriendId(), "Hello, World!");
        TestUtils.showCheckDialog("Hello, World!�����炩���ߓ��͂��ꂽ��Ԃ�" + TestData.getFriendId() + "�Ƃ̃`���b�g�E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�Ƀ`���b�g�E�B���h�E����Ă�������");
    }

    public void testShowFileTransferWindow() throws Exception {
        Skype.showFileTransferWindow(TestData.getFriendId());
        TestUtils.showCheckDialog(TestData.getFriendId() + "�֑��M����t�@�C���̑I���E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�ɑI���E�B���h�E����Ă�������");
        Skype.showFileTransferWindow(TestData.getFriendId(), new File("C:\\"));
        TestUtils.showCheckDialog("C:\\�����炩���ߑI�����ꂽ��Ԃ�" + TestData.getFriendId() + "�֑��M����t�@�C���̑I���E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�ɑI���E�B���h�E����Ă�������");
    }

    public void testShowProfileWindow() throws Exception {
        Skype.showProfileWindow();
        TestUtils.showCheckDialog("�v���t�B�[���E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�Ƀv���t�B�[���E�B���h�E����Ă�������");
    }

    public void testShowUserInformationWindow() throws Exception {
        Skype.showUserInformationWindow(TestData.getFriendId());
        TestUtils.showCheckDialog(TestData.getFriendId() + "�̃v���t�B�[���E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�Ƀv���t�B�[���E�B���h�E����Ă�������");
    }

    public void testShowConferenceWindow() throws Exception {
        Skype.showConferenceWindow();
        TestUtils.showCheckDialog("���[�U����c�ʘb�ɏ��҃E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�Ƀ��[�U����c�ʘb�ɏ��҃E�B���h�E����Ă�������");
    }

    public void testShowSearchWindow() throws Exception {
        Skype.showSearchWindow();
        TestUtils.showCheckDialog("Skype���[�U�̌����E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO��Skype���[�U�̌����E�B���h�E����Ă�������");
    }

    public void testShowOptionsWindow() throws Exception {
        Skype.showOptionsWindow(Skype.OptionsPage.ADVANCED);
        TestUtils.showCheckDialog("�g���y�[�W�����炩���ߑI�����ꂽ��ԂŐݒ�E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�Ƀ��[�U��ݒ�E�B���h�E����Ă�������");
    }
}
