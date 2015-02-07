/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.hibernate.dao.impl;

import com.appzone.sciencealerts.hibernate.entity.SmsTransaction;
import com.appzone.sciencealerts.hibernate.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author SAM
 */
public class SmsTransactionDaoImpl {

    private Session session;
    private static final Logger logger = Logger.getLogger(SmsTransactionDaoImpl.class);

    public SmsTransactionDaoImpl() {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public void save(SmsTransaction smsTransaction) {

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(smsTransaction);
                transaction.commit();
                session.flush();
                session.clear();
            } catch (Exception subEx) {
                try {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                session.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void update(SmsTransaction smsTransaction) {

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.update(smsTransaction);
                transaction.commit();
                session.flush();
                session.clear();
            } catch (Exception subEx) {
                try {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void delete(SmsTransaction smsTransaction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
