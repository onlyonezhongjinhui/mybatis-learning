package com.demo.mybatis;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Configuration properties for MyBatis.
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "mybatis-x")
public class MybatisProperties {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * Location of MyBatis xml config file.
     */
    private String configLocation;

    /**
     * Locations of MyBatis mapper files.
     *
     * @since 3.1.2 add default value
     */
    private String[] mapperLocations = new String[]{"classpath*:/mapper/**/*.xml"};

    /**
     * Packages to search type aliases. (Package delimiters are ",; \t\n")
     */
    private String typeAliasesPackage;

    /**
     * The super class for filtering type alias.
     * If this not specifies, the MyBatis deal as type alias all classes that searched from typeAliasesPackage.
     */
    private Class<?> typeAliasesSuperType;

    /**
     * Packages to search for type handlers. (Package delimiters are ",; \t\n")
     */
    private String typeHandlersPackage;

    /**
     * Indicates whether perform presence check of the MyBatis xml config file.
     */
    private boolean checkConfigLocation = false;

    /**
     * Execution mode for {@link org.mybatis.spring.SqlSessionTemplate}.
     */
    private ExecutorType executorType;

    /**
     * The default scripting language driver class. (Available when use together with mybatis-spring 2.0.2+)
     * <p>
     * ?????????????????????,?????????????????????????????? mp ???????????????
     */
    private Class<? extends LanguageDriver> defaultScriptingLanguageDriver;

    /**
     * Externalized properties for MyBatis configuration.
     */
    private Properties configurationProperties;

    /**
     * A Configuration object for customize default settings. If {@link #configLocation}
     * is specified, this property is not used.
     * TODO ?????? MybatisConfiguration
     */
    @NestedConfigurationProperty
    private MybatisConfiguration configuration;

    /**
     * TODO ???????????????
     */
    private String typeEnumsPackage;

    public Resource[] resolveMapperLocations() {
        return Stream.of(Optional.ofNullable(this.mapperLocations).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }
}
