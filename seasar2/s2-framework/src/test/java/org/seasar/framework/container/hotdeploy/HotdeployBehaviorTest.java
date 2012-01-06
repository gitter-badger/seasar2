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
package org.seasar.framework.container.hotdeploy;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class HotdeployBehaviorTest extends S2FrameworkTestCase {

    private ClassLoader originalLoader;

    private HotdeployBehavior ondemand;

    private String rootPackageName = ClassUtil.getPackageName(getClass())
            + ".creator";

    protected void setUp() {
        originalLoader = Thread.currentThread().getContextClassLoader();
        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName(rootPackageName);
        ondemand = new HotdeployBehavior();
        ondemand.setNamingConvention(convention);
        ComponentCreatorImpl creator = new ComponentCreatorImpl(convention);
        creator.setNameSuffix("Dao");
        ondemand.setCreators(new ComponentCreator[] { creator });
        S2ContainerBehavior.setProvider(ondemand);
    }

    protected void tearDown() {
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalLoader);
    }

    /**
     * @throws Exception
     */
    public void testStartStop() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(getClass());
        ondemand.start();
        try {
            assertEquals(HotdeployClassLoader.class, Thread.currentThread()
                    .getContextClassLoader().getClass());
        } finally {
            ondemand.stop();
        }
        assertSame(originalLoader, Thread.currentThread()
                .getContextClassLoader());
        assertNotSame(beanDesc, BeanDescFactory.getBeanDesc(getClass()));
    }
}