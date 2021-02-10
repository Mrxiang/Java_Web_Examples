package com.dao;

import com.actionForm.UserForm;
import com.core.MySession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class UserDAO {
    private Session session = null;
    public List query(String strif) {
        session= MySession.openSession();    //��Session
        String hql = "";
        if (strif != "all" && strif != null && strif != "") { //������ѯ
            hql = "FROM UserForm user WHERE " + strif +
                  "";
        } else { //��ѯȫ������
            hql = "FROM UserForm user";
        }
        System.out.println(hql);
        List list=null;
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
    //�����û���Ϣ
    public int insert(UserForm userForm) {
        int ret = 0;
        Transaction tx = null;
        String str="name='" + userForm.getName() + "'";
        List list = query(str);

        if (list.size()> 0) { //���ڸ���Ϣ
            ret = 2;
        } else {
            session= MySession.openSession();    //��Session
            try {
                tx = session.beginTransaction();
                session.save(userForm);
                tx.commit();
                ret = 1;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
                System.out.println("����û���Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
                return ret = 0;
            } finally {
                MySession.closeSession(session);   //�ر�Session
            }
        }
        return ret;
    }
    public String login(UserForm userForm, HttpServletRequest request){
        String rtn="";
        String name=userForm.getName();
        String pwd=userForm.getPwd();
        String str="name='"+name+"'";
        List list = query(str);
        if(list.size()==1){
            UserForm m=(UserForm)list.get(0);
            String username=m.getName();
             if(pwd.equals(m.getPwd())){
                rtn="ok";
                //********����¼���û����Ʊ��浽HttpSession��****
                HttpSession httpsession=request.getSession();
                httpsession.setAttribute("username",username);
                //********************************************
                System.out.println("��¼�ɹ���");
            }else{
                rtn="��������������";
                System.out.println("�������");
            }
        }else{
            rtn="��������û����ƴ���";
            System.out.println("��������û����ƴ���");
        }
        return rtn;
    }
    //��ѯҪ�޸ĵ��û���Ϣ
    public UserForm query(int id) {
        session = MySession.openSession(); //��Session
        UserForm userForm =null;
        try{
            userForm = (UserForm) session.get(UserForm.class, id);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            MySession.closeSession(session);  //�ر�Session
        }
        return userForm;
    }

    //�޸��û���Ϣ
    public int Modify(UserForm userForm, int flag) {
        int id = userForm.getId();
        int ret = 0;
        Transaction tx = null;
        session= MySession.openSession();    //��Session
        try {
            tx = session.beginTransaction();
             UserForm userF=(UserForm)session.get(UserForm.class,id);
             if(flag==1){
                 userF.setPwd(userForm.getPwd());
             }else{
                 userF.setSetInstorage(userForm.getSetInstorage());
                 userF.setSetOutstorage(userForm.getSetOutstorage());
                 userF.setSetDeal(userForm.getSetDeal());
                 userF.setSetQuery(userForm.getSetQuery());
                 userF.setSetBasic(userForm.getSetBasic());
                 userF.setSetSys(userForm.getSetSys());
             }
            session.update(userF);
            tx.commit();
            ret = 1;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("�޸��û���Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            return ret = 0;
        } finally {
            MySession.closeSession(session);   //�ر�Session
        }
        return ret;
    }

    //ɾ���û���Ϣ
    public int del(int id) {
        session= MySession.openSession();    //��Session
        int ret = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            UserForm userForm = (UserForm) session.get(UserForm.class, id);
            session.delete(userForm);
            tx.commit();
            ret = 1;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("ɾ���û���Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            return ret = 0;
        } finally {
            MySession.closeSession(session);   //�ر�Session
        }
        return ret;
    }

}
