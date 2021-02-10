package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import com.hibernate.model.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeLoader extends MultiActionController {
	private DAOSupport dao;

	public ModelAndView assignCourse(HttpServletRequest req,
			HttpServletResponse res) {// �ν�
		String teaId = req.getParameter("teaId");
		String submit = req.getParameter("Submit");
		List list = dao.QueryObject("from SystemCourseCode");
		Map model = new HashMap();
		model.put("list", list);
		if (teaId != null) {
			list = dao.QueryObject("from DocuTeaInfo where teaId='" + teaId
					+ "'");
			if (list != null && list.size() > 0)
				model.put("teaInfo", list.get(0));
		}
		if (submit != null) {
			String code = req.getParameter("code");
			DocuTeaInfo teaInfo = new DocuTeaInfo(teaId, null);
			SystemCourseCode courseInfo = new SystemCourseCode(code);
			EmplAssignCourse assignCourse = new EmplAssignCourse(teaInfo,
					courseInfo);
			System.out.println(Date.valueOf("2006-8-8"));
			assignCourse.setRkrq(Date.valueOf(req.getParameter("rkrq").trim()));
			dao.InsertOrUpdate(assignCourse);
			model.remove("teaInfo");
			model.put("message", "��ְ�ɹ�");
		}
		return new ModelAndView("employeeview/teaInstateCourses", model);
	}

	public ModelAndView assignCharge(HttpServletRequest req,
			HttpServletResponse res) {// �ΰ�����
		String changeClassId = req.getParameter("changeClassId");
		String changeTeaId = req.getParameter("changeTeaId");
		String submit = req.getParameter("Submit");
		List list = dao.QueryObject("from SystemClassInfo");
		Map model = new HashMap();
		model.put("classInfo", list);
		model.put("rowInfo", list.size()>0?list.get(0):null);
		if (submit == null) {
			if (changeClassId != null) {
				list = dao.QueryObject("from SystemClassInfo where classid='"
						+ changeClassId + "'");
				if (list.size() > 0) {
					model.put("rowInfo", list.get(0));
				}
			}
			if (changeTeaId != null) {
				list = dao.QueryObject("from DocuTeaInfo where teaId='"
						+ changeTeaId + "'");
				if (list.size() > 0)
					model.put("teaInfo", list.get(0));
			}
		} else {
			String teaId = req.getParameter("teaId");
			String classId = req.getParameter("classid");
			DocuTeaInfo teaInfo = new DocuTeaInfo(teaId, null);
			SystemClassInfo classInfo = new SystemClassInfo(classId);
			Date date = Date.valueOf(req.getParameter("rzrq"));
			EmplAssignCharge assignCharge = new EmplAssignCharge(teaInfo,
					classInfo);
			assignCharge.setRzrq(date);
			dao.InsertOrUpdate(assignCharge);
			model.put("message", "��ְ�����γɹ�");
		}
		return new ModelAndView("employeeview/teaInstateCharge", model);
	}

	public ModelAndView searchCharge(HttpServletRequest req,
			HttpServletResponse res) {// ��ѯ������
		List list = null;
		String history=req.getParameter("history");
		String nop=history==null?"=":"<>";
		String lsh = req.getParameter("lsh");
		if (lsh != null) {//��ʦ��ְ
			list = dao.QueryObject("from EmplAssignCharge where lsh='" + lsh
					+ "'");
			if (list.size() > 0) {
				EmplAssignCharge obj = (EmplAssignCharge) list.get(0);
				obj.setLizhiDate(new Date(System.currentTimeMillis()));
				dao.InsertOrUpdate(obj);
			}
		}
		String condition = req.getParameter("condition");
		String conditionContent = req.getParameter("conditionContent");
		if (condition == null) {
			condition = "systemClassInfo.systemSpecialtyCode.name";
		}
		if (conditionContent == null)
			conditionContent = "";
		list = dao.QueryObject("from EmplAssignCharge where " + condition
				+ " like '" + conditionContent + "%' and lizhiDate "+nop+" null");
		Map model = new HashMap();
		model.put("list", list);
		model.put("condition", condition);
		model.put("conditionContent", conditionContent);
		model.put("history", history);
		return new ModelAndView("employeeview/chargeSearch", model);
	}

	public ModelAndView searchTeaCourse(HttpServletRequest req,
			HttpServletResponse res) {// ��ѯ�ον�ʦ
		List list = null;
		String history=req.getParameter("history");
		String nop=history==null?"=":"<>";
		String lsh = req.getParameter("lsh");
		System.out.println(333);
		if (lsh != null) {//��ʦ��ְ
			list = dao.QueryObject("from EmplAssignCourse where lsh='" + lsh
					+ "' and lizhiDate=null");
			if (list.size() > 0) {
				EmplAssignCourse obj = (EmplAssignCourse) list.get(0);
				obj.setLizhiDate(new Date(System.currentTimeMillis()));
				dao.InsertOrUpdate(obj);
			}
		}
		String condition = req.getParameter("condition");
		String conditionContent = req.getParameter("conditionContent");
		if (condition == null) {
			condition = "systemCourseCode.subject";
		}
		if (conditionContent == null)
			conditionContent = "";
		list = dao.QueryObject("from EmplAssignCourse where " + condition
				+ " like '" + conditionContent + "%' and lizhiDate "+nop+" null");
		System.out.println(list.size());

		Map model = new HashMap();
		model.put("list", list);
		model.put("condition", condition);
		model.put("conditionContent", conditionContent);
		model.put("history", history);
		return new ModelAndView("employeeview/teaCourseSearch", model);
	}

	public DAOSupport getDao() {
		return dao;
	}

	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("gb2312");
		return super.handleRequestInternal(request, response);
	}
}
