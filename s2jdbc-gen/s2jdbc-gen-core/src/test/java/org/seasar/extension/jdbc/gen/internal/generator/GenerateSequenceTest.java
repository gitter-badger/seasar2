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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.SequenceDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.dialect.HsqlGenDialect;
import org.seasar.extension.jdbc.gen.internal.model.DdlModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.DdlModel;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateSequenceTest {

    private GeneratorImplStub generator;

    private DdlModel model;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();

        SequenceDesc sequenceDesc = new SequenceDesc();
        sequenceDesc.setSequenceName("HOGE");
        sequenceDesc.setInitialValue(1);
        sequenceDesc.setAllocationSize(50);
        sequenceDesc.setDataType("integer");

        SequenceDesc sequenceDesc2 = new SequenceDesc();
        sequenceDesc2.setSequenceName("FOO");
        sequenceDesc2.setInitialValue(10);
        sequenceDesc2.setAllocationSize(20);
        sequenceDesc2.setDataType("integer");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCanonicalName("aaa");
        tableDesc.addSequenceDesc(sequenceDesc);

        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.setCanonicalName("bbb");
        tableDesc2.addSequenceDesc(sequenceDesc2);

        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.addTableDesc(tableDesc);
        databaseDesc.addTableDesc(tableDesc2);

        DdlModelFactoryImpl factory = new DdlModelFactoryImpl(
                new HsqlGenDialect(), SqlKeywordCaseType.ORIGINALCASE,
                SqlIdentifierCaseType.ORIGINALCASE, ';', "SCHEMA_INFO",
                "VERSION", null);
        model = factory.getDdlModel(databaseDesc, 0);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-sequence.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Create.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDrop() throws Exception {
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/drop-sequence.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
