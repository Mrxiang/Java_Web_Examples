package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import com.hibernate.model.Login;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchUser extends AbstractController {
	private DAOSupport dao;

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("gb2312");
		List list;
		Map model = new HashMap();
		String condition = request.getParameter("condition");
		String conditionContent = request.getParameter("conditionContent");
		if (conditionContent == null)
			conditionContent = "";
		if (condition == null)
			condition = "name";
		String id = request.getParameter("id");
		if (id != null) {
			int mid = Integer.parseInt(id);
			Login user = (Login) dao.getObject(Login.class, mid);
			if (!user.getUsername().equals("mr"))
				dao.DeleteObject(user);
			else
				model.put("message", "����ɾ����ʼ�û���");
		}
		list = dao.QueryObject("from Login where " + condition + " like '"
				+ conditionContent + "%'");
		model.put("list", list);
		model.put("condition", condition);
		model.put("conditionContent", conditionContent);
		return new ModelAndView("userManager/userSearch", model);
	}

	public DAOSupport getDao() {
		return dao;
	}

	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}
}
