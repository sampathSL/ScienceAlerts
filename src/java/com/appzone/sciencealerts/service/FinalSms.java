package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.SmsTransactionDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import com.appzone.sciencealerts.hibernate.entity.SmsTransaction;
import java.util.Date;
import org.apache.log4j.Logger;

public class FinalSms {

    SmsTransaction smsTransaction;
    SmsTransactionDaoImpl smsTransactionDAOImpl;
    private static final Logger logger = Logger.getLogger(FinalSms.class);

    public FinalSms() {
        smsTransaction = new SmsTransaction();
        smsTransactionDAOImpl = new SmsTransactionDaoImpl();
    }

    public void CollectData(SmsSender smsReceiver, String receiverSms, Date transactionTime) {
        try {
            if (smsReceiver != null && smsReceiver.getAddress() != null) {
                smsTransaction.setReceiverAddress(smsReceiver.getAddress());
                smsTransaction.setReceiverSms(receiverSms);
                smsTransaction.setTransactionTime(transactionTime);
                smsTransaction.setReceiverAddress(smsReceiver.getAddress());
                smsTransaction.setReceiverSms(receiverSms);
                smsTransaction.setTransactionTime(transactionTime);
                smsTransactionDAOImpl.save(smsTransaction);
            } else if (smsReceiver == null) {
                logger.error("Unexpected Error");
            }
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    public void NotAMember(String address, String receiverSms, Date transactionTime) {
        try {
            smsTransaction.setReceiverAddress(address);
            smsTransaction.setReceiverSms(receiverSms);
            smsTransaction.setTransactionTime(transactionTime);
            smsTransactionDAOImpl.save(smsTransaction);
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    private void writeData(SmsTransaction smsTransaction) {
        logger.info((new StringBuilder()).append(smsTransaction.getId()).append("=============Transaction Id===============").toString());
        logger.info((new StringBuilder()).append(smsTransaction.getReceiverAddress()).append("=============Transaction Receiver Address===============").toString());
        logger.info((new StringBuilder()).append(smsTransaction.getReceiverSms()).append("=============Receiver Sms===============").toString());
        logger.info((new StringBuilder()).append(smsTransaction.getTransactionTime()).append("=============Transaction Time===============").toString());
    }
}
