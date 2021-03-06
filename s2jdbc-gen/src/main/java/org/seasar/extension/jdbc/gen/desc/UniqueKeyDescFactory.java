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
package org.seasar.extension.jdbc.gen.desc;

import javax.persistence.UniqueConstraint;

/**
 * {@link UniqueKeyDesc 一意キー記述}のファクトリです。
 * 
 * @author taedium
 */
public interface UniqueKeyDescFactory {

    /**
     * 複数のカラムで表される一意制約の一意キー記述を返します。
     * 
     * @param uniqueConstraint
     *            一意制約
     * @return 存在する場合は一意キー記述、存在しない場合は{@code null}
     */
    UniqueKeyDesc getCompositeUniqueKeyDesc(UniqueConstraint uniqueConstraint);

    /**
     * 単一のカラムで表される一意制約の一意キー記述を返します。
     * 
     * @param columnDesc
     *            カラム記述
     * @return 存在する場合は一意キー記述、存在しない場合は{@code null}
     */
    UniqueKeyDesc getSingleUniqueKeyDesc(ColumnDesc columnDesc);

}