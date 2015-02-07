package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.HelpDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.Help;
import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraResponse;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms, ScienceAlert
public class ActSAShedule {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ActSAShedule.class);
    FinalSms transaction;

    public ActSAShedule() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void SetActScienceAlertsSchedule(String address) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderActSchedule = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderActSchedule != null) {
                if (smsSenderActSchedule.getIsReg().equalsIgnoreCase("Y")) {
                    if (smsSenderActSchedule.getIsActive().equalsIgnoreCase("Y")) {
                        if (smsSenderActSchedule.getIsSchedularActive().equalsIgnoreCase("Y")) {
                            smsSenderActSchedule.setLastActiveTime(date);
                            smsSenderDAOImpl.update(smsSenderActSchedule);
                            setResponceMsg(PropertyFileReader.getValue("ALREADY_ACT_SCHEDULE"));
                            MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                        address
                                    });
                            if (!response.isSuccess()) {
                                Help help = new Help();
                                help.setHelpText(smsSenderActSchedule.getId().toString());
                                HelpDaoImpl helpDao = new HelpDaoImpl();
                                helpDao.save(help);
                            }
                            transaction.CollectData(smsSenderActSchedule, getResponceMsg(), date);
                        } else {
                            smsSenderActSchedule.setIsSchedularActive("Y");
                            smsSenderActSchedule.setLastActiveTime(date);
                            smsSenderDAOImpl.update(smsSenderActSchedule);
                            setResponceMsg(PropertyFileReader.getValue("ACT_SCHEDULE"));
                            MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                        address
                                    });
                            if (!response.isSuccess()) {
                                Help help = new Help();
                                help.setHelpText(smsSenderActSchedule.getId().toString());
                                HelpDaoImpl helpDao = new HelpDaoImpl();
                                helpDao.save(help);
                            }
                            transaction.CollectData(smsSenderActSchedule, getResponceMsg(), date);
                        }
                    } else {
                        smsSenderActSchedule.setLastActiveTime(date);
                        smsSenderDAOImpl.update(smsSenderActSchedule);
                        setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("NOT_AN_ACTIVE_ACCOUNT")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").toString());
                        MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        if (!response.isSuccess()) {
                            Help help = new Help();
                            help.setHelpText(smsSenderActSchedule.getId().toString());
                            HelpDaoImpl helpDao = new HelpDaoImpl();
                            helpDao.save(help);
                        }
                        transaction.CollectData(smsSenderActSchedule, getResponceMsg(), date);
                    }
                } else {
                    smsSenderActSchedule.setUserName("UNKNOWN");
                    smsSenderActSchedule.setIsReg("Y");
                    smsSenderActSchedule.setIsActive("Y");
                    smsSenderActSchedule.setMarks(Long.valueOf(Long.parseLong("2")));
                    smsSenderActSchedule.setIsSchedularActive("Y");
                    smsSenderActSchedule.setJoinedDate(date);
                    smsSenderActSchedule.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderActSchedule);
                    setResponceMsg(PropertyFileReader.getValue("REG_ERROR"));
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderActSchedule.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderActSchedule, getResponceMsg(), date);
                }
            } else {
                SmsSender sender = new SmsSender();
                sender.setAddress(address.toString());
                sender.setUserName("UNKNOWN");
                sender.setIsReg("Y");
                sender.setIsActive("Y");
                sender.setMarks(Long.valueOf(Long.parseLong("2")));
                sender.setIsSchedularActive("Y");
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
        } catch (MchoiceAventuraMessagingException ex) {
            Logger.getLogger(ScienceAlert.class.getName()).log(Level.SEVERE, null, ex);
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
