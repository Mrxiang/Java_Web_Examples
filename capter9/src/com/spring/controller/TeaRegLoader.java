package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class TeaRegLoader implements Controller {
	private DAOSupport dao;

	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		List list = dao.QueryObject("from SystemSpecialtyCode");
		return new ModelAndView("/docuview/doc_tea_info_reg", "list", list);
	}

	public DAOSupport getDao() {
		return dao;
	}

	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}

}
