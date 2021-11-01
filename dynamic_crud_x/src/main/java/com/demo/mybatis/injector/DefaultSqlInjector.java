package com.demo.mybatis.injector;

import com.demo.mybatis.injector.methods.DeleteById;
import com.demo.mybatis.injector.methods.Insert;
import com.demo.mybatis.injector.methods.SelectById;
import com.demo.mybatis.injector.methods.UpdateById;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * SQL 默认注入器
 */
public class DefaultSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return Stream.of(
                new DeleteById(),
                new SelectById(),
                new UpdateById(),
                new Insert()
        ).collect(toList());
    }
}
