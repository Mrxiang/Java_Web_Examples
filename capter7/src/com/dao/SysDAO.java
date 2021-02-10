package com.dao;

import com.core.MySession;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SysDAO {
    public SysDAO(){
    }
    private Session session = null;
    public int sysinitialize(){
        session = MySession.openSession(); //��Session
        Transaction tx = null;
        int rtn = 0;
        try {
            tx=session.beginTransaction();
            session.createQuery("DELETE UserForm where name<>'mr'").executeUpdate();      //ɾ���û���Ϣ
            session.createQuery("DELETE GetUseForm").executeUpdate();      //��ղ���������Ϣ��
            session.createQuery("DELETE DamageForm").executeUpdate();      //��ղ��ű�����Ϣ��
            session.createQuery("DELETE CheckForm").executeUpdate();      //��������Ϣ��
            session.createQuery("DELETE InStorageForm").executeUpdate();      //��������Ϣ��
            session.createQuery("DELETE StockDetailForm").executeUpdate();      //��ղɹ���ϸ��
            session.createQuery("DELETE StockMainForm").executeUpdate();      //��ղɹ�����
            session.createQuery("DELETE ProviderForm").executeUpdate();      //��չ�Ӧ����Ϣ��
            session.createQuery("DELETE LoanForm").executeUpdate();      //������ʽ����Ϣ��
            session.createQuery("DELETE StorageForm").executeUpdate();      //��տ����Ϣ��
            session.createQuery("DELETE BranchForm").executeUpdate();      //��ղ�����Ϣ��
            session.createQuery("DELETE GoodsForm").executeUpdate();      //�����Ʒ��Ϣ��
            rtn=1;
            tx.commit();
        }catch(Exception e){
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("ϵͳ��ʹ��ʱ�Ĵ�����Ϣ��"+e.getMessage());
            rtn=0;
        } finally {
            MySession.closeSession(session);
        }
        return rtn;
    }
}
