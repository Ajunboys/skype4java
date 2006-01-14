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

public class VoiceMailAPITest extends TestCase {
    public void testLeaveVoiceMail() throws Exception {
        TestData.getFriend().leaveVoiceMail();
        TestUtils.showCheckDialog(TestData.getFriend().getId() + "�Ƀ{�C�X���[�������M����܂������H");
    }
}
