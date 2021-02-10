package com.jwy.action;

import com.jwy.dao.ISpecialtyDao;
import com.jwy.dao.IStuUserDao;
import com.jwy.dao.IUserLoginDao;
import com.jwy.dto.Specialty;
import com.jwy.dto.StuUser;
import com.jwy.dto.UserLogin;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserLoginAction extends Action {

	private IUserLoginDao userLoginDao;
	private IStuUserDao stuUserDao;
	private ISpecialtyDao specialtyDao;

	public void setSpecialtyDao(ISpecialtyDao specialtyDao) {
		this.specialtyDao = specialtyDao;
	}

	public void setStuUserDao(IStuUserDao stuUserDao) {
		this.stuUserDao = stuUserDao;
	}

	public void setUserLoginDao(IUserLoginDao userLoginDao) {
		this.userLoginDao = userLoginDao;
	}

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm userLoginForm = (DynaActionForm) form;
		UserLogin userLogin = userLoginDao.findbyNameAndPwd(userLoginForm
				.getString("loginName"), userLoginForm.getString("pwd"));
		if (userLogin == null) {
			request.setAttribute("error", "�û���¼ʧ�ܣ��û��������벻��ȷ��");
			return mapping.findForward("index");
		} else { 
			request.getSession().setAttribute("loginName",
					userLogin.getLoginName());
			request.getSession().setAttribute("id", userLogin.getId());
			if (userLogin.getType().equals("1")) {
				System.out.println("����Ա��¼");
				return mapping.findForward("manager");
			} else {
				// �ж�ѧ����û����д��������Ϣ
				StuUser stuUser = stuUserDao.findById(userLogin.getId());
				if (stuUser == null) {
					System.out.println("û��д��������Ϣ");
					List<Specialty> list = specialtyDao.findStuByAll();
					request.setAttribute("list", list);
					return mapping.findForward("addStuInfo");
				} else { 
					Specialty specialty = specialtyDao.findById(stuUser
							.getSpecialtyId());
					request.setAttribute("specialty", specialty);
					request.setAttribute("stuUser", stuUser);
					System.out.println("��д��������Ϣ");
					return mapping.findForward("welcome");
				}
			}
		}
	}
}