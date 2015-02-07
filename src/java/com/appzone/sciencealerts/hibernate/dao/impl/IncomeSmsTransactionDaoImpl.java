/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.hibernate.dao.impl;

import com.appzone.sciencealerts.hibernate.entity.IncomeSmsTransaction;
import com.appzone.sciencealerts.hibernate.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author TEST
 */
public class IncomeSmsTransactionDaoImpl {

    private Session session;
    private static final Logger logger = Logger.getLogger(IncomeSmsTransactionDaoImpl.class);

//    public IncomeSmsTransactionDaoImpl() {
//        this.session = HibernateUtil.getSessionFactory().openSession();
//    }
    public void save(IncomeSmsTransaction incomeSmsTransaction) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(incomeSmsTransaction);
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

    public void update(IncomeSmsTransaction incomeSmsTransaction) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.update(incomeSmsTransaction);
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

    public void delete(IncomeSmsTransaction incomeSmsTransaction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}