/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.scheduletasks;

import com.appzone.sciencealerts.hibernate.dao.impl.ScienceAlertsDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.ScienceAlerts;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author SAM
 */
public class DailyAlertsScheduleTask implements Job{
    
    private MchoiceAventuraSmsSender dailySmsSender;
    private String responceMsg = "";
    private static final Logger logger = Logger.getLogger(DailyAlertsScheduleTask.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        SendDailyAlerts();
    }
    
    private void SendDailyAlerts() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            SmsSenderDaoImpl smsSenderDaoImpl = new SmsSenderDaoImpl();
            ScienceAlertsDaoImpl scienceAlertsDaoImpl = new ScienceAlertsDaoImpl();
            List dailyList = smsSenderDaoImpl.getDailyAlertsSmsSenders();
            ScienceAlerts dailyScienceAlerts = scienceAlertsDaoImpl.GetDailyScienceAlert();
            if (dailyList != null) {
                if (dailyList.size() > 0) {
                    if (dailyScienceAlerts != null && dailyScienceAlerts.getSms() != null) {
                        try {
                            logger.info((new StringBuilder()).append("senderList.size() is ========== ").append(dailyList.size()).toString());
                            
                            /*for (int count = 0; count < dailyList.size(); count++) {
                                try {
                                    dailySmsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                                    setResponceMsg(dailyScienceAlerts.getSms().toString());
                                    dailySmsSender.sendMessage(getResponceMsg(),new String[]{((SmsSender) dailyList.get(count)).getAddress()});
                                } catch (MchoiceAventuraMessagingException ex) {
                                    logger.error(ex);
                                } catch (Exception ex) {
                                    logger.error(ex);
                                    ex.printStackTrace();
                                }
                            }*/
                            
                            dailySmsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                            setResponceMsg(dailyScienceAlerts.getSms().toString());
                            dailySmsSender.broadcastMessage(getResponceMsg());
                            logger.info((new StringBuilder()).append("Daily SMS Sending Success AT ========== ").append(dateFormat.format(date)).toString());
                            dailyScienceAlerts.setScheduled("Y");
                            scienceAlertsDaoImpl.update(dailyScienceAlerts);
                            
                        }catch(MchoiceAventuraMessagingException ex){
                            logger.error(ex);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            logger.error(ex);
                            ex.printStackTrace();
                        }
                    } else {
                        logger.info("dailyScienceAlerts.getSms() is null");
                    }
                } else {
                    logger.info("dailyList.size() is 0");
                }
            } else {
                logger.info("dailyList.size() is null");
            }
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    /**
     * @return the responceMsg
     */
    public String getResponceMsg() {
        return responceMsg;
    }

    /**
     * @param responceMsg the responceMsg to set
     */
    public void setResponceMsg(String responceMsg) {
        this.responceMsg = responceMsg;
    }
    
}
