package com.demo.sql;

import com.demo.entity.Goods;
import com.demo.entity.User;
import com.demo.sql.enums.TableField;
import com.demo.sql.enums.TableId;
import com.demo.sql.enums.TableName;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlScriptUtils {

    @SneakyThrows
    public static <T> String buildInsertSql(T entity) {
        Class<?> clazz = entity.getClass();
        StringBuilder sql = new StringBuilder("insert into ");
        TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
        if (tableNameAnnotation != null) {
            sql.append(tableNameAnnotation.value());
        } else {
            sql.append(clazz.getSimpleName());
        }
        sql.append(" (");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            throw new IllegalStateException("实体类不存在属性");
        }
        String columnNames = Arrays.stream(fields).map(e -> humpToLine(e.getName())).collect(Collectors.joining(","));
        sql.append(columnNames);
        sql.append(") values (");
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(String.class)) {
                sql.append("'");
                field.setAccessible(true);
                sql.append(field.get(entity));
                sql.append("'");
            } else {
                field.setAccessible(true);
                sql.append(field.get(entity));
            }
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    @SneakyThrows
    public static <T> String buildUpdateSql(T entity) {
        Class<?> clazz = entity.getClass();
        StringBuilder sql = new StringBuilder("update ");
        TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
        if (tableNameAnnotation != null) {
            sql.append(tableNameAnnotation.value());
        } else {
            sql.append(clazz.getSimpleName());
        }
        sql.append(" set ");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            throw new IllegalStateException("实体类不存在属性");
        }

        Field idField = null;
        TableId tableId = null;
        for (Field field : fields) {
            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if (tableIdAnnotation != null) {
                tableId = tableIdAnnotation;
                idField = field;
            }
            TableField tableFieldAnnotation = field.getAnnotation(TableField.class);
            if (tableFieldAnnotation != null) {
                sql.append(tableFieldAnnotation.value());
            } else {
                sql.append(humpToLine(field.getName()));
            }

            sql.append(" = ");
            if (field.getType().isAssignableFrom(String.class)) {
                sql.append("'");
                field.setAccessible(true);
                sql.append(field.get(entity));
                sql.append("'");
            } else {
                field.setAccessible(true);
                sql.append(field.get(entity));
            }
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ");

        if (tableId == null) {
            throw new IllegalStateException("缺少主键");
        }
        if (tableId.value().equals("")) {
            sql.append(humpToLine(idField.getName()));
        } else {
            sql.append(tableId.value());
        }
        sql.append(" = ");
        if (idField.getType().isAssignableFrom(String.class)) {
            sql.append("'");
            idField.setAccessible(true);
            sql.append(idField.get(entity));
            sql.append("'");
        } else {
            idField.setAccessible(true);
            sql.append(idField.get(entity));
        }

        return sql.toString();
    }

    @SneakyThrows
    public static <T> String buildDeleteSql(T entity) {
        Class<?> clazz = entity.getClass();
        StringBuilder sql = new StringBuilder("delete from ");
        TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
        if (tableNameAnnotation != null) {
            sql.append(tableNameAnnotation.value());
        } else {
            sql.append(clazz.getSimpleName());
        }
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            throw new IllegalStateException("实体类不存在属性");
        }
        sql.append(" where ");
        for (Field field : fields) {
            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if (tableIdAnnotation != null) {
                if (tableIdAnnotation.value().equals("")) {
                    sql.append(humpToLine(field.getName()));
                } else {
                    sql.append(tableIdAnnotation.value());
                }
                sql.append(" = ");
                if (field.getType().isAssignableFrom(String.class)) {
                    sql.append("'");
                    field.setAccessible(true);
                    sql.append(field.get(entity));
                    sql.append("'");
                } else {
                    field.setAccessible(true);
                    sql.append(field.get(entity));
                }
                break;
            }
        }

        return sql.toString();
    }

    @SneakyThrows
    public static <T> String buildSelectSql(T entity) {
        Class<?> clazz = entity.getClass();
        StringBuilder sql = new StringBuilder("select * from ");
        TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
        if (tableNameAnnotation != null) {
            sql.append(tableNameAnnotation.value());
        } else {
            sql.append(clazz.getSimpleName());
        }
        Field[] fields = clazz.getDeclaredFields();
        sql.append(" where ");
        for (Field field : fields) {
            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if (tableIdAnnotation != null) {
                if (tableIdAnnotation.value().equals("")) {
                    sql.append(humpToLine(field.getName()));
                } else {
                    sql.append(tableIdAnnotation.value());
                }
                sql.append(" = ");
                if (field.getType().isAssignableFrom(String.class)) {
                    sql.append("'");
                    field.setAccessible(true);
                    sql.append(field.get(entity));
                    sql.append("'");
                } else {
                    field.setAccessible(true);
                    sql.append(field.get(entity));
                }
                break;
            }
        }
        return sql.toString();
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)})
     */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    private static final Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线,效率比上面高
     */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId("123456");
        user.setAge(18);
        user.setName("张三");

        Goods goods = new Goods();
        goods.setGoodsId(123456);
        goods.setGoodsTitle("苹果");
        goods.setGoodsCount(100);

        System.out.println("insert sql:");
        System.out.println(buildInsertSql(user));
        System.out.println(buildInsertSql(goods));
        System.out.println("update sql:");
        System.out.println(buildUpdateSql(user));
        System.out.println(buildUpdateSql(goods));
        System.out.println("delete sql:");
        System.out.println(buildDeleteSql(user));
        System.out.println(buildDeleteSql(goods));
        System.out.println("select sql:");
        System.out.println(buildSelectSql(user));
        System.out.println(buildSelectSql(goods));
    }
}
