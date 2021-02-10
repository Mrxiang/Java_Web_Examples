package com.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetTime {
    //��ȡϵͳ����
    public Date getDate(){
        Date dateU = new Date();
        java.sql.Date date = new java.sql.Date(dateU.getTime());
        System.out.println(date);
        return date;
    }
    //��ȡϵͳ���ں�ʱ��
    public String getDateTime(){
        SimpleDateFormat format;
        Date date=null;
        Calendar myDate=Calendar.getInstance();
        myDate.setTime(new Date());
        date=myDate.getTime();
        format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String rtnStr=format.format(date);
//        String rtnStr=new java.util.Date().toLocaleString();
        return rtnStr;
    }
    //��ʽ������ʱ��Ϊ����-��-�� ʱ���֣��롱�ĸ�ʽ
    public String formatTime(Date date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str=format.format(date);
        return str;
    }
}
