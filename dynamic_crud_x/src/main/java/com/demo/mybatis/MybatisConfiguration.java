package com.demo.mybatis;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * replace default Configuration class
 */
public class MybatisConfiguration extends Configuration {
    private static final Log logger = LogFactory.getLog(MybatisConfiguration.class);
    /**
     * Mapper 注册
     */
    protected final MybatisMapperRegistry mybatisMapperRegistry = new MybatisMapperRegistry(this);

    public MybatisConfiguration(Environment environment) {
        this();
        this.environment = environment;
    }

    /**
     * 初始化调用
     */
    public MybatisConfiguration() {
        super();
    }

    /**
     * MybatisPlus 加载 SQL 顺序：
     * <p> 1、加载 XML中的 SQL </p>
     * <p> 2、加载 SqlProvider 中的 SQL </p>
     * <p> 3、XmlSql 与 SqlProvider不能包含相同的 SQL </p>
     * <p>调整后的 SQL优先级：XmlSql > sqlProvider > CurdSql </p>
     */
    @Override
    public void addMappedStatement(MappedStatement ms) {
        logger.debug("addMappedStatement: " + ms.getId());
        if (mappedStatements.containsKey(ms.getId())) {
            /*
             * 说明已加载了xml中的节点； 忽略mapper中的 SqlProvider 数据
             */
            logger.error("mapper[" + ms.getId() + "] is ignored, because it exists, maybe from xml file");
            return;
        }
        mappedStatements.put(ms.getId(), ms);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public MapperRegistry getMapperRegistry() {
        return mybatisMapperRegistry;
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public <T> void addMapper(Class<T> type) {
        mybatisMapperRegistry.addMapper(type);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public void addMappers(String packageName, Class<?> superType) {
        mybatisMapperRegistry.addMappers(packageName, superType);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public void addMappers(String packageName) {
        mybatisMapperRegistry.addMappers(packageName);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mybatisMapperRegistry.getMapper(type, sqlSession);
    }

    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public boolean hasMapper(Class<?> type) {
        return mybatisMapperRegistry.hasMapper(type);
    }

}
