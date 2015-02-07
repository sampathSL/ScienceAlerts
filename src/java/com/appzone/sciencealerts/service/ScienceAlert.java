package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.HelpDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.ScienceAlertsDaoImpl;
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
//            FinalSms
public class ScienceAlert {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    ScienceAlertsDaoImpl scienceAlertsSmsDaoImpl;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ScienceAlert.class);
    FinalSms transaction;

    public ScienceAlert() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        scienceAlertsSmsDaoImpl = new ScienceAlertsDaoImpl();
        transaction = new FinalSms();
    }

    public void SendScienceAlert(String address) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderGetAlert = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderGetAlert != null) {
                if (smsSenderGetAlert.getIsReg().equalsIgnoreCase("Y")) {
                    Long marks = smsSenderGetAlert.getMarks();
                    marks = Long.valueOf(marks.longValue() + 1L);
                    smsSenderGetAlert.setMarks(marks);
                    smsSenderGetAlert.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderGetAlert);
                    setResponceMsg(GetScienceAlert());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderGetAlert.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderGetAlert, getResponceMsg(), date);
                } else {
                    smsSenderGetAlert.setAddress(address.toString());
                    smsSenderGetAlert.setUserName("UNKNOWN");
                    smsSenderGetAlert.setIsReg("Y");
                    smsSenderGetAlert.setIsActive("Y");
                    smsSenderGetAlert.setMarks(Long.valueOf(Long.parseLong("3")));
                    smsSenderGetAlert.setIsSchedularActive("Y");
                    smsSenderGetAlert.setJoinedDate(date);
                    smsSenderGetAlert.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderGetAlert);
                    setResponceMsg(GetScienceAlert());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderGetAlert.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderGetAlert, getResponceMsg(), date);
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
                setResponceMsg(GetScienceAlert());
                MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{address});
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

    public String GetScienceAlert() {
        return scienceAlertsSmsDaoImpl.GenerateRandomAlert();
    }
}
