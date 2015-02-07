package com.appzone.sciencealerts.hibernate.dao.impl;

import com.appzone.sciencealerts.hibernate.entity.ScienceAlerts;
import com.appzone.sciencealerts.hibernate.util.HibernateUtil;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import java.util.*;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ScienceAlertsDaoImpl {

    private Session session;
    private static final Logger logger = Logger.getLogger(ScienceAlertsDaoImpl.class);

    public void save(ScienceAlerts scienceAlerts) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(scienceAlerts);
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
        } finally {
            try {
                session.close();
            } // Misplaced declaration of an exception variable
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void update(ScienceAlerts scienceAlerts) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(scienceAlerts);
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
        } finally {
            try {
                session.close();
            } // Misplaced declaration of an exception variable
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void delete(ScienceAlerts scienceAlerts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean CheckUniqueTitle(String title) {
        boolean foundTitle = false;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            if (title != null && !"".equalsIgnoreCase(title)) {
                Criteria crit = session.createCriteria(ScienceAlerts.class);
                crit.add(Restrictions.eq("title", title));
                List senderList = crit.list();
                if (senderList.size() > 0) {
                    foundTitle = true;
                }
            }
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception ex) {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return foundTitle;
    }

    public String GenerateRandomAlert() {
        String scienceAlert = "";
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            ScienceAlerts result = null;
            Criteria crit = session.createCriteria(ScienceAlerts.class);
            crit.setProjection(Projections.rowCount());
            int count = ((Number) crit.uniqueResult()).intValue();
            if (0 != count) {
                int index = (new Random()).nextInt(count);
                crit = session.createCriteria(ScienceAlerts.class);
                result = (ScienceAlerts) crit.setFirstResult(index).setMaxResults(1).uniqueResult();
                scienceAlert = result.getSms();
            } else {
                scienceAlert = "Unexpected Error Occured!!!";
            }
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } // Misplaced declaration of an exception variable
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return scienceAlert;
    }

    public ScienceAlerts GetDailyScienceAlert() {
        Date startDate;
        Date endDate;
        ScienceAlerts scienceDailyAlert = null;
        try {
            Calendar alertDate = Calendar.getInstance();
            alertDate.add(5,Integer.parseInt(PropertyFileReader.getValue("DAILY_ALERTS_SET_DATE")));
            Calendar startDate_ = alertDate;
            startDate = startDate_.getTime();
            alertDate.add(5,1);
            Calendar endDate_ = alertDate;
            endDate = endDate_.getTime();
            logger.info((new StringBuilder()).append(startDate.toString()).append("===Science startDate Date===").toString());
            logger.info((new StringBuilder()).append(endDate.toString()).append("===Science endDate_ Date===").toString());
            scienceDailyAlert = null;
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Criteria crit = session.createCriteria(ScienceAlerts.class);
            crit.add(Restrictions.eq("scheduled","N"));
            crit.add(Restrictions.between("publishDate", startDate, endDate));
            crit.setMaxResults(1);
            if (crit.uniqueResult() != null) {
                scienceDailyAlert = (ScienceAlerts) crit.uniqueResult();
            }
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception e) {
        } finally {
            try {
                session.close();
            } // Misplaced declaration of an exception variable
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return scienceDailyAlert;
    }

    public ScienceAlerts GetAdminScienceAlert(long alertId) {
        ScienceAlerts scienceDailyAlert = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Criteria crit = session.createCriteria(ScienceAlerts.class);
            crit.add(Restrictions.eq("id", Long.valueOf(alertId)));
            crit.setMaxResults(1);
            if (crit.uniqueResult() != null) {
                scienceDailyAlert = (ScienceAlerts) crit.uniqueResult();
            }
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                session.close();
            } // Misplaced declaration of an exception variable
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return scienceDailyAlert;
    }
}
