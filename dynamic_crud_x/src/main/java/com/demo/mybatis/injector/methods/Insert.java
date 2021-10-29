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
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 插入一条数据（选择字段插入）
 *
 * @author hubin
 * @since 2018-04-06
 */
public class Insert extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass) {
        return null;
    }
}
