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

public class DailyDeactiveCustomers
        implements Job {

    private String responceMsg;
    private MchoiceAventuraSmsSender deactiveSmsSender;
    private static final Logger logger = Logger.getLogger(DailyDeactiveCustomers.class);

    public DailyDeactiveCustomers() {
        responceMsg = "";
    }

    @Override
    public void execute(JobExecutionContext jec)
            throws JobExecutionException {
        DeactiveDailyAlerts();
    }

    private void DeactiveDailyAlerts() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            SmsSenderDaoImpl smsSenderDaoImpl = new SmsSenderDaoImpl();
            List deactivateList = smsSenderDaoImpl.getDailyDeactiveSmsSenders();
            setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("DAILY_DEACTIVE_SMS")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(" ").append(PropertyFileReader.getValue("EXAMPLE_ACT_SCHEDULE")).toString());
            if (deactivateList != null) {
                if (deactivateList.size() > 0) {
                    logger.info((new StringBuilder()).append("aboutToDeactivateList.size() is = ").append(deactivateList.size()).toString());
                    for (int count = 0; count < deactivateList.size(); count++) {
                        try {
                            deactiveSmsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                            deactiveSmsSender.sendMessage(getResponceMsg(), new String[]{((SmsSender) deactivateList.get(count)).getAddress().toString()});
                            SmsSenderDaoImpl smsSenderDaoImpl1 = new SmsSenderDaoImpl();
                            SmsSender smsSender = (SmsSender) deactivateList.get(count);
                            smsSender.setIsSchedularActive("N");
                            smsSender.setLastActiveTime(date);
                            smsSenderDaoImpl1.update(smsSender);
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
                    logger.info("DeactivateList.size() is 0");
                }
            } else {
                logger.info("DeactivateList.size() is null");
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
