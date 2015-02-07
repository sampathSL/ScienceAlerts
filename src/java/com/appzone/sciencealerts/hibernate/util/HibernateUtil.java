/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.hibernate.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final Logger logger;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    static {
        logger = Logger.getLogger(HibernateUtil.class);
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            //sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            sessionFactory = (new Configuration()).configure("/hibernate.cfg.xml").buildSessionFactory();
        } catch (ExceptionInInitializerError ex) {
            logger.error(ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}