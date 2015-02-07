package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.*;
import com.appzone.sciencealerts.hibernate.entity.*;
import com.appzone.sciencealerts.properties.AppZoneConfig;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import hsenidmobile.sdp.rest.servletbase.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms
public class ScienceAlertsComments {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    AppCommentsDaoImpl appCommentsDaoImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsComments.class);
    FinalSms transaction;

    public ScienceAlertsComments() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        appCommentsDaoImpl = new AppCommentsDaoImpl();
        transaction = new FinalSms();
    }

    public void SendAppComment(String address, String setAppComment[]) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderGetAlert = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderGetAlert == null) {
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
            }
            if (setAppComment.length <= 2) {
                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("SEND_A_COMMENTS_NO_MESSAGE")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_COMMENT")).toString());
                smsSender.sendMessage(getResponceMsg(), new String[]{
                            address
                        });
                transaction.NotAMember(address, getResponceMsg(), date);
            } else {
                SmsSender smsSenderAppCommentsSender = smsSenderDAOImpl.findBySmsSenderAddress(address);
                String comment = "";
                for (int count = 2; count < setAppComment.length; count++) {
                    comment = (new StringBuilder()).append(comment).append(" ").append(setAppComment[count].trim().toString()).toString();
                }

                comment = comment.trim();
                if (smsSenderAppCommentsSender != null) {
                    if (smsSenderAppCommentsSender.getIsReg().equalsIgnoreCase("Y")) {
                        if (smsSenderAppCommentsSender.getIsActive().equalsIgnoreCase("Y")) {
                            Long marks = smsSenderAppCommentsSender.getMarks();
                            marks = Long.valueOf(marks.longValue() + 1L);
                            smsSenderAppCommentsSender.setMarks(marks);
                            smsSenderAppCommentsSender.setLastActiveTime(date);
                            smsSenderDAOImpl.update(smsSenderAppCommentsSender);
                            comment = comment.replace("null", "");
                            AppComments appComments = new AppComments();
                            appComments.setSmssender(smsSenderAppCommentsSender);
                            appComments.setIsChecked("N");
                            appComments.setComment(comment);
                            appCommentsDaoImpl.save(appComments);
                            setResponceMsg(PropertyFileReader.getValue("SEND_A_COMMENTS_THANK_YOU"));
                            MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                        address
                                    });
                            if (!response.isSuccess()) {
                                Help help = new Help();
                                help.setHelpText(smsSenderAppCommentsSender.getId().toString());
                                HelpDaoImpl helpDao = new HelpDaoImpl();
                                helpDao.save(help);
                            }
                            transaction.CollectData(smsSenderAppCommentsSender, getResponceMsg(), date);
                            Long senderId = smsSenderAppCommentsSender.getId();
                            Long commentId = appComments.getId();
                            String finalMsg = (new StringBuilder()).append(senderId.toString()).append("-").append(commentId.toString()).append("-").append(comment).toString();
                            if (finalMsg.length() < 160) {
                                smsSender.sendMessage(finalMsg, new String[]{
                                            PropertyFileReader.getValue("MY_ADDRESS")
                                        });
                            } else {
                                finalMsg = finalMsg.substring(0, 159);
                                smsSender.sendMessage(finalMsg, new String[]{
                                            PropertyFileReader.getValue("MY_ADDRESS")
                                        });
                            }
                        } else {
                            comment = comment.replace("null", "");
                            AppComments appComments = new AppComments();
                            appComments.setSmssender(smsSenderAppCommentsSender);
                            appComments.setIsChecked("N");
                            appComments.setComment(comment);
                            appCommentsDaoImpl.save(appComments);
                            setResponceMsg(PropertyFileReader.getValue("SEND_A_COMMENTS_THANK_YOU"));
                            MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                        address
                                    });
                            if (!response.isSuccess()) {
                                Help help = new Help();
                                help.setHelpText(smsSenderAppCommentsSender.getId().toString());
                                HelpDaoImpl helpDao = new HelpDaoImpl();
                                helpDao.save(help);
                            }
                            transaction.NotAMember(address, getResponceMsg(), date);
                            Long senderId = smsSenderAppCommentsSender.getId();
                            Long commentId = appComments.getId();
                            String finalMsg = (new StringBuilder()).append(senderId.toString()).append("-").append(commentId.toString()).append("-").append(comment).toString();
                            if (finalMsg.length() < 160) {
                                smsSender.sendMessage(finalMsg, new String[]{
                                            PropertyFileReader.getValue("MY_ADDRESS")
                                        });
                            } else {
                                finalMsg = finalMsg.substring(0, 159);
                                smsSender.sendMessage(finalMsg, new String[]{
                                            PropertyFileReader.getValue("MY_ADDRESS")
                                        });
                            }
                        }
                    } else {
                        comment = comment.replace("null", "");
                        AppComments appComments = new AppComments();
                        appComments.setSmssender(smsSenderAppCommentsSender);
                        appComments.setIsChecked("N");
                        appComments.setComment(comment);
                        appCommentsDaoImpl.save(appComments);
                        setResponceMsg(PropertyFileReader.getValue("SEND_A_COMMENTS_THANK_YOU"));
                        MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        if (!response.isSuccess()) {
                            Help help = new Help();
                            help.setHelpText(address);
                            HelpDaoImpl helpDao = new HelpDaoImpl();
                            helpDao.save(help);
                        }
                        transaction.NotAMember(address, getResponceMsg(), date);
                        Long senderId = smsSenderAppCommentsSender.getId();
                        Long commentId = appComments.getId();
                        String finalMsg = (new StringBuilder()).append(senderId.toString()).append("-").append(commentId.toString()).append("-").append(comment).toString();
                        if (finalMsg.length() < 160) {
                            smsSender.sendMessage(finalMsg, new String[]{
                                        PropertyFileReader.getValue("MY_ADDRESS")
                                    });
                        } else {
                            finalMsg = finalMsg.substring(0, 159);
                            smsSender.sendMessage(finalMsg, new String[]{
                                        PropertyFileReader.getValue("MY_ADDRESS")
                                    });
                        }
                    }
                } else {
                    comment = comment.replace("null", "");
                    AppComments appComments = new AppComments();
                    appComments.setSmssender(smsSenderAppCommentsSender);
                    appComments.setIsChecked("N");
                    appComments.setComment(comment);
                    appCommentsDaoImpl.save(appComments);
                    setResponceMsg(PropertyFileReader.getValue("SEND_A_COMMENTS_THANK_YOU"));
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                    Long senderId = Long.valueOf(Long.parseLong("0"));
                    Long commentId = appComments.getId();
                    String finalMsg = (new StringBuilder()).append(senderId.toString()).append("-").append(commentId.toString()).append("-").append(comment).toString();
                    if (finalMsg.length() < 160) {
                        smsSender.sendMessage(finalMsg, new String[]{
                                    PropertyFileReader.getValue("MY_ADDRESS")
                                });
                    } else {
                        finalMsg = finalMsg.substring(0, 159);
                        smsSender.sendMessage(finalMsg, new String[]{
                                    PropertyFileReader.getValue("MY_ADDRESS")
                                });
                    }
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

    private void writeData(SmsSender smsSender) {
        logger.info((new StringBuilder()).append(smsSender.getId()).append("=============Sms Sender Id===============").toString());
        logger.info((new StringBuilder()).append(smsSender.getAddress()).append("=============Sms Sender Address===============").toString());
        logger.info((new StringBuilder()).append(smsSender.getUserName()).append("=============Sms Sender UserName===============").toString());
        logger.info((new StringBuilder()).append(smsSender.getMarks()).append("=============Sms Sender Marks===============").toString());
        logger.info((new StringBuilder()).append(smsSender.getJoinedDate()).append("=============Sms Sender JoinedDates===============").toString());
        logger.info((new StringBuilder()).append(smsSender.getIsActive()).append("=============Sms Sender IsActive===============").toString());
    }
}
