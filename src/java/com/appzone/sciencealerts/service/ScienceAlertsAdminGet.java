package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.ScienceAlertsDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.ScienceAlerts;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

public class ScienceAlertsAdminGet {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsAdminGet.class);

    public ScienceAlertsAdminGet() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
    }

    public void getAdminScienceAlert(String address, String adminAlert[]) {
        try {
            if (adminAlert.length > 2 && adminAlert.length < 4) {
                Date date = new Date();
                if (address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS")) || address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS2"))) {
                    try {
                        if (adminAlert[2] != null) {
                            ScienceAlertsDaoImpl scienceAlertsDaoImpl = new ScienceAlertsDaoImpl();
                            ScienceAlerts scienceAlerts = scienceAlertsDaoImpl.GetAdminScienceAlert(Long.parseLong(adminAlert[2].toString()));
                            setResponceMsg(scienceAlerts.getSms());
                            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                            smsSender.sendMessage(getResponceMsg(), new String[]{address});
                        } else {
                            logger.error("null number");
                        }
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
