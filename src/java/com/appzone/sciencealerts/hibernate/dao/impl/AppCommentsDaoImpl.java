/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.hibernate.dao.impl;

import com.appzone.sciencealerts.hibernate.entity.AppComments;
import com.appzone.sciencealerts.hibernate.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.*;

public class AppCommentsDaoImpl {

    private Session session;
    private static final Logger logger = Logger.getLogger(AppCommentsDaoImpl.class);

    public void save(AppComments appComments) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(appComments);
                transaction.commit();
                session.flush();
                session.clear();
            } catch (Exception subEx) {
                try {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception ex) {
                    logger.error(subEx);
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

    public void update(AppComments appComments) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.update(appComments);
                transaction.commit();
                session.flush();
                session.clear();
            } catch (Exception subEx) {
                try {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception ex) {
                    logger.error(subEx);
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

    public void delete(AppComments appComments) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AppComments findByAppCommentId(long appCommentId) {
        AppComments appComments = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery((new StringBuilder()).append("from AppComments as comments where comments.id=").append(appCommentId).toString());
            appComments = (AppComments) query.uniqueResult();
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return appComments;
        }
    }
}
