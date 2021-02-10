package com.jwy.dao;

import com.jwy.dto.Course;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Jingweiyu 
 */
public interface ICourseDao { 
	public void insert(Course course);		//�����¿γ�
	public void updateIsFinish(Integer id);	//���γ�����Ϊѧ��������
	public List<Object> findBySearch(Map<String,String> map);//����רҵ���ơ��γ����ƣ��ڿν�ʦ����
	public Course findByID(Integer id);		//���տγ̱�Ų���
	public List<Course> findByAll();		//��ѯ���пγ�
	public List<Object[]> findByStat(Map<String,String> map);	//����������ѯ�γ�
	public List<Object[]> findSelectStu(Integer id);	//��ѯ������ѡ��˿γ̵�ѧ��
}
