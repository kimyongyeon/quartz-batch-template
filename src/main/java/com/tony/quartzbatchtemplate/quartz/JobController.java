package com.tony.quartzbatchtemplate.quartz;

import com.tony.quartzbatchtemplate.executor.Job1Execute;
import com.tony.quartzbatchtemplate.executor.Job2Execute;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Controller
public class JobController {

   @Autowired
   private Scheduler scheduler;

   AtomicInteger atomicInteger = new AtomicInteger(0);

   @PostConstruct
   public void start() {
      JobDetail job1 = buildJobDetail(Job1Execute.class, "job1", "jobs", new HashMap<>());
      JobDetail job2 = buildJobDetail(Job2Execute.class, "job2", "jobs", new HashMap<>());
      try {

         Map<JobDetail, Set<? extends Trigger>> jobMap = new HashMap<>();
         Set<Trigger> triggers = new HashSet<>();
         triggers.add(buildJobTrigger("0 * * * * ?"));
         triggers.add(buildJobTrigger("1/5 * * * * ?"));
         jobMap.put(job1, triggers);

         triggers = new HashSet<>();
         triggers.add(buildJobTrigger("1/10 * * * * ?"));
         jobMap.put(job2, triggers);

         scheduler.scheduleJobs(jobMap, true);
//         scheduler.scheduleJob(agg, buildJobTrigger("0 * * * * ?"));

      } catch (SchedulerException e) {
         e.printStackTrace();
      }
   }

   //String scheduleExp ="0 40 11 * * ?"; 초 분 시 일 월 ?
   public Trigger buildJobTrigger(String scheduleExp) {
      // todo: 트리거를 걸면 원하는 클래스를 호출하는데 이건 어디서 해주는 걸까?
      return newTrigger()
         .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
   }

   public JobDetail buildJobDetail(Class job, String name, String group, Map params) {
      log.info("jobController seq: " + atomicInteger.get()+"");
      return newJob(job).withIdentity(name, group)
         .usingJobData("jobId", "jobController class")
         .build();
   }


}
