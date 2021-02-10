package com.wgh.action;

import java.util.*;

import javax.servlet.http.*;
import org.apache.struts.action.*;

import antlr.collections.List;

import com.wgh.actionForm.StudentForm;
import com.wgh.actionForm.TaoTiForm;
import com.wgh.dao.StudentDAO;

public class Student extends Action {
	private StudentDAO studentDAO = null;

	public Student() {
		this.studentDAO = new StudentDAO();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		System.out.println("��ȡ�Ĳ�ѯ�ַ�����" + action);
		if ("studentQuery".equals(action)) {
			return studentQuery(mapping, form, request, response);
		} else if ("login".equals(action)) {
			return studentLogin(mapping, form, request, response);
		} else if ("studentAdd".equals(action)) {
			return studentAdd(mapping, form, request, response);
		} else if ("studentDel".equals(action)) {
			return studentDel(mapping, form, request, response);
		} else if ("modifyQuery".equals(action)) {
			return modifyQuery(mapping, form, request, response);
		} else if ("studentModify".equals(action)) {
			return studentModify(mapping, form, request, response);
		}else if("seekPwd1".equals(action)){
			return seekPwd1(mapping,form,request,response);
		}else if("seekPwd2".equals(action)){
			return seekPwd2(mapping,form,request,response);
		}else{
			request.setAttribute("error","���Ĳ�������");		//��������Ϣ���浽error��
			return mapping.findForward("error");				//ת����ʾ������Ϣ��ҳ��
		}
	}

	// ���������֤
	public ActionForward studentLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		StudentForm studentForm = (StudentForm) form;
		int ret = studentDAO.checkStudent(studentForm);
		System.out.print("��֤���ret��ֵ:" + ret);
		if (ret == 2) {
			request.setAttribute("error", "������Ŀ���׼��֤������������");
			return mapping.findForward("error");
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("student", studentForm.getID());
			return mapping.findForward("studentLoginok");
		}
	}

	// ��ѯ������Ϣ
	private ActionForward studentQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("studentQuery", studentDAO.query(null));
		return mapping.findForward("studentQuery");
	}

	// ��ӿ���ע����Ϣ
	private ActionForward studentAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		StudentForm studentForm = (StudentForm) form;
		String ret = studentDAO.insert(studentForm);
		System.out.println("����ֵret��" + ret);
		if (ret.equals("re")) {
			request.setAttribute("error", "���Ѿ�ע�ᣬֱ�ӵ�¼���ɣ�");
			return mapping.findForward("error");
		} else if(ret.equals("miss")){
			request.setAttribute("error", "ע��ʧ�ܣ�");
			return mapping.findForward("error");
		}else{
			request.setAttribute("ret",ret);
			return mapping.findForward("studentAdd");
		}
	}

	// �޸Ŀ�����Ϣʱ��ѯ
	private ActionForward modifyQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("��ȡ��ID��"+request.getParameter("ID"));
		StudentForm studentForm=(StudentForm)(studentDAO.query(request.getParameter("ID")).get(0));
		System.out.println("��Bean�л�ȡ��ID��"+studentForm.getID());
		request.setAttribute("modifyQuery", studentForm);
		return mapping.findForward("modifyQuery");
	}
//�һ����루��һ����
	private ActionForward seekPwd1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		StudentForm studentForm = (StudentForm) form;
		StudentForm s=studentDAO.seekPwd1(studentForm);
		request.setAttribute("seekPwd2", s);
		if(s.getID().equals("")){
			request.setAttribute("error", "�������׼��֤�Ų����ڣ�");
			return mapping.findForward("error");
		}else{
			return mapping.findForward("seekPwd1");	
		}
	}
//	�һ����루�ڶ�����
	private ActionForward seekPwd2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		StudentForm studentForm = (StudentForm) form;
		StudentForm s=studentDAO.seekPwd2(studentForm);
		request.setAttribute("seekPwd3", s);
		if(s.getID().equals("")){
			request.setAttribute("error", "�������������ʾ����Ĵ𰸲���ȷ��");
			return mapping.findForward("error");
		}else{
			return mapping.findForward("seekPwd2");
		}
	}
	// �޸Ŀ�����Ϣ
	private ActionForward studentModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		StudentForm studentForm = (StudentForm) form;
		int ret = studentDAO.update(studentForm);
		if (ret == 0) {
			request.setAttribute("error", "�޸Ŀ�����Ϣʧ�ܣ�");
			return mapping.findForward("error");
		} else {
			return mapping.findForward("studentModify");
		}
	}

	// ɾ��������Ϣ
	private ActionForward studentDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		StudentForm studentForm = (StudentForm) form;
		int ret = studentDAO.delete(studentForm);
		if (ret == 0) {
			request.setAttribute("error", "ɾ��������Ϣʧ�ܣ�");
			return mapping.findForward("error");
		} else {
			return mapping.findForward("studentDel");
		}
	}
}