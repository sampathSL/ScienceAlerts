package com.appzone.sciencealerts.scheduletasks;

import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import com.appzone.sciencealerts.service.ScienceAlert;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DailyAboutToDeactiveCustomers
        implements Job {

    private String responceMsg;
    private MchoiceAventuraSmsSender aboutToDeactiveSmsSender;
    private static final Logger logger = Logger.getLogger(DailyAboutToDeactiveCustomers.class);

    public DailyAboutToDeactiveCustomers() {
        responceMsg = "";
    }

    public void execute(JobExecutionContext jec)
            throws JobExecutionException {
        CheckAboutToDeactiveAccounts();
    }

    private void CheckAboutToDeactiveAccounts() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            SmsSenderDaoImpl smsSenderDaoImpl = new SmsSenderDaoImpl();
            List aboutToDeactivateList = smsSenderDaoImpl.getAboutToDeactiveSmsSenders();
            setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("ABOUT_TO_DEACTIVE_SMS")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(" ").append(PropertyFileReader.getValue("EXAMPLE_GET")).toString());
            if (aboutToDeactivateList != null) {
                if (aboutToDeactivateList.size() > 0) {
                    logger.info((new StringBuilder()).append("aboutToDeactivateList.size() is = ").append(aboutToDeactivateList.size()).toString());
                    for (int count = 0; count < aboutToDeactivateList.size(); count++) {
                        try {
                            aboutToDeactiveSmsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                            aboutToDeactiveSmsSender.sendMessage(getResponceMsg(), new String[]{
                                        ((SmsSender) aboutToDeactivateList.get(count)).getAddress().toString()
                                    });
                            continue;
                        } catch (MchoiceAventuraMessagingException ex) {
                            java.util.logging.Logger.getLogger(ScienceAlert.class.getName()).log(Level.SEVERE, null, ex);
                            continue;
                        } catch (Exception ex) {
                            logger.error(ex);
                            ex.printStackTrace();
                        }
                    }
                    logger.info((new StringBuilder()).append("Daily About To Deactivate Sending Success AT ").append(dateFormat.format(date)).toString());
                } else {
                    logger.info("aboutToDeactivateList.size() is 0");
                }
            } else {
                logger.info("aboutToDeactivateList.size() is null");
            }
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    public String getResponceMsg() {
        return responceMsg;
    }

    public void setResponceMsg(String responceMsg) {
        this.responceMsg = responceMsg;
    }
}
