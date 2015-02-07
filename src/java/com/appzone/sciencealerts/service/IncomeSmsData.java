package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.IncomeSmsTransactionDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.IncomeSmsTransaction;
import java.util.Date;
import org.apache.log4j.Logger;

public class IncomeSmsData {

    IncomeSmsTransaction incomeSmsTransaction;
    IncomeSmsTransactionDaoImpl incomeSmsTransactionDAOImpl;
    private static final Logger logger = Logger.getLogger(IncomeSmsData.class);

    public IncomeSmsData() {
        incomeSmsTransaction = new IncomeSmsTransaction();
        incomeSmsTransactionDAOImpl = new IncomeSmsTransactionDaoImpl();
    }

    public void CollectIncomeData(String address, String content, Date date) {
        try {
            incomeSmsTransaction.setSenderAddress(address);
            incomeSmsTransaction.setSenderSms(content);
            incomeSmsTransaction.setTransactionTime(date);
            incomeSmsTransactionDAOImpl.save(incomeSmsTransaction);
        } catch (Exception ex) {
            logger.info(ex.toString());
            ex.printStackTrace();
        }
    }
}
