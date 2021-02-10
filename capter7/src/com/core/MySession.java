package com.core;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MySession {
    static SessionFactory sessionFactory;
    static {
        try {
            Configuration config = new Configuration().configure();
            sessionFactory = config.buildSessionFactory();
        } catch (Exception e) {
            System.out.println("����SessionFactoryʱ�Ĵ�����Ϣ��" + e.getMessage());
        }
    }

    //��Session
    public static Session openSession() {
        Session session = sessionFactory.openSession();
        return session;
    }

    //�ر�Session
    public static void closeSession(Session session) {
        if (session != null) {
            session.close();
        }
    }

}
