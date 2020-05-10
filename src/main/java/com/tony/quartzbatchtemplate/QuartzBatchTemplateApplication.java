package com.tony.quartzbatchtemplate;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

@EnableCaching
@EnableBatchProcessing
@EnableRetry
@SpringBootApplication
//@SpringBootApplication(exclude = {
//   DataSourceAutoConfiguration.class,
//   DataSourceTransactionManagerAutoConfiguration.class,
//   HibernateJpaAutoConfiguration.class
//})
public class QuartzBatchTemplateApplication {

   public static void main(String[] args) {
      SpringApplication.run(QuartzBatchTemplateApplication.class, args);
   }

}
