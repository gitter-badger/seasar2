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
package org.seasar.extension.jdbc.gen.internal.command;

import org.junit.After;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.internal.command.DumpDbMetaCommand;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumpDbMetaCommandTest {

    /**
     * 
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        SingletonS2ContainerFactory.destroy();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testInit() throws Exception {
        DumpDbMetaCommand command = new DumpDbMetaCommand();
        command.setConfigPath("s2jdbc-gen-core-test.dicon");
        command.validate();
        command.init();
        assertNotNull(command.dialect);
        assertNotNull(command.dbTableMetaReader);
    }
}
