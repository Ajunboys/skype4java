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

import java.util.Properties;
import junit.framework.Assert;

final class TestCaseProperties {
    private final Class testCaseClass;
    private Properties properties;

    TestCaseProperties(Class testCaseClass) {
        this.testCaseClass = testCaseClass;
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(getPropertyFileName()));
        } catch (Exception e) {
            Assert.fail(getPropertyFileName() + ".base�t�@�C��������" + getPropertyFileName() + "�t�@�C���𓯂��f�B���N�g�����ɍ쐬���Ă�������");
        }
    }

    private String getPropertyFileName() {
        return testCaseClass.getSimpleName() + ".properties";
    }

    String getProperty(String key) {
        if (!properties.containsKey(key)) {
            throw new IllegalArgumentException(getPropertyFileName() + "�t�@�C����" + key + "�G���g�����܂܂�Ă��܂���");
        }
        return properties.getProperty(key);
    }
}
