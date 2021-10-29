/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.mybatis.injector.methods;

import com.demo.mybatis.injector.AbstractMethod;
import com.demo.sql.SqlScriptUtils;
import com.demo.sql.enums.TableId;
import com.demo.sql.enums.TableName;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;

/**
 * 根据 ID 删除
 */
public class DeleteById extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        SqlMethod sqlMethod = SqlMethod.DELETE_BY_ID;
        String tableName;
        TableName tableNameAnnotation = modelClass.getAnnotation(TableName.class);
        if (tableNameAnnotation != null) {
            tableName = tableNameAnnotation.value();
        } else {
            tableName = modelClass.getSimpleName();
        }
        Field[] fields = modelClass.getDeclaredFields();
        if (fields.length == 0) {
            throw new IllegalStateException("实体类不存在属性");
        }
        String keyColumn = null;
        String keyProperty = null;
        for (Field field : fields) {
            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if (tableIdAnnotation != null) {
                if (tableIdAnnotation.value().equals("")) {
                    keyColumn = SqlScriptUtils.humpToLine(field.getName());
                } else {
                    keyColumn = tableIdAnnotation.value();
                }
                keyProperty = field.getName();
                break;
            }
        }
        String sql = String.format(sqlMethod.getSql(), tableName, keyColumn, keyProperty);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
        return this.addDeleteMappedStatement(mapperClass, SqlMethod.DELETE_BY_ID.getMethod(), sqlSource);
    }

}
