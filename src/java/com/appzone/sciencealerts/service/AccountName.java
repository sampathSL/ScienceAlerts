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
//            FinalSms, ScienceAlert
public class AccountName {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(AccountName.class);
    FinalSms transaction;

    public AccountName() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void AccountUserName(String address, String setUsrNameStr[]) {
        Date date = new Date();
        try {
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            SmsSender smsSenderSetName = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (setUsrNameStr.length < 8) {
                if (smsSenderSetName != null) {
                    String name = "";
                    for (int count = 2; count < setUsrNameStr.length; count++) {
                        name = (new StringBuilder()).append(name).append(" ").append(setUsrNameStr[count].trim()).toString();
                    }

                    name = name.trim();
                    if (name.length() > 2 && name.length() < 20) {
                        boolean checkUserNameExists = smsSenderDAOImpl.searchUserName(name.trim());
                        if (smsSenderSetName.getIsReg().equalsIgnoreCase("Y")) {
                            if (smsSenderSetName.getIsActive().equalsIgnoreCase("Y")) {
                                if (checkUserNameExists) {
                                    if (smsSenderSetName.getUserName().equalsIgnoreCase(name.trim())) {
                                        smsSenderSetName.setLastActiveTime(date);
                                        smsSenderDAOImpl.update(smsSenderSetName);
                                        setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("USER_NAME_ALREADY_EXISTS_CHANGE_FOR_SAME_USER_NAME_OLD_MEMEBER")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_SETN_DIFF1")).toString());
                                        MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                                    address
                                                });
                                        if (!response.isSuccess()) {
                                            Help help = new Help();
                                            help.setHelpText(smsSenderSetName.getId().toString());
                                            HelpDaoImpl helpDao = new HelpDaoImpl();
                                            helpDao.save(help);
                                        }
                                        transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
                                    } else {
                                        smsSenderSetName.setLastActiveTime(date);
                                        smsSenderDAOImpl.update(smsSenderSetName);
                                        setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("USER_NAME_ALREADY_EXISTS_ERROR_OLD_MEMBER")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_SETN_DIFF2")).toString());
                                        MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                                    address
                                                });
                                        if (!response.isSuccess()) {
                                            Help help = new Help();
                                            help.setHelpText(smsSenderSetName.getId().toString());
                                            HelpDaoImpl helpDao = new HelpDaoImpl();
                                            helpDao.save(help);
                                        }
                                        transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
                                    }
                                } else {
                                    smsSenderSetName.setLastActiveTime(date);
                                    smsSenderSetName.setUserName(name.trim());
                                    smsSenderDAOImpl.update(smsSenderSetName);
                                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("EXISTING_USER_NAME_CHANGE_SUCCESS_SUB1")).append(name.trim()).append(PropertyFileReader.getValue("EXISTING_USER_NAME_CHANGE_SUCCESS_SUB2")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_GET")).toString());
                                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                                address
                                            });
                                    if (!response.isSuccess()) {
                                        Help help = new Help();
                                        help.setHelpText(smsSenderSetName.getId().toString());
                                        HelpDaoImpl helpDao = new HelpDaoImpl();
                                        helpDao.save(help);
                                    }
                                    transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
                                }
                            } else if (checkUserNameExists) {
                                smsSenderSetName.setLastActiveTime(date);
                                smsSenderDAOImpl.update(smsSenderSetName);
                                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("USER_NAME_ALREADY_EXISTS_ERROR_NEW_MEMBER")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_SETN")).toString());
                                MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                            address
                                        });
                                if (!response.isSuccess()) {
                                    Help help = new Help();
                                    help.setHelpText(smsSenderSetName.getId().toString());
                                    HelpDaoImpl helpDao = new HelpDaoImpl();
                                    helpDao.save(help);
                                }
                                transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
                            } else {
                                Long marks = smsSenderSetName.getMarks();
                                marks = Long.valueOf(marks.longValue() + 1L);
                                smsSenderSetName.setMarks(marks);
                                smsSenderSetName.setLastActiveTime(date);
                                smsSenderSetName.setUserName(name.trim());
                                smsSenderSetName.setIsActive("Y");
                                smsSenderSetName.setIsSchedularActive("Y");
                                smsSenderDAOImpl.update(smsSenderSetName);
                                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("USER_NAME_SET_SUCCESS_SUB1")).append(name.trim()).append(PropertyFileReader.getValue("USER_NAME_SET_SUCCESS_SUB2")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_GET")).toString());
                                MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                            address
                                        });
                                if (!response.isSuccess()) {
                                    Help help = new Help();
                                    help.setHelpText(smsSenderSetName.getId().toString());
                                    HelpDaoImpl helpDao = new HelpDaoImpl();
                                    helpDao.save(help);
                                }
                                transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
                                MchoiceAventuraSmsSender smsSenderScienceAlertFirst = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
                                FinalSms transactionFirst = new FinalSms();
                                ScienceAlert scienceAlert = new ScienceAlert();
                                String alert = scienceAlert.GetScienceAlert();
                                setResponceMsg(alert);
                                MchoiceAventuraResponse response_ = smsSenderScienceAlertFirst.sendMessage(getResponceMsg(), new String[]{
                                            address
                                        });
                                if (!response_.isSuccess()) {
                                    Help help = new Help();
                                    help.setHelpText(smsSenderSetName.getId().toString());
                                    HelpDaoImpl helpDao = new HelpDaoImpl();
                                    helpDao.save(help);
                                }
                                transactionFirst.CollectData(smsSenderSetName, getResponceMsg(), date);
                            }
                        } else {
                            smsSenderSetName.setLastActiveTime(date);
                            smsSenderDAOImpl.update(smsSenderSetName);
                            setResponceMsg(PropertyFileReader.getValue("REG_ERROR"));
                            MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                        address
                                    });
                            if (!response.isSuccess()) {
                                Help help = new Help();
                                help.setHelpText(smsSenderSetName.getId().toString());
                                HelpDaoImpl helpDao = new HelpDaoImpl();
                                helpDao.save(help);
                            }
                            transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
                        }
                    } else {
                        smsSenderSetName.setLastActiveTime(date);
                        smsSenderDAOImpl.update(smsSenderSetName);
                        setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("USER_NAME_LENGTH_ERROR")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_SETN")).toString());
                        MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                    address
                                });
                        if (!response.isSuccess()) {
                            Help help = new Help();
                            help.setHelpText(smsSenderSetName.getId().toString());
                            HelpDaoImpl helpDao = new HelpDaoImpl();
                            helpDao.save(help);
                        }
                        transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
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
                    transaction.CollectData(sender, getResponceMsg(), date);
                }
            } else if (smsSenderSetName != null) {
                if (smsSenderSetName.getIsReg().equalsIgnoreCase("Y")) {
                    smsSenderSetName.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderSetName);
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("INCORRECT_COMMAND")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_HELP")).toString());
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderSetName.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
                } else {
                    smsSenderSetName.setLastActiveTime(date);
                    smsSenderDAOImpl.update(smsSenderSetName);
                    setResponceMsg(PropertyFileReader.getValue("REG_ERROR"));
                    MchoiceAventuraResponse response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(smsSenderSetName.getId().toString());
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.CollectData(smsSenderSetName, getResponceMsg(), date);
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
                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("INCORRECT_COMMAND")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_HELP")).toString());
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

    private String getResponceMsg() {
        return responceMsg;
    }

    private void setResponceMsg(String responceMsg) {
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