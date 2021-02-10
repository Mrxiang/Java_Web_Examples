package com.wgh.core;

import java.text.NumberFormat;

public class ChStr {
    public String formatNO(int str, int length) {
        float ver = Float.parseFloat(System.getProperty(
                "java.specification.version"));  //��ȡJDK�İ汾
        String laststr = "";
        if (ver < 1.5) {  //JDK1.5���°汾ִ�е����
            try {
                NumberFormat formater = NumberFormat.getNumberInstance();	//������ʵ����NumberFormat��һ��ʵ��
                formater.setMinimumIntegerDigits(length);					//ָ��
                laststr = formater.format(str).toString().replace(",", "");
            } catch (Exception e) {
                System.out.println("��ʽ���ַ���ʱ�Ĵ�����Ϣ��" + e.getMessage());	//����쳣��Ϣ
            }
        } else {  //JDK1.5�汾ִ�е����
        	Integer[] arr=new Integer[1];		//��������ʼ������arr
			arr[0]=new Integer(str);	//��Ҫ��ʽ��������str��ֵ������arr�ĵ�һ��Ԫ��
            laststr = String.format("%0"+length+"d", arr);
        }
        return laststr;
    }
}
