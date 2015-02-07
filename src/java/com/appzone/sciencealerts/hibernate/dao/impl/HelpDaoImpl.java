/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.hibernate.dao.impl;

import com.appzone.sciencealerts.hibernate.entity.Help;
import com.appzone.sciencealerts.hibernate.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author SAM
 */
public class HelpDaoImpl {

    private Session session;
    private static final Logger logger = Logger.getLogger(HelpDaoImpl.class);

    public void save(Help help) {

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(help);
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

    public void update(Help help) {

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.update(help);
                session.flush();
                session.clear();
                transaction.commit();
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

    public void delete(Help help) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
