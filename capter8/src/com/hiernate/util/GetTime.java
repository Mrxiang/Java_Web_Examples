package com.hiernate.util;      	//�����ౣ����com.hibernate.util����
import java.text.DateFormat;		//����java.text.DateFormat��
import java.text.ParseException;	//����java.text.ParseException��
import java.text.SimpleDateFormat;	//����java.text.SimpleDateFormat��
import java.util.Calendar;			//����java.util.Calendar��
import java.util.Date;				//����java.util.Date��
public class GetTime {
	
//	�ж�date1�Ƿ���date2֮ǰ
	public static boolean isDateBefore(String date1,String date) {
		boolean b = true;									//���ݸ÷����ķ���ֵ���ñ���
		DateFormat df = DateFormat.getDateTimeInstance();  //���ʱ���ʽ��ΪϵͳĬ�ϵĸ�ʽ
    try {
		  b=df.parse(date1).before(df.parse(date));        //�ж�date1�Ƿ���date2֮ǰ
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return b;
	}
	//��д���ϵͳ���ڵķ�����
	public static Date getDate(){								//��Date����Ϊ����ֵ����getDate()����
		Date dateU = new Date();                                  //����Date�����
		java.sql.Date date= new java.sql.Date(dateU.getTime());   //getTime()�����ɵõ���ǰϵͳ������
	    return date;
	}
	//��д������ں�ʱ��ķ�����
	public static String  getDateTime(){						//�÷�������ֵΪString����
		SimpleDateFormat format;                                //simpleDateFormat��ʹ�ÿ���ѡ���κ��û����������-ʱ���ʽ��ģʽ
		Date date = null;
		Calendar myDate = Calendar.getInstance();               //Calendar �ķ��� getInstance���Ի�ô����͵�һ��ͨ�õĶ���
		myDate.setTime(new Date());                   //ʹ�ø����� Date ���ô� Calendar ��ʱ��
		date = myDate.getTime();                                //����һ����ʾ�� Calendar ʱ��ֵ������Ԫ�����ڵĺ���ƫ�������� Date ����
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //����ʱ���ʽΪ���ꡢ�¡��ա�ʱ���֡���
		String strRtn = format.format(date);                    //�������� Date ��ʽ��Ϊ����/ʱ���ַ��������������ֵ���������� String
		return strRtn;
	}
}
