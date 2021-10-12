package com.demo.handler;

import com.demo.enums.EnumValue;
import com.demo.enums.IEnum;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class CommonEnumTypeHandler<E extends Enum<?>> extends BaseTypeHandler<Enum<?>> {
    private static final ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    private final Class<E> type;
    private final Invoker invoker;

    public CommonEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        MetaClass metaClass = MetaClass.forClass(type, reflectorFactory);
        String name = "value";
        if (!IEnum.class.isAssignableFrom(type)) {
            name = findEnumValueFieldName(this.type).orElseThrow(() -> new IllegalArgumentException(String.format("Could not find @EnumValue in Class: %s.", this.type.getName())));
        }
        this.invoker = metaClass.getGetInvoker(name);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Enum<?> parameter, JdbcType jdbcType)
            throws SQLException {
        if (jdbcType == null) {
            ps.setObject(i, this.getValue(parameter));
        } else {
            // see r3589
            ps.setObject(i, this.getValue(parameter), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == rs.getObject(columnName) && rs.wasNull()) {
            return null;
        }
        return this.valueOf(this.type, rs.getObject(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs.getObject(columnIndex) && rs.wasNull()) {
            return null;
        }
        return this.valueOf(this.type, rs.getObject(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex) && cs.wasNull()) {
            return null;
        }
        return this.valueOf(this.type, cs.getObject(columnIndex));
    }

    private E valueOf(Class<E> enumClass, Object value) {
        E[] es = enumClass.getEnumConstants();
        return Arrays.stream(es).filter((e) -> value.equals(getValue(e))).findAny().orElse(null);
    }

    private Object getValue(Object object) {
        try {
            return invoker.invoke(object, new Object[0]);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找标记标记EnumValue字段
     *
     * @param clazz class
     */
    public static Optional<String> findEnumValueFieldName(Class<?> clazz) {
        if (clazz != null && clazz.isEnum()) {
            Optional<Field> optional = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(EnumValue.class))
                    .findFirst();
            return Optional.of(optional.map(Field::getName).orElseThrow(() -> new IllegalArgumentException("cannot find enum value")));
        }
        return Optional.empty();
    }

}
