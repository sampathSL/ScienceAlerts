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
//            FinalSms, InvalidMessage
public class ScienceAlertsHelp {

    private MchoiceAventuraSmsSender smsSender;
    private String responceMsg;
    SmsSenderDaoImpl smsSenderDAOImpl;
    private static final Logger logger = Logger.getLogger(ScienceAlertsHelp.class);
    FinalSms transaction;

    public ScienceAlertsHelp() {
        responceMsg = "";
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void GetHelp(String address, String helpText[]) {
        Date date = new Date();
        try {
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
            smsSender = new MchoiceAventuraSmsSender(new URL(AppZoneConfig.getURL()), AppZoneConfig.getApp_Id(), AppZoneConfig.getPass());
            if (helpText.length > 3) {
                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("INCORRECT_COMMAND")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_HELP")).toString());
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
            } else if (helpText.length == 2) {
                setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTIONS")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_HELP1")).toString());
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
            } else {
                MchoiceAventuraResponse response = null;
                if (helpText[2].trim().toString().toLowerCase().equalsIgnoreCase("")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTIONS")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_HELP1")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().toLowerCase().equalsIgnoreCase(" ")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTIONS")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_HELP1")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("1")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION1")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_RANK")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("2")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION2")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_UNREG")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("3")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION3")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_SETN")).toString());
                    smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("4")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION4")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_GETINFO")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("5")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION5")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_ACT_SCHEDULE")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("6")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION6")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_DEACT_SCHEDULE")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("7")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION7")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_GET")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else if (helpText[2].trim().toString().equalsIgnoreCase("8")) {
                    setResponceMsg((new StringBuilder()).append(PropertyFileReader.getValue("GET_HELP_OPTION8")).append(" ").append(PropertyFileReader.getValue("DEFAULT_PORT")).append(".").append(PropertyFileReader.getValue("EXAMPLE_COMMENT")).toString());
                    response = smsSender.sendMessage(getResponceMsg(), new String[]{
                                address
                            });
                    if (!response.isSuccess()) {
                        Help help = new Help();
                        help.setHelpText(address);
                        HelpDaoImpl helpDao = new HelpDaoImpl();
                        helpDao.save(help);
                    }
                    transaction.NotAMember(address, getResponceMsg(), date);
                } else {
                    InvalidMessage invalidMessage = new InvalidMessage();
                    invalidMessage.InformInvalideMessage(address);
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
