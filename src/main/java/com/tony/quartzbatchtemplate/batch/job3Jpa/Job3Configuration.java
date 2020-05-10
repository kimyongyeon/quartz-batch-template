package com.tony.quartzbatchtemplate.batch.job3Jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

//@Profile("jpa")
@Slf4j
@Configuration
public class Job3Configuration implements InitializingBean {

   @Autowired
   public JobBuilderFactory jobBuilderFactory;

   @Autowired
   public StepBuilderFactory stepBuilderFactory;

   @Autowired
   private Person2Repository person2Repository;

//   @Autowired
//   DataSourceTransactionManager secondTm;

   @Bean
   public Job job3(Job3CompletionNotificationListener listener, Step step) {
      return jobBuilderFactory.get("job3")
         .incrementer(new RunIdIncrementer())
         .listener(listener)
         .start(step)
         .build();
   }

   @Bean
   public Step step(ItemWriter<Person2> jpaItemWriter) {
      return stepBuilderFactory.get("step")
         .<Person2, Person2> chunk(10)
         .reader(person2Reader())
         .processor(person2Processor())
         .writer(jpaItemWriter)
//         .transactionManager(secondTm)
         .build();
   }

   @Bean
   public FlatFileItemReader<Person2> person2Reader() {
      return new FlatFileItemReaderBuilder<Person2>()
         .name("personItemReader")
         .resource(new ClassPathResource("sample-data.csv"))
         .delimited()
         .names(new String[]{"firstName", "lastName"})
         .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
            setTargetType(Person2.class);
         }})
         .build();
   }

   @Bean
   public Person2ItemProcessor person2Processor() {
      return new Person2ItemProcessor();
   }

   @Bean
   public ItemWriter<Person2> jpaItemWriter() {
      return items -> {
         person2Repository.saveAll(items);
      };
   }

   @Override
   public void afterPropertiesSet() throws Exception {
      log.info("job3Configuration start");
   }
}
