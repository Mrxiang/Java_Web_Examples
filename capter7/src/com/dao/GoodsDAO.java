package com.dao;

import com.actionForm.GoodsForm;
import com.core.MySession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GoodsDAO {
    private Session session = null;
    public List query(String strif, int del) {
        session = MySession.openSession(); //��Session
        String hql = "";
        if (strif != "all" && strif != null && strif != "") { //������ѯ
            hql = "FROM GoodsForm goods WHERE " + strif + "";
        } else { //��ѯȫ������
            hql = "FROM GoodsForm goods WHERE ifdel=" + del + " ORDER BY ifdel";
        }
        System.out.println(hql);
        List list=null;
        try {
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return list;
    }

    //�޸���Ʒ��Ϣʱ�Ĳ�ѯ
    public GoodsForm query(int id) {
        session = MySession.openSession(); //��Session
        GoodsForm goodsForm = null;
        try {
            goodsForm = (GoodsForm) session.get(GoodsForm.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return goodsForm;
    }

    public int insert(GoodsForm goodsForm) {
        int ret = 0;
        Transaction tx = null;
        String str = "name='" + goodsForm.getName() + "' AND spec='" +
                     goodsForm.getSpec() + "'";
        List list = query(str, 0);
        if (list.size() > 0) { //���ڸ���Ϣ
            ret = 2;
        } else {
            session = MySession.openSession(); //��Session
            try {
                tx = session.beginTransaction();
                session.save(goodsForm);
                tx.commit();
                ret = 1;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                System.out.println("���������Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
                return ret = 0;
            } finally {
                MySession.closeSession(session); //�ر�Session
            }
        }
        return ret;
    }

    // �޸�������Ϣ
    public int update(GoodsForm goodsForm) {
        session = MySession.openSession(); //��Session
        int ret = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
//            ChStr chStr=new ChStr();
            GoodsForm goodsF = (GoodsForm) session.get(GoodsForm.class,
                    goodsForm.getId());
            goodsF.setSpec(goodsForm.getSpec());
            goodsF.setUnit(goodsForm.getUnit());
            goodsF.setPrice(goodsForm.getPrice());
            goodsF.setProducer(goodsForm.getProducer());
//            goodsF.setSpec(chStr.toChinese(goodsForm.getSpec()));
//            goodsF.setUnit(chStr.toChinese(goodsForm.getUnit()));
//            goodsF.setPrice(goodsForm.getPrice());
//            goodsF.setProducer(chStr.toChinese(goodsForm.getProducer()));
            session.update(goodsF);
            tx.commit();
            ret = 1;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("�޸�������Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            return ret = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return ret;
    }

    public int del(int id, byte val) {
        session = MySession.openSession(); //��Session
        int ret = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            GoodsForm goodsF = (GoodsForm) session.get(GoodsForm.class, id);
            goodsF.setIfdel(val);
            System.out.println(val + "*********" + id);
            session.update(goodsF);
            tx.commit();
            ret = 1;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("ɾ��/�ָ�������Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            return ret = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return ret;
    }
}
