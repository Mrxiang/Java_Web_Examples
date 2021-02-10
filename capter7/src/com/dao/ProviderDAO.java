package com.dao;

import com.actionForm.ProviderForm;
import com.core.MySession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProviderDAO {
    private Session session = null;
    public List query(String strif) {
        session = MySession.openSession();  //��Session
        List list=null;
            String hql = "";
            if (strif != "all" && strif != null && strif != "") { //������ѯ
                hql = "FROM ProviderForm provider WHERE " + strif +
                      "";
            } else { //��ѯȫ������
                hql = "FROM ProviderForm provider";
            }
            System.out.println(hql);
        try{
            Query query = session.createQuery(hql);
            list = query.list();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            MySession.closeSession(session);  //�ر�Session
        }
        return list;
    }

public int insert(ProviderForm providerForm){
    int ret=0;
    Transaction tx=null;
//    ChStr chStr=new ChStr();
//    providerForm.setName(chStr.toChinese(providerForm.getName()));
//    providerForm.setAddress(chStr.toChinese(providerForm.getAddress()));
//    providerForm.setBankName(chStr.toChinese(providerForm.getBankName()));
//    providerForm.setMemo(chStr.toChinese(providerForm.getMemo()));
    String str="name='" + providerForm.getName() + "'";
    List list = query(str);
        if (list.size()> 0) { //���ڸ���Ϣ
        ret=2;
    }else{
        session= MySession.openSession(); //��Session
        try {
            tx = session.beginTransaction();
            session.save(providerForm);
            tx.commit();
            ret = 1;
        } catch (Exception e) {
            if (tx != null) {
                 tx.rollback();
            }
            System.out.println("��ӹ�Ӧ����Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            return ret = 0;
        } finally {
             MySession.closeSession(session); //�ر�Session
        }
    }
    return ret;
}
//��ѯҪ�޸ĵĹ�Ӧ����Ϣ
public ProviderForm query(int id){
    session= MySession.openSession();     //��Session
    ProviderForm providerForm=null;
    try{
        providerForm=(ProviderForm)session.get(ProviderForm.class,id);

    }catch(Exception e){
        e.printStackTrace();
    }finally{
        MySession.closeSession(session); //�ر�Session
    }
    return providerForm;
}
//�޸Ĺ�Ӧ����Ϣ
public int Modify(ProviderForm providerForm){
 session= MySession.openSession();    //��Session
 int id=providerForm.getId();
 int ret = 0;
 Transaction tx = null;
 try {
     tx = session.beginTransaction();
    // ProviderForm providerF=(ProviderForm)session.get(ProviderForm.class,id);

     session.update(providerForm);
     tx.commit();
     ret = 1;
 } catch (Exception e) {
     if (tx != null) {
         tx.rollback();
     }
     System.out.println("�޸Ĺ�Ӧ����Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
     return ret = 0;
 } finally {
     MySession.closeSession(session);   //�ر�Session
 }
 return ret;
    }
//ɾ����Ӧ����Ϣ
public int del(int id){
     session= MySession.openSession(); //��Session
     int ret = 0;
     Transaction tx = null;
     try {
         tx = session.beginTransaction();
         ProviderForm providerF=(ProviderForm)session.get(ProviderForm.class,id);
         session.delete(providerF);
         tx.commit();
         ret = 1;
     } catch (Exception e) {
         if (tx != null) {
             tx.rollback();
         }
         System.out.println("ɾ����Ӧ����Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
         return ret = 0;
     } finally {
         MySession.closeSession(session); //�ر�Session
     }
     return ret;
    }


}
