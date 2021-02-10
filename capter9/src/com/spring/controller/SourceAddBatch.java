package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import com.hibernate.model.CourseStuInfo;
import com.hibernate.model.DocuStuInfo;
import com.hibernate.model.SystemCourseCode;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SourceAddBatch extends AbstractController{
	private DAOSupport dao;
	private String successView;
	public DAOSupport getDao() {
		return dao;
	}

	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse rep) throws Exception {
		req.setCharacterEncoding("GB2312");
		String stuId,examType;
		Date examDate;
		stuId = req.getParameter("stuId");
		examType = req.getParameter("examType");
		examDate = Date.valueOf(req.getParameter("examDate"));
		String findSql = "From CourseStuInfo csi where csi.docuStuInfo.stuId = '" + stuId + 
		                  "' and examType = '" + examType + "' and examDate = '" + examDate + "'";
		List findList = dao.QueryObject(findSql);
		System.out.println("findList: " + findList);
		System.out.println("findList.isEmpty(): " + findList.isEmpty());
		if(!findList.isEmpty()){
			Map model = new HashMap();
			model.put("message", "��ѧ���ĳɼ������Ѿ����ڣ�����������!!!");
			List list = dao.QueryObject("from SystemCourseCode");			
			model.put("list", list);
			return new ModelAndView("/sourceview/doc_stusource_input", model);
			//return new ModelAndView(new RedirectView("regCjInfo.htm"),model);
		}
		
		List list = dao.QueryObject("from SystemCourseCode");
		Object[] obj = list.toArray();
		
		//DocuStuInfo stuinfo = new DocuStuInfo();
		List stuList = dao.QueryObject("from DocuStuInfo where stu_id = '" + req.getParameter("stuId") +"'");
		System.out.println("sddds:" + stuList);
		DocuStuInfo stuinfo = (DocuStuInfo)stuList.get(0);
		Set MM = stuinfo.getCourseStu();
		//Set MM = new HashSet();
		//stuinfo.setStuId(req.getParameter("stuId"));
		for(int i = 0 ; i < obj.length ; i ++){
			SystemCourseCode courseObject = (SystemCourseCode)obj[i];
			CourseStuInfo regForm = new CourseStuInfo();
			regForm.setDocuStuInfo(stuinfo);
			regForm.setSystemCourseCode(courseObject);
			regForm.setCzy(req.getParameter("czy"));
			regForm.setExamType(req.getParameter("examType"));
			regForm.setExamDate(Date.valueOf(req.getParameter("examDate")));
			regForm.setGrade(Float.valueOf(req.getParameter("grade" + courseObject.getCode())));
			//stuinfo.getCourseStu().add(regForm);
			MM.add(regForm);
			//dao.InsertOrUpdate(regForm);
		}
		//stuinfo.setCourseStu(MM);
		dao.InsertOrUpdate(stuinfo);
		
		
		Map model = new HashMap();
		model.put("message", "�ɼ�¼��ɹ��������¼��!!!");
		List listcode = dao.QueryObject("from SystemCourseCode");
		model.put("list", listcode);
		return new ModelAndView("/sourceview/doc_stusource_input", model);
		/*if (submit==null||submit.length()<=0){			
			if(stuId != null){
				List list1 = dao.QueryObject("from DocuStuInfo where stuId='"+ stuId + "'");
				DocuStuInfo stuinfo = (DocuStuInfo)list1.get(0);
				model.put("stuName", stuinfo.getName());
			}
			
			model.put("list", list);			
		}else{
			CourseStuInfo regForm = new CourseStuInfo();
			DocuStuInfo insertStuInfo=new DocuStuInfo();
			insertStuInfo.setStuId(req.getParameter("stuId"));
			regForm.setDocuStuInfo(insertStuInfo);
			SystemCourseCode insertCourseCode=new SystemCourseCode();
			insertCourseCode.setCode(req.getParameter("code"));
			regForm.setSystemCourseCode(insertCourseCode);	
			model.put("form", regForm);
			dao.InsertOrUpdate(regForm);
			model.put("message", "�ɼ�¼��ɹ��������¼�롣");	
		}	*/
		
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

}
