package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import com.hibernate.model.Login;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserController extends SimpleFormController {
	private DAOSupport dao;

	public AddUserController() {
		setCommandClass(Login.class);
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		HttpSession session = request.getSession();
		Login manager = (Login) session.getAttribute("loginUser");
		Map model = new HashMap();
		if (manager != null&&manager.getUsername().equals("mr")) {
			Login newUser = (Login) command;
			List list = dao.QueryObject("from Login where username='"
					+ newUser.getUsername() + "'");
			if(list.size()>0)
				model.put("message", "��¼�����Ѿ����ڣ�������û�����");
			else{
				dao.InsertOrUpdate(newUser);
				model.put("message", "�û�\""+newUser.getName()+"\"��ӳɹ���");				
			}
		}
		else
			model.put("message", "Ȩ�޲�����δ��¼���뷵�ص�½��");
		return new ModelAndView("userManager/addUser",model);
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		request.setCharacterEncoding("gb2312");
	}

	public DAOSupport getDao() {
		return dao;
	}

	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}

}
