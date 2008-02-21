/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.task.GenTask;
import org.seasar.framework.exception.IORuntimeException;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenTaskTest {

    private ClassLoader classLoader;

    @Before
    public void setUp() throws Exception {
        String classpath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");
        String[] paths = classpath.split("\\" + pathSeparator);
        URL[] urls = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
            urls[i] = new File(paths[i]).toURL();
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        classLoader = new ChildFirstClassLoader(cl);
    }

    @Test
    public void testToURLs() throws Exception {
        GenTask genTask = new GenTask();
        URL[] urls = genTask.toURLs(new String[] { "a", "b", "c" });
        assertEquals(3, urls.length);
    }

    @Test
    public void testLoadClass() throws Exception {
        GenTask genTask = new GenTask();
        Class<?> clazz = genTask.loadClass(getClass().getName() + "$Hoge",
                classLoader);
        assertEquals(classLoader, clazz.getClassLoader());
    }

    @Test
    public void testExecute() throws Exception {
        GenTask genTask = new GenTask();
        Class<?> clazz = Class.forName(getClass().getName() + "$Hoge", true,
                classLoader);
        Object hoge = clazz.newInstance();
        genTask.execute(clazz, hoge, classLoader);
        Field field = clazz.getField("foo");
        Object foo = field.get(hoge);
        assertNotNull(foo);
        assertEquals(classLoader, foo.getClass().getClassLoader());
    }

    public static class Hoge {

        public Foo foo;

        public void execute() {
            foo = new Foo();
        }

    }

    public static class Foo {
    }

    public static class ChildFirstClassLoader extends ClassLoader {

        public ChildFirstClassLoader(ClassLoader parent) {
            super(parent);
        }

        @Override
        protected synchronized Class loadClass(String name, boolean resolve)
                throws ClassNotFoundException {
            if (!name.equals(Hoge.class.getName())
                    && !name.equals(Foo.class.getName())) {
                return super.loadClass(name, resolve);
            }
            InputStream is = getParent().getResourceAsStream(
                    name.replace('.', '/') + ".class");
            if (is == null) {
                throw new ClassNotFoundException(name);
            }
            try {
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
    }

}
