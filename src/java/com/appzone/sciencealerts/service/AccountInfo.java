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
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms
public class AccountInfo {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(AccountInfo.class);
    FinalSms transaction;
    SimpleDateFormat dateformat;

    public AccountInfo() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
        dateformat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    }

    public void GetAccInfo(String address) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderGetAccInfo = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderGetAccInfo != null) {
                if (smsSenderGetAccInfo.getIsReg().equalsIgnoreCase("Y")) {
                    String daily_alerts = "";
                    if (smsSenderGetAccInfo.getIsSchedularActive().equalsIgnoreCase("Y")) {
                        daily_alerts = "active.";
                    } else {
                        daily_alerts = "not active.";
                    }
                    smsSenderGetAccInfo.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderGetAccInfo);
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("ACCOUNT_INFO_PART1_USER_NAME")).append(" ").append(smsSenderGetAccInfo.getUserName()).append(PropertyFileReader.getValue("ACCOUNT_INFO_PART2_MARKS")).append(" ").append(smsSenderGetAccInfo.getMarks()).append(" ").append(PropertyFileReader.getValue("ACCOUNT_INFO_PART3_JOINED_DATE")).append(dateformat.format(smsSenderGetAccInfo.getJoinedDate())).append(" ").append(PropertyFileReader.getValue("ACCOUNT_INFO_PART4_DAILY_ALERTS")).append(" ").append(daily_alerts).toString());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderGetAccInfo.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderGetAccInfo, getResponceMsg(), date);

                } else {
                    smsSenderGetAccInfo.setUserName("UNKNOWN");
                    smsSenderGetAccInfo.setIsReg("Y");
                    smsSenderGetAccInfo.setIsActive("Y");
                    smsSenderGetAccInfo.setMarks(Long.valueOf(Long.parseLong("2")));
                    smsSenderGetAccInfo.setIsSchedularActive("Y");
                    smsSenderGetAccInfo.setJoinedDate(date);
                    smsSenderGetAccInfo.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderGetAccInfo);
                    String daily_alerts = "active.";
                    smsSenderGetAccInfo.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderGetAccInfo);
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("ACCOUNT_INFO_PART1_USER_NAME")).append(" ").append(smsSenderGetAccInfo.getUserName()).append(PropertyFileReader.getValue("ACCOUNT_INFO_PART2_MARKS")).append(" ").append(smsSenderGetAccInfo.getMarks()).append(" ").append(PropertyFileReader.getValue("ACCOUNT_INFO_PART3_JOINED_DATE")).append(dateformat.format(smsSenderGetAccInfo.getJoinedDate())).append(" ").append(PropertyFileReader.getValue("ACCOUNT_INFO_PART4_DAILY_ALERTS")).append(" ").append(daily_alerts).toString());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderGetAccInfo.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderGetAccInfo, getResponceMsg(), date);
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
                String daily_alerts = "active.";
                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("ACCOUNT_INFO_PART1_USER_NAME")).append(" ").append(sender.getUserName()).append(PropertyFileReader.getValue("ACCOUNT_INFO_PART2_MARKS")).append(" ").append(sender.getMarks()).append(" ").append(PropertyFileReader.getValue("ACCOUNT_INFO_PART3_JOINED_DATE")).append(dateformat.format(sender.getJoinedDate())).append(" ").append(PropertyFileReader.getValue("ACCOUNT_INFO_PART4_DAILY_ALERTS")).append(" ").append(daily_alerts).toString());
                MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                            address
                        });
                if (!response.isSuccess()) {
                    Help help = new Help();
                    help.setHelpText(sender.getId().toString());
                    HelpDaoImpl helpDao = new HelpDaoImpl();
                    helpDao.save(help);
                }
                transaction.CollectData(sender, getResponceMsg(), date);
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
