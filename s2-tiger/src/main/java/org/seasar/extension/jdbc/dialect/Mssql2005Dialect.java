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
package org.seasar.extension.jdbc.dialect;


/**
 * MS SQLServer用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class Mssql2005Dialect extends MssqlDialect {

    @Override
    public boolean supportsOffset() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        if (offset > 0) {
            return convertLimitSqlByRowNumber(sql, offset, limit);
        }
        return super.convertLimitSql(sql, offset, limit);

    }
}