package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import com.hibernate.model.CourseStuInfo;
import com.hibernate.model.SystemCourseCode;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class SourceInfoList extends MultiActionController{
	private DAOSupport dao = null;
	
	public DAOSupport getDao() {
		return dao;
	}
	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}	
	public ModelAndView LinkSourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		SystemClassInfo classInfo = 
		List listClass = dao.QueryObject("From SystemClassInfo");
		System.out.println("class:" + listClass);
		return new ModelAndView("docuview/doc_stusource_list", "classlist",listClass);
	}
	public ModelAndView ClassSourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List listClass = dao.QueryObject("From SystemClassInfo");
		System.out.println("class:" + listClass);
		
		List courseList = dao.QueryObject("from SystemCourseCode");				
		Vector vname = new Vector();
		vname.addElement("ѧ������");
		Iterator iterator = courseList.iterator();
		int index = 0;
		while(iterator.hasNext()){
			SystemCourseCode courseObject = (SystemCourseCode)iterator.next();	
			vname.addElement(courseObject.getSubject());
			index ++;
		}
        List courseListObject = dao.QueryObject("FROM CourseStuInfo ");
        System.out.println("courseListObject:" + courseListObject);
        Object[] courseArray = courseListObject.toArray();
        int count = courseArray.length;        
        java.util.Collection collection = new java.util.ArrayList();
        int modcount = count / index;
        for(int i = 0 ; i < modcount; i++){
        	Vector vdata = new Vector();
        	CourseStuInfo coursename = (CourseStuInfo)courseArray[i * index];
        	vdata.addElement(coursename.getDocuStuInfo().getName());        	
        	for(int j = 0 ; j < index ; j++){
        		CourseStuInfo course = (CourseStuInfo)courseArray[i * index + j];
        		vdata.addElement(course.getGrade());
        	}
        	collection.add(vdata);
        }       
        Map map = new HashMap();
        map.put("clname", listClass);
        map.put("tname", vname);
        map.put("cdata", collection);
        return new ModelAndView("docuview/doc_stusource_list", "map",map);		
	}	
}
