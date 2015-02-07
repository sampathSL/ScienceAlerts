package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.HelpDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.Help;
import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import hsenidmobile.sdp.rest.servletbase.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms
public class InvalidMessage {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(InvalidMessage.class);
    FinalSms transaction;

    public InvalidMessage() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void InformInvalideMessage(String address) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderInvalideSms = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderInvalideSms != null) {
                smsSenderInvalideSms.setLastActiveTime(date);
                smsSenderDAOImpl.update(smsSenderInvalideSms);
                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("INCORRECT_COMMAND")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_HELP")).toString());
                MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                            address
                        });
                if (!response.isSuccess()) {
                    Help help = new Help();
                    help.setHelpText(address);
                    HelpDaoImpl helpDao = new HelpDaoImpl();
                    helpDao.save(help);
                }
                transaction.CollectData(smsSenderInvalideSms, getResponceMsg(), date);
            } else {
                SmsSender sender = new SmsSender();
                sender.setAddress(address.toString());
                sender.setUserName("UNKNOWN");
                sender.setIsReg("Y");
                sender.setIsActive("Y");
                sender.setMarks(Long.valueOf(Long.parseLong("2")));
                sender.setIsSchedularActive("N");
                sender.setJoinedDate(date);
                sender.setLastActiveTime(date);
                smsSenderDAOImpl.save(sender);
                setResponceMsg(PropertyFileReader.getValue("REG_ERROR"));
                MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                            address
                        });
                if (!response.isSuccess()) {
                    Help help = new Help();
                    help.setHelpText(sender.getId().toString());
                    HelpDaoImpl helpDao = new HelpDaoImpl();
                    helpDao.save(help);
                }
                transaction.NotAMember(address, getResponceMsg(), date);
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
