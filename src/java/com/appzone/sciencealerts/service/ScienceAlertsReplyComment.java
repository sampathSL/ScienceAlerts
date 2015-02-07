package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.AppCommentsDaoImpl;
import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.AppComments;
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
public class ScienceAlertsReplyComment {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    AppCommentsDaoImpl appCommentsDaoImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsReplyComment.class);
    FinalSms transaction;

    public ScienceAlertsReplyComment() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        appCommentsDaoImpl = new AppCommentsDaoImpl();
        transaction = new FinalSms();
    }

    public void Reply(String address, String replyCmt[]) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            if (replyCmt.length > 3) {
                Long recipientId = Long.valueOf(Long.parseLong(replyCmt[2].toString().trim()));
                Long commentId = Long.valueOf(Long.parseLong(replyCmt[3].toString().trim()));
                SmsSender recipient = smsSenderDAOImpl.findBySmsSenderId(recipientId.longValue());
                AppComments appcomment = appCommentsDaoImpl.findByAppCommentId(commentId.longValue());
                if (recipient != null && recipient.getAddress() != null && appcomment != null && appcomment.getId().longValue() > 0L && appcomment.getIsChecked().equalsIgnoreCase("N")) {
                    if (address.equalsIgnoreCase(PropertyFileReader.getValue("MY_ADDRESS"))) {
                        String replyStr = "";
                        for (int count = 4; count < replyCmt.length; count++) {
                            replyStr = (new StringBuilder()).append(replyStr).append(" ").append(replyCmt[count].toString()).toString();
                        }

                        replyStr = replyStr.trim();
                        replyStr = replyStr.replace("null", "");
                        String finalReply = (new StringBuilder()).append("Dear ").append(recipient.getUserName()).append(",thank you for using ScienceAlerts.").append(replyStr).toString();
                        if (finalReply.length() < 160) {
                            setResponceMsg(finalReply);
                            smsSender.sendMessage(getResponceMsg(), new String[]{recipient.getAddress()});
                            transaction.CollectData(recipient, getResponceMsg(), date);
                            appcomment.setIsChecked("Y");
                            appCommentsDaoImpl.update(appcomment);
                        } else {
                            finalReply = finalReply.substring(0, 159);
                            setResponceMsg(finalReply);
                            smsSender.sendMessage(getResponceMsg(), new String[]{recipient.getAddress()});
                            transaction.CollectData(recipient, getResponceMsg(), date);
                            appcomment.setIsChecked("Y");
                            appCommentsDaoImpl.update(appcomment);
                        }
                    } else {
                        logger.error((new StringBuilder()).append("Admin Address Doesn't Match !!!!  ").append(date).toString());
                    }
                } else {
                    logger.error((new StringBuilder()).append("App Comment Reply Failed !!!!  ").append(date).toString());
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
