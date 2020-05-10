package com.tony.quartzbatchtemplate.executor;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.SyncTaskExecutor;


@Slf4j
public class Job1Execute implements org.quartz.Job {

   @Autowired
   private JobLocator jobLocator;

   @Autowired
   private JobLauncher jobLauncher;



   @Autowired
   private ApplicationContext ctx;


   @Override
   public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

//      JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap(); // 최초 한번만 파라미터로 받는다.
//      int seq = (int) jobDataMap.get("seq");
//      log.info("Job1Execute: " + seq + "");

      Job job1 = ctx.getBean("job1", Job.class);
      try {



         JobExecution jobExecution = jobLauncher.run(job1, new JobParameters());

         log.info("job1 instance id: " + jobExecution.getId());


      } catch (JobExecutionAlreadyRunningException e) {
         e.printStackTrace();
      } catch (JobRestartException e) {
         e.printStackTrace();
      } catch (JobInstanceAlreadyCompleteException e) {
         e.printStackTrace();
      } catch (JobParametersInvalidException e) {
         e.printStackTrace();
      }
   }

}
