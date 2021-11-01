package com.demo.mybatis.injector.methods;

import com.demo.mybatis.enums.TableId;
import com.demo.mybatis.enums.TableName;
import com.demo.mybatis.injector.AbstractMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;

/**
 * 根据ID 查询一条数据
 */
public class SelectById extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        SqlMethod sqlMethod = SqlMethod.SELECT_BY_ID;
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
                    keyColumn = humpToLine(field.getName());
                } else {
                    keyColumn = tableIdAnnotation.value();
                }
                keyProperty = field.getName();
                break;
            }
        }
        String sql = String.format(sqlMethod.getSql(), "*", tableName, keyColumn, keyProperty, "");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
        return this.addSelectMappedStatementForTable(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
    }
}
