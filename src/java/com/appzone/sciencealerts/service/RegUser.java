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
public class RegUser {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(RegUser.class);
    FinalSms transaction;

    public RegUser() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void ScienceAlertReg(String address) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderReg = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderReg != null) {
                if (smsSenderReg.getIsReg().equalsIgnoreCase("Y")) {
                    smsSenderReg.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderReg);
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("REG_ALREADY")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").toString());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{address});
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderReg.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    
                    ScienceAlertsHelp help = new ScienceAlertsHelp();
                    help.GetHelp(address, "SCA HELP".split("\\s+"));
                    
                    transaction.CollectData(smsSenderReg, getResponceMsg(), date);
                } else {
                    smsSenderReg.setIsReg("Y");
                    smsSenderReg.setIsActive("Y");
                    smsSenderReg.setIsSchedularActive("Y");
                    smsSenderReg.setJoinedDate(date);
                    smsSenderReg.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderReg);
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("REG_SUCCESS_PART1_ACT1")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").toString());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{address});
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderReg.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    
                    ScienceAlertsHelp help = new ScienceAlertsHelp();
                    help.GetHelp(address, "SCA HELP".split("\\s+"));
                    
                    transaction.CollectData(smsSenderReg, getResponceMsg(), date);
                }
            } else {
                SmsSender sender = new SmsSender();
                sender.setAddress(address.toString());
                sender.setUserName("UNKNOWN");
                sender.setIsReg("Y");
                sender.setIsActive("Y");
                sender.setMarks(Long.valueOf(Long.parseLong("3")));
                sender.setIsSchedularActive("Y");
                sender.setJoinedDate(date);
                sender.setLastActiveTime(date);
                smsSenderDAOImpl.save(sender);
                if (sender.getId().longValue() != 0L) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("USER_NAME_SET_SUCCESS_SUB3")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_GET")).toString());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{address});
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(sender.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }

                    ScienceAlertsHelp help = new ScienceAlertsHelp();
                    help.GetHelp(address, "SCA HELP".split("\\s+"));

                    transaction.CollectData(sender, getResponceMsg(), date);
                } else {
                    setResponceMsg(PropertyFileReader.getValue("REG_ERROR"));
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{address});
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    
                    transaction.NotAMember(address, getResponceMsg(), date);
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

    private String getResponceMsg() {
        return responceMsg;
    }

    private void setResponceMsg(String responceMsg) {
        this.responceMsg = responceMsg;
    }
}
