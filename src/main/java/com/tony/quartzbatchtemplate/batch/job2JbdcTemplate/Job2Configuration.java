package com.tony.quartzbatchtemplate.batch.job2JbdcTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

// https://heowc.dev/programming-study/repo/spring/spring-batch/configuring-step.html

@Profile("jdbc")
@Slf4j
@Configuration
public class Job2Configuration {

   @Autowired
   DataSourceTransactionManager secondTm;

   @Autowired
   public JobBuilderFactory jobBuilderFactory;

   @Autowired
   public StepBuilderFactory stepBuilderFactory;

   @Bean
   public Job job2(JobCompletionNotificationListener listener, Step step1) {
      return jobBuilderFactory.get("job2")
         .incrementer(new RunIdIncrementer())
         .listener(listener)
         .start(step1)
         .on("COMPLETED").to(step2(customItemWriter()))
         .on("FAILED").fail() // 실패하면 아예 멈춰라
         .on("*").to(failureStep())
         .end()
//         .flow(step1)
//         .next(befreStep())
//         .next(step2(customItemWriter()))
//         .end()
         .build();
   }

   @Bean
   public Step befreStep() {
      return stepBuilderFactory.get("befreStep")
         .tasklet((contribution, chunkContext) -> {
            log.info(">>>>> This is befreStep");
            return RepeatStatus.FINISHED;
         })
         .build();
   }

   @Bean
   public Step failureStep() {
      return stepBuilderFactory.get("failureStep")
         .tasklet((contribution, chunkContext) -> {
            log.info(">>>>> This is failureStep");
            return RepeatStatus.FINISHED;
         })
         .build();
   }

   @Bean
   public Step step1(JdbcBatchItemWriter<Person> writer) {
      return stepBuilderFactory.get("step1")
         // https://sheerheart.tistory.com/entry/Spring-Batch-commitinterval%EC%97%90-%EB%8C%80%ED%95%9C-%EC%A0%95%EB%A6%AC
         // commit-interval과 같은 기능임.
         // 개수의 정의 = reader가 읽고, processor가 처리해서 writer에 넘겨지는 갯수를 의미.
         // 그리고 트랜잭션 설정이 되어있다면 이 갯수 단위로 트랜잭션으로 커밋이 발생한다.
         .<Person, Person> chunk(10)
         // 롤백을 안하고자 한다면 이거 지정
         .faultTolerant()
         // 어떤 Exception이 발생하면 재시도를 할지 설정하고, 재시도의 횟수를 설정할 수 있음.
         .retryLimit(3).retry(DeadlockLoserDataAccessException.class)
         // 롤백을 안했을때 어떤 Exception을 호출할지도 지정.
         .noRollback(RuntimeException.class)
         // JMS 같은 큐를 사용하는 경우, 매번 큐에 메시지를 다시 저장하므로 이를 버퍼링하지 않도록 구성
         .reader(reader()).readerIsTransactionalQueue()
         .processor(processor())
         .writer(writer)
         .transactionManager(secondTm)
         .build();
   }

   @Bean
   public Step step2(ItemWriter<Person> customItemWriter) {
      return stepBuilderFactory.get("step2")
         .<Person, Person> chunk(10)
         .reader(reader())
         .processor(processor())
         .writer(customItemWriter)
//         .transactionManager(secondTm)
         .build();
   }




   @Bean
   public FlatFileItemReader<Person> reader() {
      return new FlatFileItemReaderBuilder<Person>()
         .name("personItemReader")
         .resource(new ClassPathResource("sample-data.csv"))
         .delimited()
         .names(new String[]{"firstName", "lastName"})
         .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
            setTargetType(Person.class);
         }})
         .build();
   }

   @Bean
   public PersonItemProcessor processor() {
      return new PersonItemProcessor();
   }

   @Bean
   public ItemWriter<Person> customItemWriter() {
      return items -> {
         for (Person item : items) {
            log.info(item.toString());
         }
      };
   }

   @Bean
   @Retryable(backoff = @Backoff(delay = 1000, maxDelay = 10000, multiplier = 2))
   public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
      return new JdbcBatchItemWriterBuilder<Person>()
         .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
         .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
         .dataSource(dataSource)
         .build();
   }



//   @Bean
//   public JobFactory jobFactory(AutowireCapableBeanFactory beanFactory) {
//      return new SpringBeanJobFactory() {
//         @Override
//         protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
//            Object job = super.createJobInstance(bundle);
//            beanFactory.autowireBean(job);
//            return job;
//         }
//      };
//   }

}
