package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.ScienceAlertsDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.ScienceAlerts;
import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            ScienceAlert
public class ScienceAlertsAdvert2 {

    private MchoiceAventuraSmsSender dailySmsSender;
    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsAdvert2.class);

    public ScienceAlertsAdvert2() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
    }

    public void SendAdvert2(String address, String scaAdd[]) {
        Date date = new Date();
        try {
            if (scaAdd.length == 3) {
                if (address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS"))) {
                    try {
                        if (scaAdd[2] != null) {
                            ScienceAlertsDaoImpl scienceAlertsDaoImpl = new ScienceAlertsDaoImpl();
                            ScienceAlerts scienceAlerts = scienceAlertsDaoImpl.GetAdminScienceAlert(Long.parseLong(scaAdd[2].toString()));
                            if (scienceAlerts.getSms() != null) {
                                SmsSenderDaoImpl smsSenderDaoImpl = new SmsSenderDaoImpl();
                                List dailyList = smsSenderDaoImpl.getDailyAlertsSmsSenders();
                                if (dailyList != null) {
                                    if (dailyList.size() > 0) {
                                        try {
                                            logger.info((new StringBuilder()).append("senderList.size() is ========== ").append(dailyList.size()).toString());
                                            for (int count = 0; count < dailyList.size(); count++) {
                                                try {
                                                    dailySmsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                                                    setResponceMsg(scienceAlerts.getSms().toString().trim());
                                                    dailySmsSender.sendMessage(getResponceMsg(), new String[]{
                                                                ((SmsSender) dailyList.get(count)).getAddress()
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

                                            scienceAlerts.setScheduled("Y");
                                            scienceAlertsDaoImpl.update(scienceAlerts);
                                        } catch (Exception ex) {
                                            logger.error(ex);
                                            ex.printStackTrace();
                                        }
                                    } else {
                                        logger.info("dailyList.size() is 0");
                                    }
                                } else {
                                    logger.info("dailyList.size() is null");
                                }
                            } else {
                                logger.info("Science Alerts SMS is null");
                            }
                        } else {
                            logger.info("Science alerts Id is null");
                        }
                    } catch (Exception ex) {
                        logger.error(ex);
                        ex.printStackTrace();
                    }
                } else {
                    logger.error((new StringBuilder()).append("Address doesn't match ========Security Alert!!!=======").append(date).toString());
                }
            } else {
                logger.error((new StringBuilder()).append("Parameters missing or invalide parameter range").append(date).toString());
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
