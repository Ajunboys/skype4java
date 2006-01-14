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

import junit.framework.TestCase;

public class VideoAPITest extends TestCase {
    public void testGetVideoDevice() throws Exception {
        String name = Skype.getVideoDevice();
        if (name == null) {
            name = "�W���r�f�I�f�o�C�X";
        }
        TestUtils.showCheckDialog("Web�J������[" + name + "]�ɐݒ肳��Ă��܂����H");
        Skype.setVideoDevice("");
    }

    public void testOpenVideoTestWindow() throws Exception {
        Skype.openVideoTestWindow();
        TestUtils.showCheckDialog("Web�J�����̃e�X�g�E�B���h�E���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO��Web�J�����̃e�X�g�E�B���h�E����Ă�������");
    }

    public void testOpenVideoOptionsWindow() throws Exception {
        Skype.openVideoOptionsWindow();
        TestUtils.showCheckDialog("�r�f�I�ݒ�y�[�W���\������Ă��܂����H");
        TestUtils.showMessageDialog("���֐i�ޑO�Ƀr�f�I�ݒ�y�[�W�̃E�B���h�E����Ă�������");
    }
}
