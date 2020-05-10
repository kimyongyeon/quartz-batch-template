package com.tony.quartzbatchtemplate.executor;

import lombok.extern.slf4j.Slf4j;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Job2Execute extends QuartzJobBean implements InterruptableJob {

   @Autowired
   private JobLauncher jobLauncher;

   @Autowired
   private ApplicationContext ctx;

   @Autowired
   private SimpleJobLauncher simpleJobLauncher;

   @Override
   public void interrupt() throws UnableToInterruptJobException {
   }

   @Override
   protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

      // todo: 변화하는 파라미터를 받는 방법은?

//      JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap(); // 최초 한번만 파라미터로 받는다.
//      int seq = (int) jobDataMap.get("seq");
//      log.info("Job2Execute: " + seq+"");

      try {
//         Job job2 = ctx.getBean("job2", Job.class);
//         simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
//         simpleJobLauncher.setTaskExecutor(new SyncTaskExecutor());
//         JobExecution jobExecution = simpleJobLauncher.run(job2, new JobParameters());
//         JobExecution jobExecution = jobLauncher.run(job2, new JobParameters());
//         log.info("job2 instance id: " + jobExecution.getId());
         log.info("job3 instance id: start");
         Job job3 = ctx.getBean("job3", Job.class);
         simpleJobLauncher.setTaskExecutor(new SyncTaskExecutor());
         JobExecution jobExecution = simpleJobLauncher.run(job3, new JobParameters());
//         JobExecution jobExecution = jobLauncher.run(job3, new JobParameters());
         log.info("job3 instance id: " + jobExecution.getId() + " end");
         int zero = 0;
         int cal = 4815 / zero;

      } catch (Exception e) {
         log.info("--- Error in job!");
         JobExecutionException e2 = new JobExecutionException(e);
         // Quartz will automatically unschedule
         // all triggers associated with this job
         // so that it does not run again
         e2.setUnscheduleAllTriggers(true);
         throw e2;
      }

   }
}
