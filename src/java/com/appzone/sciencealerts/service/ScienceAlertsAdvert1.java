package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

public class ScienceAlertsAdvert1 {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsAdvert1.class);

    public ScienceAlertsAdvert1() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
    }

    public void SendAdvert1(String address, String scaAdd[]) {
        Date date = new Date();
        try {
            if (scaAdd.length > 3) {
                if (address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS"))) {
                    try {
                        String scaAddStr = "";
                        for (int count = 2; count < scaAdd.length; count++) {
                            scaAddStr = (new StringBuilder()).append(scaAddStr).append(" ").append(scaAdd[count].toString()).toString();
                        }

                        scaAddStr = scaAddStr.trim();
                        scaAddStr = scaAddStr.replace("null", "");
                        smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                        setResponceMsg(scaAddStr);
                        logger.info("BROADCAT MSG START");
                        smsSender.broadcastMessage(getResponceMsg());
                        logger.info("BROADCAT MSG SUCCESS");
                    } catch (MalformedURLException e) {
                        logger.error(e);
                        e.printStackTrace();
                    } catch (MchoiceAventuraMessagingException e) {
                        logger.error(e);
                        e.printStackTrace();
                    } catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                    }
                } else {
                    logger.error((new StringBuilder()).append("Address doesn't match ========Security Alert!!!=======").append(date).toString());
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public String getResponceMsg() {
        return responceMsg;
    }

    public void setResponceMsg(String responceMsg) {
        this.responceMsg = responceMsg;
    }
}
