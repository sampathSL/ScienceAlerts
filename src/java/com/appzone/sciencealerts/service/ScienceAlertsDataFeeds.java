package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import com.appzone.sciencealerts.scheduletasks.DataFeedScheduleTask;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms, InvalidMessage
public class ScienceAlertsDataFeeds {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsDataFeeds.class);
    FinalSms transaction;

    public ScienceAlertsDataFeeds() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void UploadDataFeeds(String address, String split[]) {
        Date date = new Date();
        try {
            if (split.length > 2) {
                if (address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS"))) {
                    smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                    if (split[2].trim().toString().equalsIgnoreCase("1")) {
                        DataFeedScheduleTask task1 = new DataFeedScheduleTask();
                        task1.FindAlerts(PropertyFileReader.getValue("URL1"));
                        task1.FindAlerts(PropertyFileReader.getValue("URL2"));
                        setResponceMsg("Upload Success");
                        smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        transaction.NotAMember(address, getResponceMsg(), date);
                    } else if (split[2].trim().toString().equalsIgnoreCase("2")) {
                        DataFeedScheduleTask task2 = new DataFeedScheduleTask();
                        task2.FindAlerts(PropertyFileReader.getValue("URL3"));
                        task2.FindAlerts(PropertyFileReader.getValue("URL4"));
                        setResponceMsg("Upload Success");
                        smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        transaction.NotAMember(address, getResponceMsg(), date);
                    } else if (split[2].trim().toString().equalsIgnoreCase("3")) {
                        DataFeedScheduleTask task3 = new DataFeedScheduleTask();
                        task3.FindAlerts(PropertyFileReader.getValue("URL5"));
                        task3.FindAlerts(PropertyFileReader.getValue("URL6"));
                        setResponceMsg("Upload Success");
                        smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        transaction.NotAMember(address, getResponceMsg(), date);
                    } else if (split[2].trim().toString().equalsIgnoreCase("4")) {
                        DataFeedScheduleTask task4 = new DataFeedScheduleTask();
                        task4.FindAlerts(PropertyFileReader.getValue("URL7"));
                        task4.FindAlerts(PropertyFileReader.getValue("URL8"));
                        setResponceMsg("Upload Success");
                        smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        transaction.NotAMember(address, getResponceMsg(), date);
                    } else if (split[2].trim().toString().equalsIgnoreCase("5")) {
                        DataFeedScheduleTask task5 = new DataFeedScheduleTask();
                        task5.FindAlerts(PropertyFileReader.getValue("URL9"));
                        task5.FindAlerts(PropertyFileReader.getValue("URL10"));
                        task5.FindAlerts(PropertyFileReader.getValue("URL11"));
                        setResponceMsg("Upload Success");
                        smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        transaction.NotAMember(address, getResponceMsg(), date);
                    } else {
                        InvalidMessage invalidMessage = new InvalidMessage();
                        invalidMessage.InformInvalideMessage(address);
                    }
                } else {
                    logger.error((new StringBuilder()).append("Address doesn't match ========Security Alert!!!=======").append(date).toString());
                }
            } else {
                logger.error((new StringBuilder()).append("Incorrect command ========Security Alert!!!=======").append(date).toString());
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
    }

    public String getResponceMsg() {
        return responceMsg;
    }

    public void setResponceMsg(String responceMsg) {
        this.responceMsg = responceMsg;
    }
}
