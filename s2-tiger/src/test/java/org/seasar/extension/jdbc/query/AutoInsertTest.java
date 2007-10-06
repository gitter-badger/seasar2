/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

/**
 * @author koichik
 */
public class AutoInsertTest extends TestCase {

    private JdbcManagerImpl manager;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());

        PersistenceConventionImpl convention = new PersistenceConventionImpl();
        EntityMetaFactoryImpl emFactory = new EntityMetaFactoryImpl();
        emFactory.setPersistenceConvention(convention);
        TableMetaFactoryImpl tableMetaFactory = new TableMetaFactoryImpl();
        tableMetaFactory.setPersistenceConvention(convention);
        emFactory.setTableMetaFactory(tableMetaFactory);

        PropertyMetaFactoryImpl pFactory = new PropertyMetaFactoryImpl();
        pFactory.setPersistenceConvention(convention);
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(convention);
        pFactory.setColumnMetaFactory(cmFactory);
        emFactory.setPropertyMetaFactory(pFactory);
        emFactory.initialize();
        manager.setEntityMetaFactory(emFactory);
    }

    @Override
    protected void tearDown() throws Exception {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
        manager = null;
    }

    /**
     * 
     */
    public void testCallerClass() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testExcludesNull() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertFalse(query.excludesNull);
        assertSame(query, query.excludesNull());
        assertTrue(query.excludesNull);
    }

    /**
     * 
     */
    public void testIncludes() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertTrue(query.excludesProperties.isEmpty());

        assertSame(query, query.includes("name"));
        assertEquals(1, query.includesProperties.size());
        assertTrue(query.includesProperties.contains("name"));
        assertFalse(query.includesProperties.contains("id"));
        assertFalse(query.includesProperties.contains("version"));

        assertSame(query, query.includes("id", "version"));
        assertEquals(3, query.includesProperties.size());
        assertTrue(query.includesProperties.contains("name"));
        assertTrue(query.includesProperties.contains("id"));
        assertTrue(query.includesProperties.contains("version"));
    }

    /**
     * 
     */
    public void testExcludes() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertTrue(query.excludesProperties.isEmpty());

        assertSame(query, query.excludes("name"));
        assertEquals(1, query.excludesProperties.size());
        assertTrue(query.excludesProperties.contains("name"));
        assertFalse(query.excludesProperties.contains("id"));
        assertFalse(query.excludesProperties.contains("version"));

        assertSame(query, query.excludes("id", "version"));
        assertEquals(3, query.excludesProperties.size());
        assertTrue(query.excludesProperties.contains("name"));
        assertTrue(query.excludesProperties.contains("id"));
        assertTrue(query.excludesProperties.contains("version"));
    }

    /**
     * 
     */
    public void testPrepareTarget() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepareTargetProperties();
        assertEquals(4, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
        assertEquals("fffId", query.targetProperties.get(2).getName());
        assertEquals("version", query.targetProperties.get(3).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.includes("id", "version");
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("version", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.excludes("name");
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("fffId", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesAndExcludes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.includes("id", "name", "fffId").excludes("fffId");
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesAndExcludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.includes("id", "name", "fffId").excludesNull();
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareIntoClause() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(" (ID, NAME, FFF_ID, VERSION)", query.intoClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareValuesClause() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(" values (?, ?, ?, ?)", query.valuesClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareParams() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(4, query.bindVariableList.size());
        assertEquals(new Integer(100), query.bindVariableList.get(0));
        assertEquals("hoge", query.bindVariableList.get(1));
        assertNull(query.bindVariableList.get(2));
        assertEquals(new Long(1L), query.bindVariableList.get(3));
    }

    /**
     * @throws Exception
     */
    public void testPrepareParams_excludesNull() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepare("execute");
        assertEquals(3, query.bindVariableList.size());
        assertEquals(new Integer(100), query.bindVariableList.get(0));
        assertEquals("hoge", query.bindVariableList.get(1));
        assertEquals(new Long(1L), query.bindVariableList.get(2));
    }

    /**
     * 
     */
    public void testPrepareSql() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(
                "insert into EEE (ID, NAME, FFF_ID, VERSION) values (?, ?, ?, ?)",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_excludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepare("execute");
        assertEquals("insert into EEE (ID, NAME, VERSION) values (?, ?, ?)",
                query.executedSql);
    }

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 1;
                    }
                };
                return ps;
            }

        };
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into EEE (ID, NAME, FFF_ID, VERSION) values (100, 'hoge', null, 1)",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_excludesNull() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 1;
                    }
                };
                return ps;
            }

        };
        query.excludesNull();
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into EEE (ID, NAME, VERSION) values (100, 'hoge', 1)",
                sqlLog.getCompleteSql());
    }

}