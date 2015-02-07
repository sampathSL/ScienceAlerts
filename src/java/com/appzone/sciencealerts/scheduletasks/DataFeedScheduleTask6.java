/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.scheduletasks;

import com.appzone.sciencealerts.properties.PropertyFileReader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author SAM
 */
public class DataFeedScheduleTask6  implements Job{

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        DataFeedScheduleTask df = new DataFeedScheduleTask();
        df.FindAlerts(PropertyFileReader.getValue("URL11"));
        df.FindAlerts(PropertyFileReader.getValue("URL12"));
    }
    
}


