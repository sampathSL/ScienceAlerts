package com.appzone.sciencealerts.main;

import com.appzone.sciencealerts.properties.PropertyFileReader;
import com.appzone.sciencealerts.service.*;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsMessage;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsMoServlet;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Initializing the Science Alerts SMS Application
 * @author SAM
 */
public class ScienceAlertsMain extends MchoiceAventuraSmsMoServlet {

    private String content;
    private String address;
    private static final Logger logger = Logger.getLogger(ScienceAlertsMain.class);

    public ScienceAlertsMain() {
        content = "";
        address = "";
    }

    /**
     * Initializing method for capturing the input message
     * @param msg
     */
    @Override
    protected void onMessage(MchoiceAventuraSmsMessage msg) {
        try {
            content = msg.getMessage();
            address = msg.getAddress();
            content = content.replaceAll("[^\\s\\w:./,!-]+", ""); // filtering the income sms content
            String trimContant = content.trim();
            SaveIncomeData(address, trimContant);
            String split[] = content.split("\\s+");
            if (split.length > 1) { 
                if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("KEY_WORD").trim())) { // separating the key words
                    if (split[0].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("REG_CODE").trim())) { // register to the app
                        RegUser regUser = new RegUser();
                        regUser.ScienceAlertReg(address);
                    } else if (split[0].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("UNREG_CODE").trim())) { // unregister from the app
                        UnRegUser unRegUser = new UnRegUser();
                        unRegUser.ScienceAlertUnReg(address);
                    } else {
                        InvalidMessage invalidMessage = new InvalidMessage();
                        invalidMessage.InformInvalideMessage(address);
                    }
                } else if (split[0].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("KEY_WORD").trim())) {
                    if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("SET_ACC_USERNAME").trim())) { // user set account name
                        AccountName accountUserName = new AccountName();
                        accountUserName.AccountUserName(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("GET_SCIENCE_ALERT").trim())) { // get a science alert
                        ScienceAlert scienceAlert = new ScienceAlert();
                        scienceAlert.SendScienceAlert(address);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("GET_ACC_INFO").trim())) { // get a account information
                        AccountInfo accountInfo = new AccountInfo();
                        accountInfo.GetAccInfo(address);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("DAILY_ALERTS_ACT_CODE").trim())) {
                        ActSAShedule actSAShedule = new ActSAShedule();
                        actSAShedule.SetActScienceAlertsSchedule(address);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("DAILY_ALERTS_DEACT_CODE").trim())) {
                        DeActSASchedule deActSASchedule = new DeActSASchedule();
                        deActSASchedule.SetDActScienceAlertsSchedule(address);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("HELP").trim())) {
                        ScienceAlertsHelp scienceAlertsHelp = new ScienceAlertsHelp();
                        scienceAlertsHelp.GetHelp(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("SEND_A_COMMENTS").trim())) {
                        ScienceAlertsComments scienceAlertsComments = new ScienceAlertsComments();
                        scienceAlertsComments.SendAppComment(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("REPLY_COMMENT").trim())) {
                        ScienceAlertsReplyComment scienceAlertsReplyComment = new ScienceAlertsReplyComment();
                        scienceAlertsReplyComment.Reply(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("SCIENCE_ALERTS_AD1").trim())) {
                        ScienceAlertsAdvert1 acienceAlertsAdvert1 = new ScienceAlertsAdvert1();
                        acienceAlertsAdvert1.SendAdvert1(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("SCIENCE_ALERTS_AD2").trim())) {
                        ScienceAlertsAdvert2 acienceAlertsAdvert2 = new ScienceAlertsAdvert2();
                        acienceAlertsAdvert2.SendAdvert2(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("SCIENCE_ALERTS_SENDER_REPLY").trim())) {
                        ScienceAlertsSenderInform senderInform = new ScienceAlertsSenderInform();
                        senderInform.Inform(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("RANK").trim())) {
                        AccountRank senderAccRank = new AccountRank();
                        senderAccRank.GetAccRank(address);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("DATA_FEEDS").trim())) {
                        ScienceAlertsDataFeeds dataFeeds = new ScienceAlertsDataFeeds();
                        dataFeeds.UploadDataFeeds(address, split);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("DAILY_SEND").trim())) {
                        DailySendData dailySend = new DailySendData();
                        dailySend.SendData(address);
                    } else if (split[1].trim().toLowerCase().equalsIgnoreCase(PropertyFileReader.getValue("ADMIN_GET").trim())) {
                        ScienceAlertsAdminGet scienceAlertsAdminGet = new ScienceAlertsAdminGet();
                        scienceAlertsAdminGet.getAdminScienceAlert(address, split);
                    } else {
                        InvalidMessage invalidMessage = new InvalidMessage();
                        invalidMessage.InformInvalideMessage(address);
                    }
                } else {
                    InvalidMessage invalidMessage = new InvalidMessage();
                    invalidMessage.InformInvalideMessage(address);
                }
            } else {
                InvalidMessage invalidMessage = new InvalidMessage();
                invalidMessage.InformInvalideMessage(address);
            }
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    
    /**
     *
     * @param address
     * @param content
     */
    private void SaveIncomeData(String address, String content) {
        try {
            IncomeSmsData incomeSmsTransaction = new IncomeSmsData();
            Date date = new Date();
            incomeSmsTransaction.CollectIncomeData(address, content, date);
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }
}
