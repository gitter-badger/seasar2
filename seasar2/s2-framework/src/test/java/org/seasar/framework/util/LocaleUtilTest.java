/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.util;

import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author higa
 *
 */
public class LocaleUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetLocale() throws Exception {
        assertEquals("1", Locale.getDefault(), LocaleUtil.getLocale(null));
        assertEquals("2", Locale.JAPANESE, LocaleUtil.getLocale("ja"));
        assertEquals("3", Locale.JAPAN, LocaleUtil.getLocale("ja_JP"));
    }
}