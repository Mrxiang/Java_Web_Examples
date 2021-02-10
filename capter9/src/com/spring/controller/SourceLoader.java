package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import com.hibernate.model.CourseStuInfo;
import com.hibernate.model.DocuStuInfo;
import com.hibernate.model.SystemClassInfo;
import com.hibernate.model.SystemCourseCode;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class SourceLoader extends MultiActionController {
	private DAOSupport dao;
	public ModelAndView reg(HttpServletRequest req, HttpServletResponse res) {
		List list = dao.QueryObject("from SystemCourseCode");
		Map model = new HashMap();
		model.put("list", list);
		String stuIdstr = req.getParameter("stuId");
		if (stuIdstr != null) {
			List list1 = dao.QueryObject("from DocuStuInfo where stuId='"
					+ stuIdstr + "'");
			if (list1 != null&&list1.size()>0) {
				DocuStuInfo stuInfo = (DocuStuInfo) list1.get(0);
				model.put("stuInfo", stuInfo);
			}
		}
		return new ModelAndView("/sourceview/doc_stusource_input", model);
	}
	public ModelAndView search(HttpServletRequest req, HttpServletResponse res) {
		System.out.println(33333);
		try {
			req.setCharacterEncoding("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String condition = req.getParameter("condition");
		String conditionContent = req.getParameter("conditionContent");
		List list = null;
		System.out.println(condition);
		if (condition != null && conditionContent != null) {
			System.out.println("ccc"+condition);
			if (condition.equals("�������"))
				condition = "examType";
			else if (condition.equals("��Ŀ����"))
				condition = "systemCourseCode.subject";
			else if (condition.equals("ѧ������"))
				condition = "docuStuInfo.name";
			else if (condition.equals("ѧ�����"))
				condition = "docuStuInfo.stuId";
			else
				condition = "docuStuInfo.name";
			list = dao.QueryObject("from CourseStuInfo where " + condition
					+ " like '" + conditionContent + "%'");
		} else {
			list = dao.QueryObject("from CourseStuInfo");
		}
		Map model = new HashMap();
		model.put("list", list);
		return new ModelAndView("/sourceview/doc_stusource_search", model);
	}
	/**
	 * �༶���Գɼ���ѯ
	 */
	public ModelAndView ClassSourceList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String classid = request.getParameter("selectmc");
		if (classid == null)
			classid = "%";
		String examType = request.getParameter("typeexam");
		if (examType == null || examType.length() <= 0) {
			examType = "%";
		} else {
			examType = new String(request.getParameter("typeexam").getBytes(
					"iso-8859-1"), "gb2312");
		}
		String examDate = request.getParameter("dateexam");

		List listClass = dao.QueryObject("From SystemClassInfo");
		List courseList = dao.QueryObject("from SystemCourseCode");

		Vector vname = new Vector();
		vname.addElement("ѧ������");
		vname.addElement("�������");
		vname.addElement("��������");
		Iterator iterator = courseList.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			SystemCourseCode courseObject = (SystemCourseCode) iterator.next();
			vname.addElement(courseObject.getSubject());
			index++;
		}
		vname.addElement("�ܷ���");
		String sqlSelect = null;
		if (examDate == null || examDate.length() <= 0) {
			examDate = "%";
			sqlSelect = "SELECT * FROM course_stu_info WHERE (SUBSTRING(stu_id, 1, 6) LIKE '"
					+ classid
					+ "' AND exam_type LIKE '"
					+ examType+ "')";
		} else {
			sqlSelect = "SELECT * FROM course_stu_info WHERE (SUBSTRING(stu_id, 1, 6) LIKE '"
					+ classid
					+ "' AND exam_type LIKE '"
					+ examType
					+ "' AND exam_date = '" + examDate + "')";
		}
		List courseListObject = dao.QueryObjectFromSql(sqlSelect,
				"course_stu_info", new CourseStuInfo());
		System.out.println(courseListObject);
		Object[] courseArray = courseListObject.toArray();
		int count = courseArray.length;
		java.util.Collection collection = new java.util.ArrayList();
		int modcount = count / index;

		for (int i = 0; i < modcount; i++) {
			Vector vdata = new Vector();
			CourseStuInfo coursename = (CourseStuInfo) courseArray[i * index];
			vdata.addElement(coursename.getDocuStuInfo().getName());
			vdata.addElement(coursename.getExamType());
			vdata.addElement(coursename.getExamDate());

			float gradesum = 0.0f;
			for (int j = 0; j < index; j++) {
				CourseStuInfo course = (CourseStuInfo) courseArray[i * index
						+ j];
				vdata.addElement(course.getGrade());
				gradesum = gradesum
						+ Float.parseFloat(String.valueOf(course.getGrade()));
			}
			vdata.addElement(Float.valueOf(gradesum));
			gradesum = 0.0f;
			collection.add(vdata);
		}
		classid = request.getParameter("selectmc");
		Map map = new HashMap();
		map.put("clname", listClass);
		map.put("tname", vname);
		map.put("cdata", collection);
		map.put("oldmc", classid);
		return new ModelAndView("sourceview/doc_stusource_class_gather", "map",
				map);
	}
	/**
	 * �꼶���Գɼ�ͳ��
	 */
	public ModelAndView GradeSourceList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gradeid = request.getParameter("selectmc");
		String examType = request.getParameter("typeexam");
		String examDate = request.getParameter("dateexam");
		List listGrade = dao.QueryObject("From SystemGradeCode");
		List courseList = dao.QueryObject("From SystemCourseCode");
		Map map = new HashMap();
		map.put("grname", listGrade);

		Vector vname = new Vector();
		vname.addElement("�༶����");
		vname.addElement("ѧ������");
		vname.addElement("�������");
		vname.addElement("��������");
		map.put("tname", vname);
		if (examType != null && !examType.equals("")) {
			examType = new String(request.getParameter("typeexam").getBytes(
					"iso-8859-1"), "gb2312");
		} else {
			map.put("message", "�������ѯ����");
			return new ModelAndView("sourceview/doc_stusource_grade_gather",
					"map", map);
		}
		Iterator iterator = courseList.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			SystemCourseCode courseObject = (SystemCourseCode) iterator.next();
			vname.addElement(courseObject.getSubject());
			index++;
		}
		vname.addElement("�ܷ���");
		String sqlSelect = null;
		if (examType == null || examType.equals("")) {
			sqlSelect = "select code,left(stu_id,6) as �༶����,str(sum(grade)/count(stu_id),10,2) as �༶�ɼ�,count(stu_id) as �༶���� from course_stu_info group by code,left(stu_id,6) ";
		} else {
			System.out.println("no null");
			sqlSelect = "select code,left(stu_id,6) as �༶����,str(sum(grade)/count(stu_id),10,2) as �༶�ɼ�,count(stu_id) as �༶���� from course_stu_info where left(stu_id,2)='"+gradeid+"' and exam_type = '"
					+ examType
					+ "' and exam_date = '"
					+ examDate
					+ "' group by code,left(stu_id,6)";
		}

		List courseListObject = dao.QueryObjectFromSql(sqlSelect);

		/*
		 * Iterator it = courseListObject.iterator(); while(it.hasNext()){
		 * Object[] results = (Object[])it.next();
		 * 
		 * System.out.println(results); for(int i = 0 ; i < results.length ;
		 * i++){ System.out.println("������: " + results[i]); } }
		 */
		Object[] courseArray = courseListObject.toArray();
		int count = courseArray.length;
		java.util.Collection collection = new java.util.ArrayList();
		int modcount = count / index;
		for (int i = 0; i < modcount; i++) {
			Vector vdata = new Vector();
			Object[] results = (Object[]) courseArray[i * index];
			String classid = String.valueOf(results[1]);
			List classObjectList = dao
					.QueryObject("From SystemClassInfo where classid = '"
							+ classid + "'");
			SystemClassInfo classobj = (SystemClassInfo) classObjectList.get(0);
			vdata.addElement(classobj.getClassmc());
			vdata.addElement(results[3]);
			vdata.addElement(examType);
			vdata.addElement(examDate);

			float gradesum = 0.00f;
			for (int j = 0; j < index; j++) {
				Object[] course = (Object[]) courseArray[i * index + j];
				vdata.addElement(course[2]);
				gradesum = gradesum
						+ Float.parseFloat(String.valueOf(course[2])); // �ܷ���
			}
			java.text.DecimalFormat formatsum = new java.text.DecimalFormat(
					"#.00");
			vdata.addElement(formatsum.format(gradesum));
			gradesum = 0.0f;
			collection.add(vdata);
		}
		gradeid = request.getParameter("selectmc");
		if (examDate == null || examType == null) {
			map.put("cdata", null);
		} else {
			map.put("cdata", collection);
		}
		map.put("oldmc", gradeid);
		map.put("examType", examType);
		map.put("examDate", examDate);
		return new ModelAndView("sourceview/doc_stusource_grade_gather", "map",
				map);
	}
	public DAOSupport getDao() {
		return dao;
	}

	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}

}
