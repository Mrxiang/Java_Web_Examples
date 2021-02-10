package com.wgh.dao;

import com.wgh.actionForm.StudentForm;
import com.wgh.core.ChStr;
import com.wgh.core.ConnDB;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class StudentDAO {
	private ConnDB conn=new ConnDB();
	private ChStr chStr=new ChStr();
    //���������֤
    public int checkStudent(StudentForm studentForm) {
        int flag = 1;
        String sql = "SELECT * FROM tb_student where ID='" +
                     studentForm.getID()+ "'";
        ResultSet rs = conn.executeQuery(sql);
        try {
            if (rs.next()) {
                String pwd = studentForm.getPwd();
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
        } catch (Exception ex) {
            flag = 2;
            System.out.println(ex.getMessage());
        }
        return flag;
    }

    //�������
    public String insert(StudentForm s) {
        String sql1="SELECT * FROM tb_student WHERE cardNo='"+s.getCardNo()+"'";
        ResultSet rs = conn.executeQuery(sql1);							//ִ��SQL��ѯ���
        String sql = "";
        String flag = "miss";											//���ڼ�¼������Ϣ�ı���
        String ID="";
            try {
                if (rs.next()) {											//������ڼ�¼
                    flag="re";										//��ʾ������Ϣ�Ѿ�ע��
                } else {
    				/*****************�Զ�����׼��֤��***********************************************/
                	String sql_max="SELECT max(ID) FROM tb_student";
                	ResultSet rs_max=conn.executeQuery(sql_max);			//��ѯ����׼��֤��
            		java.util.Date date=new java.util.Date();				//ʵ����java.util.Date()��
            		String newTime=new SimpleDateFormat("yyyyMMdd").format(date);	//��ʽ����ǰ����
                	if(rs_max.next()){
                		
	                	if(rs_max.getString(1)!=null){
	                		String max_ID=rs_max.getString(1);				//��ȡ����׼��֤��
	                		int newId=Integer.parseInt(max_ID.substring(10,16))+1;//ȡ�����׼��֤���е����ֱ��+1
	                		String no=chStr.formatNO(newId,6);				//�����ɵı�Ÿ�ʽ��Ϊ6λ
//	                		ID="CN"+newTime+no;						//���������׼��֤��
	                		ID=newTime+no;						//���������׼��֤��
	                	}else{											//����һ������ע��ʱ
//	                		ID="CN"+newTime+"000001";					//���ɵ�һ��׼��֤��
	                		ID=newTime+"000001";					//���ɵ�һ��׼��֤��
	                	}
                	}else{
                		flag="miss";
                	}
                	/********************************************************************************/
              		sql = "INSERT INTO tb_student (ID,name,pwd,sex,question,answ er,profession,cardNo) values('" +
                                 ID+ "','" +s.getName() +"','"+s.getPwd()+"','"+s.getSex()+"','"+s.getQuestion()+
                                 "','"+s.getAnswer()+"','"+s.getProfession()+"','"+s.getCardNo()+"')";
                    int ret= conn.executeUpdate(sql);					//���濼��ע����Ϣ
                    
                    System.out.println("SQL "+sql);
                    if(ret==0){
                    	flag="miss";									//��ʾ����ע��ʧ��
                    }else{
                    	flag="��ϲ����ע��ɹ�!\\r���ס����׼��֤�ţ�"+ID;	//�������ɵ�׼��֤��
                    }
                    conn.close();											//�ر����ݿ�����
                }
            } catch (Exception e) {
                flag="miss1";
                e.printStackTrace();
                System.out.println("����ע��ʱ�Ĵ�����Ϣ��"+e.getMessage());	//���������ʾ��Ϣ������̨
            }
        return flag;
    }

    //��ѯ����
    public List query(String id) {
    	List studentList = new ArrayList();
        StudentForm studentForm1 = null;
        String sql="";
        if(id==null ||id.equals("")){
            sql = "SELECT * FROM tb_student ORDER BY joinTime DESC";
        }else{
        	sql = "SELECT * FROM tb_student WHERE id='" +id+ "'";
        }
        ResultSet rs = conn.executeQuery(sql);
        try {
            while (rs.next()) {
                studentForm1 = new StudentForm();
                studentForm1.setID(rs.getString(1));
                studentForm1.setName(rs.getString(2));
                studentForm1.setPwd(rs.getString(3));
                studentForm1.setSex(rs.getString(4));
                studentForm1.setJoinTime(java.text.DateFormat.getDateTimeInstance().parse(rs.getString(5)));
                studentForm1.setQuestion(rs.getString(6));
                studentForm1.setAnswer(rs.getString(7));
                studentForm1.setProfession(rs.getString(8));
                studentForm1.setCardNo(rs.getString(9));
                studentList.add(studentForm1);
            }
        } catch (Exception ex) {}
        return studentList;
    }
    //�޸Ŀ�������
    public int update(StudentForm s){
        String sql="UPDATE tb_student SET pwd='"+s.getPwd()+"',sex='"+s.getSex()+"',question='"+s.getQuestion()+"',answer='"+s.getAnswer()+"',profession='"+s.getProfession()+"' where ID='"+s.getID()+"'";
        int ret=conn.executeUpdate(sql);
        System.out.println("�޸Ŀ�������ʱ��SQL��"+sql);
        conn.close();
        return ret;
    }
//�һ����루��һ����
    public StudentForm seekPwd1(StudentForm s){
    	String sql="SELECT * FROM tb_student WHERE ID='"+s.getID()+"'";
        ResultSet rs = conn.executeQuery(sql);
            try {
                if (rs.next()) {
                    s.setID(rs.getString(1));
                    s.setQuestion(rs.getString(6));
                }else{
                s.setID("");
                }
            }catch(Exception e){
            	System.out.println("�һ����루��һ�������ֵĴ�����Ϣ��"+e.getMessage());
            }
            return s;
    }
//  �һ����루�ڶ�����
    public StudentForm seekPwd2(StudentForm s){
    	String sql="SELECT * FROM tb_student WHERE ID='"+s.getID()+"'";
    	System.out.println("SQL"+sql);
        ResultSet rs = conn.executeQuery(sql);
        try {
            if (rs.next()) {
                	String ID=rs.getString(1);
                	String pwd=rs.getString(3);
                	String answer=rs.getString(7);
                	if(answer.equals(s.getAnswer())){
                		s.setID(ID);
                		s.setPwd(pwd);
                		System.out.println("���룺"+pwd);
                	}else{
                		s.setID("");
                	}
                }
            }catch(Exception e){
            	System.out.println("�һ����루�ڶ��������ֵĴ�����Ϣ��"+e.getMessage());
            }
            return s;
    }
//    ɾ������
        public int delete(StudentForm studentForm) {
        	int flag=0;
        	String[] delId=studentForm.getDelIdArray();
        	if (delId.length>0){
        		String id="'";
        		for(int i=0;i<delId.length;i++){
        			id=id+delId[i]+"','";
        		}
        		id=id.substring(0,id.length()-2);
                String sql = "DELETE FROM tb_student where id in (" + id +")";
                System.out.println("ɾ��ʱ��SQL��"+sql);
                flag = conn.executeUpdate(sql);
                conn.close();
        	}else{
        		flag=0;
        	}
            return flag;
        }
}
