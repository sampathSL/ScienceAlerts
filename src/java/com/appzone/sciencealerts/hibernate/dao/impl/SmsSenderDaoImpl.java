package com.appzone.sciencealerts.hibernate.dao.impl;

import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import com.appzone.sciencealerts.hibernate.util.HibernateUtil;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import java.util.*;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class SmsSenderDaoImpl {

    private Session session;
    private static final Logger logger = Logger.getLogger(SmsSenderDaoImpl.class);

    public void save(SmsSender smsSender) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(smsSender);
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

    public void update(SmsSender smsSender) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(smsSender);
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception subException) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(subException);
        } finally {
            try {
                session.close();
            } // Misplaced declaration of an exception variable
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void delete(SmsSender smsSender) {
        try {
            update(smsSender);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SmsSender findBySmsSenderId(long smsSenderId) {
        SmsSender smsSender = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery((new StringBuilder()).append("from SmsSender as sender where sender.id=").append(smsSenderId).toString());
            smsSender = (SmsSender) query.uniqueResult();
            Hibernate.initialize(smsSender);
            smsSender = (SmsSender) query.uniqueResult();
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return smsSender;
    }

    public boolean searchUserName(String userName) {
        boolean foundUserName = false;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery((new StringBuilder()).append("from SmsSender as sender where sender.userName='").append(userName).append("'").toString());
            List senderList = query.list();
            if (senderList.size() > 0) {
                foundUserName = true;
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
        return foundUserName;
    }

    public List getDailyAlertsSmsSenders() {
        List smsSenderList = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Criteria crit = session.createCriteria(SmsSender.class);
            crit.add(Restrictions.eq("isReg", "Y"));
            crit.add(Restrictions.eq("isActive", "Y"));
            crit.add(Restrictions.eq("isSchedularActive", "Y"));
            crit.setMaxResults(Integer.parseInt(PropertyFileReader.getValue("SCHEDULE_NUMBER_OF_MAX_USERS")));
            smsSenderList = crit.list();
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
        return smsSenderList;
    }

    public List getAboutToDeactiveSmsSenders() {
        List smsSenderList = null;
        Date startDate = null;
        Date endDate = null;
        try {
            Calendar cStartDate = Calendar.getInstance();
            cStartDate.add(5, Integer.parseInt(PropertyFileReader.getValue("ADA_START_DATE")));
            startDate = cStartDate.getTime();
            Calendar cEndDate = Calendar.getInstance();
            cEndDate.add(5, Integer.parseInt(PropertyFileReader.getValue("ADA_END_DATE")));
            endDate = cEndDate.getTime();
            logger.info((new StringBuilder()).append(startDate.toString()).append("===About To Deactivate startDate ===").toString());
            logger.info((new StringBuilder()).append(endDate.toString()).append("===About To Deactivate endDate ===").toString());
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Criteria crit = session.createCriteria(SmsSender.class);
            crit.add(Restrictions.eq("isReg", "Y"));
            crit.add(Restrictions.eq("isActive", "Y"));
            crit.add(Restrictions.eq("isSchedularActive", "Y"));
            crit.add(Restrictions.between("lastActiveTime", startDate, endDate));
            crit.setMaxResults(Integer.parseInt(PropertyFileReader.getValue("SCHEDULE_NUMBER_OF_MAX_USERS")));
            smsSenderList = crit.list();
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
        return smsSenderList;
    }

    public List getDailyDeactiveSmsSenders() {
        List smsSenderList = null;
        Date lastDate = null;
        try {
            Calendar CLastDate = Calendar.getInstance();
            CLastDate.add(5, Integer.parseInt(PropertyFileReader.getValue("DEACTIVE_LAST_DATE")));
            lastDate = CLastDate.getTime();
            logger.info((new StringBuilder()).append(lastDate.toString()).append("===Last Active Date ===").toString());
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Criteria crit = session.createCriteria(SmsSender.class);
            crit.add(Restrictions.eq("isReg", "Y"));
            crit.add(Restrictions.eq("isActive", "Y"));
            crit.add(Restrictions.eq("isSchedularActive", "Y"));
            crit.add(Restrictions.le("lastActiveTime", lastDate));
            crit.setMaxResults(Integer.parseInt(PropertyFileReader.getValue("SCHEDULE_NUMBER_OF_MAX_USERS")));
            smsSenderList = crit.list();
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
        return smsSenderList;
    }

    public List getAllActiveSmsSenders() {
        List smsSenderList = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Criteria crit = session.createCriteria(SmsSender.class);
            crit.add(Restrictions.eq("isReg", "Y"));
            crit.add(Restrictions.eq("isActive", "Y"));
            crit.setMaxResults(Integer.parseInt(PropertyFileReader.getValue("SCHEDULE_NUMBER_OF_MAX_USERS")));
            smsSenderList = crit.list();
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
        return smsSenderList;
    }

    public Long getSmsSenderRank(Long id) {
        Long smsSenderRank = new Long("0");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from SmsSender as sender where sender.isReg = 'Y' order by sender.marks DESC");
            List senderList = query.list();
            if (senderList.size() > 0) {
                for (long count = 0L; count < (long) senderList.size(); count++) {
                    if (((SmsSender) senderList.get(Integer.parseInt(Long.toString(count)))).getId().equals(id)) {
                        smsSenderRank = Long.valueOf(count + 1L);
                    }
                }
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
        return smsSenderRank;
    }

    public SmsSender findBySmsSenderAddress(String address) {
        SmsSender smsSender = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery((new StringBuilder()).append("from SmsSender as sender where sender.address='").append(address.toString()).append("'").toString());
            smsSender = (SmsSender) query.uniqueResult();
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
        return smsSender;
    }
}
