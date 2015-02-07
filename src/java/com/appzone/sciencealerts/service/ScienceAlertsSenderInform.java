package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms
public class ScienceAlertsSenderInform {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsSenderInform.class);
    FinalSms transaction;

    public ScienceAlertsSenderInform() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void Inform(String address, String split[]) {
        Date date = new Date();
        try {
            if (split.length > 3) {
                if (address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS"))) {
                    SmsSender receiver = smsSenderDAOImpl.findBySmsSenderId(Long.parseLong(split[2].toString()));
                    if (receiver != null && receiver.getAddress() != null) {
                        String scaInformStr = "";
                        for (int count = 3; count < split.length; count++) {
                            scaInformStr = (new StringBuilder()).append(scaInformStr).append(" ").append(split[count].toString()).toString();
                        }

                        scaInformStr = scaInformStr.trim();
                        scaInformStr = scaInformStr.replace("null", "");
                        if (scaInformStr.length() < 160) {
                            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                            setResponceMsg(scaInformStr);
                            smsSender.sendMessage(getResponceMsg(), new String[]{
                                        receiver.getAddress()
                                    });
                            transaction.CollectData(receiver, getResponceMsg(), date);
                        } else {
                            logger.error((new StringBuilder()).append("Inform Sms is too long ==============").append(date).toString());
                        }
                    } else {
                        logger.error((new StringBuilder()).append("receiver is null ====================================").append(date).toString());
                    }
                } else {
                    logger.error((new StringBuilder()).append("Address doesn't match ========Security Alert!!!=======").append(date).toString());
                }
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
