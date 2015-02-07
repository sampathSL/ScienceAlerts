package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.HelpDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.Help;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import com.appzone.sciencealerts.scheduletasks.SendDailyAlerts;
import hsenidmobile.sdp.rest.servletbase.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms
public class DailySendData {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(DailySendData.class);
    FinalSms transaction;

    public DailySendData() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void SendData(String address) {
        Date date = new Date();
        try {
            if (address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS"))) {
                smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                SendDailyAlerts dailyAlerts = new SendDailyAlerts();
                dailyAlerts.execute();
                setResponceMsg("Data Sent Success");
                MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{address});
                if (!response.isSuccess()) {
                    Help help = new Help();
                    help.setHelpText(address);
                    HelpDaoImpl helpDao = new HelpDaoImpl();
                    helpDao.save(help);
                }
                transaction.NotAMember(address, getResponceMsg(), date);
            } else {
                logger.error((new StringBuilder()).append("Address doesn't match ========Security Alert!!!=======").append(date).toString());
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
