package com.tony.quartzbatchtemplate.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class SampleController {
   @Autowired
   private JobLauncher jobLauncher;

   @Autowired
   private ApplicationContext ctx;

   AtomicInteger atomicInteger = new AtomicInteger(1);

   @GetMapping("/sampleJob")
   public int sampleJob(String jobName) {

      Job job = null;
      if ("job1".equals(jobName)) {
         job = ctx.getBean("job1", Job.class);
      } else if ("job2".equals(jobName)) {
         job = ctx.getBean("job2", Job.class);
      } else {
         job = ctx.getBean("job3", Job.class);
      }
      try {
         jobLauncher.run(job, new JobParameters());


         return atomicInteger.incrementAndGet();

      } catch (JobExecutionAlreadyRunningException e) {
         e.printStackTrace();
      } catch (JobRestartException e) {
         e.printStackTrace();
      } catch (JobInstanceAlreadyCompleteException e) {
         e.printStackTrace();
      } catch (JobParametersInvalidException e) {
         e.printStackTrace();
      }

      return 0;
   }
}
