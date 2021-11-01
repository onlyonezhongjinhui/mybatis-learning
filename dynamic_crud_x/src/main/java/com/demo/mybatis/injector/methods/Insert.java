package com.demo.mybatis.injector.methods;

import com.demo.mybatis.enums.TableId;
import com.demo.mybatis.enums.TableName;
import com.demo.mybatis.injector.AbstractMethod;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 插入一条数据
 */
public class Insert extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
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
        String columns = " ( " + Arrays.stream(fields).map(e -> humpToLine(e.getName())).collect(Collectors.joining(",")) + ")";
        StringBuilder values = new StringBuilder();
        values.append(" ( ");
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
            }
            values.append("#{");
            values.append(field.getName());
            values.append("},");
        }
        values.deleteCharAt(values.length() - 1);
        values.append(" ) ");
        String sql = String.format(sqlMethod.getSql(), tableName, columns, values);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
        KeyGenerator keyGenerator = new NoKeyGenerator();
        return this.addInsertMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource, keyGenerator, keyColumn, keyProperty);
    }

}
