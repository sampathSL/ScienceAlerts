/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.scheduletasks;

import com.appzone.sciencealerts.properties.PropertyFileReader;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author TEST
 */
public class ScheduleTaskManager implements ServletContextListener {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ScheduleTaskManager.class);
    public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";
    private ServletContext ctx = null;
    private StdSchedulerFactory factory = null;
    /**
     *
     * @param event
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            ctx = event.getServletContext();
            factory = new StdSchedulerFactory();
            logger.info("Storing QuartzScheduler Factory at" + QUARTZ_FACTORY_KEY);
            ctx.setAttribute(QUARTZ_FACTORY_KEY,factory);
            JobDetail jDetail1 = new JobDetail("Import_Alerts1","NJob1",DataFeedScheduleTask1.class);
            JobDetail jDetail2 = new JobDetail("Import_Alerts2","NJob2",DataFeedScheduleTask2.class);
            JobDetail jDetail3 = new JobDetail("Import_Alerts3","NJob3",DataFeedScheduleTask3.class);
            JobDetail jDetail4 = new JobDetail("Import_Alerts4","NJob4",DataFeedScheduleTask4.class);
            JobDetail jDetail5 = new JobDetail("Import_Alerts5","NJob5",DataFeedScheduleTask5.class);
            JobDetail jDetail6 = new JobDetail("Import_Alerts6","NJob6",DataFeedScheduleTask6.class);
            JobDetail jDetail7 = new JobDetail("Send_Daily_Alerts1","NJob7",DailyAlertsScheduleTask.class);
            JobDetail jDetail8 = new JobDetail("Send_Daily_Alerts2","NJob8",DailyAlertsScheduleTask.class);
            CronTrigger crTrigger1 = new CronTrigger("cronTrigger1","NJob1",PropertyFileReader.getValue("SCHEDULE_TIME_IMPORT_ALERTS1"));
            CronTrigger crTrigger2 = new CronTrigger("cronTrigger2","NJob2",PropertyFileReader.getValue("SCHEDULE_TIME_IMPORT_ALERTS2"));
            CronTrigger crTrigger3 = new CronTrigger("cronTrigger3","NJob3",PropertyFileReader.getValue("SCHEDULE_TIME_IMPORT_ALERTS3"));
            CronTrigger crTrigger4 = new CronTrigger("cronTrigger4","NJob4",PropertyFileReader.getValue("SCHEDULE_TIME_IMPORT_ALERTS4"));
            CronTrigger crTrigger5 = new CronTrigger("cronTrigger5","NJob5",PropertyFileReader.getValue("SCHEDULE_TIME_IMPORT_ALERTS5"));
            CronTrigger crTrigger6 = new CronTrigger("cronTrigger6","NJob6",PropertyFileReader.getValue("SCHEDULE_TIME_IMPORT_ALERTS6"));
            CronTrigger crTrigger7 = new CronTrigger("cronTrigger7","NJob7",PropertyFileReader.getValue("SCHEDULE_TIME_SEND_ALERTS1"));
            CronTrigger crTrigger8 = new CronTrigger("cronTrigger8","NJob8",PropertyFileReader.getValue("SCHEDULE_TIME_SEND_ALERTS2"));
            factory.getScheduler().scheduleJob(jDetail1,crTrigger1);
            factory.getScheduler().scheduleJob(jDetail2,crTrigger2);
            factory.getScheduler().scheduleJob(jDetail3,crTrigger3);
            factory.getScheduler().scheduleJob(jDetail4,crTrigger4);
            factory.getScheduler().scheduleJob(jDetail5,crTrigger5);
            factory.getScheduler().scheduleJob(jDetail6,crTrigger6);
            factory.getScheduler().scheduleJob(jDetail7,crTrigger7);
             factory.getScheduler().scheduleJob(jDetail8,crTrigger8);
            
            factory.getScheduler().start();
            
        } catch (Exception ex) {
            logger.error("Quartz failed to initialize", ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            factory.getScheduler().shutdown();
        } catch (SchedulerException ex) {
            logger.error("Quartz failed to shutdown", ex);
        }
    }
}
