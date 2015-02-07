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
public class AccountRank {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(AccountRank.class);
    FinalSms transaction;
    SimpleDateFormat dateformat;
    Date date;

    public AccountRank() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
        dateformat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        date = new Date();
    }

    public void GetAccRank(String address) {
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderGetAccInfo = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderGetAccInfo != null) {
                if (smsSenderGetAccInfo.getIsReg().equalsIgnoreCase("Y")) {
                    smsSenderGetAccInfo.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderGetAccInfo);
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("RANK_INFO")).append(" ").append(smsSenderDAOImpl.getSmsSenderRank(smsSenderGetAccInfo.getId())).append(PropertyFileReader.getValue("RANK_INFO_MORE")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(" ").append(PropertyFileReader.getValue("RANK_INFO_RETRIC")).toString());
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
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("RANK_INFO")).append(" ").append(smsSenderDAOImpl.getSmsSenderRank(smsSenderGetAccInfo.getId())).append(PropertyFileReader.getValue("RANK_INFO_MORE")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(" ").append(PropertyFileReader.getValue("RANK_INFO_RETRIC")).toString());
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
                smsSenderDAOImpl.update(sender);
                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("RANK_INFO")).append(" ").append(smsSenderDAOImpl.getSmsSenderRank(sender.getId())).append(PropertyFileReader.getValue("RANK_INFO_MORE")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(" ").append(PropertyFileReader.getValue("RANK_INFO_RETRIC")).toString());
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
