package com.im;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
/**
 * Application类说明自己是Spring Boot的入口类，那么需要加入@Configuration注解。
 * 
 * 习惯放在主方法类Application上@EnableAutoConfiguration，当项目运行时，Spring容器去自动查找带特定注解的类，如：带@Entity、@Service等类
 * 
 * 如果不带basePackage 属性的话@ComponentScan，它会自动扫描以入口类所在的包为父节点下所有子包下的类。这也是Spring Boot会提议我们把Application类放于根包路径下。
 * 
 * *****/
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@MapperScan("com.im.user.mapper")
public class Application {

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return new org.apache.tomcat.jdbc.pool.DataSource();
	}
	
	/*****加载mybatis的文件*****/
	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver
				.getResources("classpath:/mybatis/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        logger.info("SpringBoot Start Success");
    }
}
