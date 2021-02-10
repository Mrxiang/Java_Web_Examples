package com.mr.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ObjectDao<T> {
	private static SessionFactory sessionFactory = null;
	private Session session = null;//����Session����
	Transaction tx = null;//��������������
	//�������ݿ�
	static {
		try {
			// ����Hibernate�����ļ�
			Configuration cfg = new Configuration().configure();
			sessionFactory = cfg.buildSessionFactory();
		} catch (Exception e) {
			System.err.println("�����Ự����ʧ��");
			e.printStackTrace();
		}
	}
	/**
	 * ɾ������
	 * @param t
	 * @return
	 */
	public boolean deleteT(T t) {
		Session session = sessionFactory.openSession();//����Session
		try {
			tx = session.beginTransaction();//��������
			session.delete(t);//ִ������ɾ������
			tx.commit();//�����ύ
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();//�ر�Session
		}
		return true;
	}
	/**
	 * ��������
	 * @param t
	 * @return
	 */
	public boolean saveT(T t) {
		Session session = sessionFactory.openSession();//����Session
		try {
			tx = session.beginTransaction();//��������
			session.save(t);//ִ��������Ӳ���
			tx.commit();//�����ύ
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();//�ر�Session
		}
		return true;
	}
	/**
	 * �޸�����
	 * @param t
	 * @return
	 */
	public boolean updateT(T t) {
		Session session = sessionFactory.openSession();//����Session
		try {
			tx = session.beginTransaction();//��������
			session.update(t);//ִ�������޸Ĳ���
			tx.commit();//�����ύ
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();//�ر�Session
		}
		return true;
	}
	/**
	 * ͨ�����Ͷ���Ĳ�ѯ������䷽��
	 * @param hql
	 * @return
	 */
	public List<T> queryList(String hql) {
		session = sessionFactory.openSession();//����Session
		tx = session.beginTransaction();//��������
		List<T> list = null;
		try {
			Query query = session.createQuery(hql);//����hql�����в�ѯ
			list = query.list();//�����صĽ����ת����List����
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();//�����ύ
		session.close();//�ر�Session
		return list;//����List����
	}
	/**
	 * �ڲ�ѯ����з���ָ�������ķ���
	 * @param hql
	 * @param showNumber ��ѯ���������
	 * @param beginNumber ��ѯ����ʼλ��
	 * @return
	 */
	public List<T> queryList(String hql, int showNumber, int beginNumber) {
		session = sessionFactory.openSession();//����Session
		tx = session.beginTransaction();//��������
		List<T> list = null;
		try {
			Query query = session.createQuery(hql);//����hql�����в�ѯ
			query.setMaxResults(showNumber);//���ò�ѯ���������
			query.setFirstResult(beginNumber);//���ò�ѯ����ʼλ��
			list = query.list();//�����صĽ����ת����List����
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();//�����ύ
		session.close();//�ر�Session
		return list;//����List����
	}
	/**
	 * ��ѯ������Ϣ
	 * @param hql
	 * @return
	 */
	public T queryFrom(String hql) {
		T t = null;//����ʵ�����
		session = sessionFactory.openSession();//����Session
		tx = session.beginTransaction();//��������
		try {
			Query query = session.createQuery(hql);//����hql�����в�ѯ
			t = (T) query.uniqueResult();//����ѯ���ת��Ϊʵ�����
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();//�����ύ
		session.close();//Session�ر�
		return t;//���ض���
	}

	public List queryListObject(String hql) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List list = null;
		try {
			Query query = session.createQuery(hql);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();
		session.close();
		return list;
	}
}
