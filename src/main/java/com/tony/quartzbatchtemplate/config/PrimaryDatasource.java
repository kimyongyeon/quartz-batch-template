package com.tony.quartzbatchtemplate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Profile("jpa")
@Configuration
public class PrimaryDatasource {

   @Primary
   @Bean(name="dataSource")
   @ConfigurationProperties(prefix="spring.primary.datasource")
   public DataSource dataSource() {
      return DataSourceBuilder.create().build();
   }

   @Primary
   @Bean(name="transactionManager")
   public DataSourceTransactionManager transactionManager(@Autowired @Qualifier("dataSource") DataSource dataSource) {
      return new DataSourceTransactionManager(dataSource);
   }

//   @Primary
//   @Bean(name="sqlSessionFactory")
//   public SqlSessionFactory sqlSessionFactoryBean(@Autowired @Qualifier("dataSource") DataSource dataSource, ApplicationContext applicationContext)
//      throws Exception {
//      SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//      factoryBean.setDataSource(dataSource);
//      factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config-primary.xml"));
//      factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper-primary/**/*.xml"));
//      return factoryBean.getObject();
//   }

//   @Primary
//   @Bean(name="sqlSession")
//   public SqlSessionTemplate sqlSession(@Autowired @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//      return new SqlSessionTemplate(sqlSessionFactory);
//   }


}
