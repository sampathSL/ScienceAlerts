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
//            FinalSms, ScienceAlert, ActSAShedule
public class DeActSASchedule {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ActSAShedule.class);
    FinalSms transaction;

    public DeActSASchedule() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void SetDActScienceAlertsSchedule(String address) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderDactSchedule = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderDactSchedule != null) {
                if (smsSenderDactSchedule.getIsReg().equalsIgnoreCase("Y")) {
                    if (smsSenderDactSchedule.getIsSchedularActive().equalsIgnoreCase("N")) {
                        smsSenderDactSchedule.setLastActiveTime(date);
                        smsSenderDAOImpl.update(smsSenderDactSchedule);
                        setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("ALREADY_DEACT_SCHEDULE")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_ACT_SCHEDULE")).toString());
                        MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        if (!response.isSuccess()) {
                            Help help = new Help();
                            help.setHelpText(smsSenderDactSchedule.getId().toString());
                            HelpDaoImpl helpDao = new HelpDaoImpl();
                            helpDao.save(help);
                        }
                        transaction.CollectData(smsSenderDactSchedule, getResponceMsg(), date);
                    } else {
                        smsSenderDactSchedule.setIsSchedularActive("N");
                        smsSenderDactSchedule.setLastActiveTime(date);
                        smsSenderDAOImpl.update(smsSenderDactSchedule);
                        setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("DEACT_SCHEDULE")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_ACT_SCHEDULE")).toString());
                        MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        if (!response.isSuccess()) {
                            Help help = new Help();
                            help.setHelpText(smsSenderDactSchedule.getId().toString());
                            HelpDaoImpl helpDao = new HelpDaoImpl();
                            helpDao.save(help);
                        }
                        transaction.CollectData(smsSenderDactSchedule, getResponceMsg(), date);
                    }
                } else {
                    smsSenderDactSchedule.setUserName("UNKNOWN");
                    smsSenderDactSchedule.setIsReg("Y");
                    smsSenderDactSchedule.setIsActive("Y");
                    smsSenderDactSchedule.setMarks(Long.valueOf(Long.parseLong("3")));
                    smsSenderDactSchedule.setIsSchedularActive("Y");
                    smsSenderDactSchedule.setJoinedDate(date);
                    smsSenderDactSchedule.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderDactSchedule);
                    smsSenderDactSchedule.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderDactSchedule);
                    setResponceMsg(PropertyFileReader.getValue("REG_ERROR"));
                    ScienceAlertsHelp helpM = new ScienceAlertsHelp();
                    helpM.GetHelp(address, "SCA HELP".split("\\s+"));
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderDactSchedule.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderDactSchedule, getResponceMsg(), date);
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
                setResponceMsg(PropertyFileReader.getValue("REG_ERROR"));
                ScienceAlertsHelp helpM = new ScienceAlertsHelp();
                helpM.GetHelp(address, "SCA HELP".split("\\s+"));
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
