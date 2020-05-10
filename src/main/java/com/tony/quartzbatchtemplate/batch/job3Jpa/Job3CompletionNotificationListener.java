package com.tony.quartzbatchtemplate.batch.job3Jpa;

import com.tony.quartzbatchtemplate.batch.job2JbdcTemplate.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Job3CompletionNotificationListener extends JobExecutionListenerSupport {
   private final JdbcTemplate jdbcTemplate;

   @Autowired
   public Job3CompletionNotificationListener(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   @Override
   public void afterJob(JobExecution jobExecution) {
      if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
         log.info("!!! JOB FINISHED! Time to verify the results");

         jdbcTemplate.query("SELECT id, first_name, last_name FROM PERSON2 ",
            (rs, row) -> new Person2(
               rs.getLong(1),
               rs.getString(2),
               rs.getString(3))
         ).forEach(person -> log.info("Found <" + person + "> in the database."));
      }
   }
}
