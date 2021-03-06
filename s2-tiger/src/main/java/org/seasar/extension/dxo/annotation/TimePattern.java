/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;

/**
 * <code>java.sql.Time</code>と<code>String</code>の変換に使用するフォーマットを指定します。
 * 
 * @author koichik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface TimePattern {

    /**
     * <code>java.sql.Time</code>と<code>String</code>の変換に使用するフォーマットです。
     * <p>
     * フォーマットは{@link SimpleDateFormat}に従います．
     * </p>
     * 
     * @return <code>java.sql.Time</code>と<code>String</code>の変換に使用するフォーマット
     */
    String value();

}
