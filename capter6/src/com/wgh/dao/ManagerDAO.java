package com.wgh.dao;

import com.wgh.actionForm.ManagerForm;
import com.wgh.core.ConnDB;
import java.sql.*;
import java.util.*;

public class ManagerDAO {
	private ConnDB conn=new ConnDB();
    //����Ա�����֤
    public int checkManager(ManagerForm managerForm) {
        int flag = 1;
        String sql = "SELECT * FROM tb_manager where name='" +
                     managerForm.getName() + "'";
        ResultSet rs = conn.executeQuery(sql);
        try {
            if (rs.next()) {
                String pwd = managerForm.getPwd();
                if (pwd.equals(rs.getString(3))) {
                    rs.last();
                    int rowSum = rs.getRow();	//��ȡ��¼����
                    rs.first();
                    if (rowSum!=1) {
                        flag = 2;
                        System.out.print("��ȡrow��ֵ��" + sql + rowSum);
                    }
                } else {
                    flag = 2;
                }
            }else{
                flag = 2;
            }
        } catch (SQLException ex) {
            flag = 2;
            System.out.println(ex.getMessage());
        }
        return flag;
    }
    //�������
    public int insert(ManagerForm managerForm) {
        String sql1="SELECT * FROM tb_manager WHERE name='"+managerForm.getName()+"'";
        ResultSet rs = conn.executeQuery(sql1);
        String sql = "";
        int falg = 0;
            try {
                if (rs.next()) {
                    falg=2;
                } else {
                    sql = "INSERT INTO tb_manager (name,pwd) values('" +
                                 managerForm.getName() + "','" +
                                 managerForm.getPwd() +
                                 "')";
                    falg = conn.executeUpdate(sql);
                    System.out.println("��ӹ���Ա��Ϣ��SQL��" + sql);
                    conn.close();
                }
            } catch (SQLException ex) {
                falg=0;
            }
        return falg;
    }
    //��ѯ����
    public List query(int id) {
    	List managerList = new ArrayList();
        ManagerForm managerForm1 = null;
        String sql="";
        if(id==0){
            sql = "SELECT * FROM tb_manager";
        }else{
        	sql = "SELECT * FROM tb_manager WHERE id=" +id+ "";
        }
        ResultSet rs = conn.executeQuery(sql);
        try {
            while (rs.next()) {
                managerForm1 = new ManagerForm();
                managerForm1.setID(rs.getInt(1));
                managerForm1.setName(rs.getString(2));
                managerForm1.setPwd(rs.getString(3));
                managerList.add(managerForm1);
            }
        } catch (SQLException ex) {}
        return managerList;
    }
    //�޸Ĺ���Ա����
    public int updatePwd(ManagerForm managerForm){
        String sql="UPDATE tb_manager SET pwd='"+managerForm.getPwd()+"' where name='"+managerForm.getName()+"'";
        int ret=conn.executeUpdate(sql);
        System.out.println("�޸Ĺ���Ա����ʱ��SQL��"+sql);
        conn.close();
        return ret;
    }

//    ɾ������
        public int delete(ManagerForm managerForm) {
            String sql = "DELETE FROM tb_manager where id=" + managerForm.getID() +"";
            int flag = conn.executeUpdate(sql);
            conn.close();
            return flag;
        }
}
