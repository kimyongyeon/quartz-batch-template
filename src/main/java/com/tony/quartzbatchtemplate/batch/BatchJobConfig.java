package com.tony.quartzbatchtemplate.batch;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class BatchJobConfig {

   @Autowired
   JobRepository jobRepository;

   @Bean
   RetryTemplate retryTemplate() {
      RetryTemplate retryTemplate = new RetryTemplate();
      retryTemplate.setBackOffPolicy(backOffPolicy());
      return retryTemplate;
   }

   @Bean
   public ExponentialBackOffPolicy backOffPolicy() {
      ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
      // 제일 처음 시도하는 1초를 설정
      backOffPolicy.setInitialInterval(1000);
      // 최대 10000초까지 다다른 이후에는 일정한 간격으로 재시도를 계속 한다.
      backOffPolicy.setMaxInterval(10000);
      // 배수를 2배로 하여 다음 시도까지 2씩 늘린다.
      backOffPolicy.setMultiplier(2);
      return backOffPolicy;
   }

   @Bean
   SimpleJobLauncher simpleJobLauncher() {
      SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
      simpleJobLauncher.setJobRepository(jobRepository);
      return simpleJobLauncher;
   }
}
