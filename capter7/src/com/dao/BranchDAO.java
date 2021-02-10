package com.dao;

import com.actionForm.BranchForm;
import com.core.MySession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BranchDAO {
    private Session session = null;
    public List query(String strif) {
        session = MySession.openSession(); //��Session
        String hql = "";
        if (strif != "all" && strif != null && strif != "") { //������ѯ
            hql = "FROM BranchForm branch WHERE " + strif +
                  "";
        } else { //��ѯȫ������
            hql = "FROM BranchForm branch";
        }
        System.out.println(hql);
        List list = null;
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

//���沿����Ϣ
    public int insert(BranchForm branchForm) {
        int ret = 0;
        Transaction tx = null;
//    ChStr chStr=new ChStr();
//    branchForm.setName(chStr.toChinese(branchForm.getName()));
//    branchForm.setMemo(chStr.toChinese(branchForm.getMemo()));
        String str = "name='" + branchForm.getName() + "'";
        List list = query(str);
        if (list.size() > 0) { //���ڸ���Ϣ
            ret = 2;
        } else {
            session = MySession.openSession(); //��Session
            try {
                tx = session.beginTransaction();
                session.save(branchForm);
                tx.commit();
                ret = 1;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                System.out.println("��Ӳ�����Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
                return ret = 0;
            } finally {
                MySession.closeSession(session); //�ر�Session
            }
        }
        return ret;
    }

//ɾ��������Ϣ
    public int del(int id) {
        session = MySession.openSession(); //��Session
        int ret = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            BranchForm branchF = (BranchForm) session.get(BranchForm.class, id);
            session.delete(branchF);
            tx.commit();
            ret = 1;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("ɾ��������Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            return ret = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return ret;
    }


}
