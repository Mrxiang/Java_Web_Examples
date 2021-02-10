package com.jwy.action;

import com.jwy.dao.ISpecialtyDao;
import com.jwy.dto.Specialty;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * @author Jingweiyu
 */
public class SpecialtyAction extends DispatchAction {

	private ISpecialtyDao specialtyDao;
	public void setSpecialtyDao(ISpecialtyDao specialtyDao) {
		this.specialtyDao = specialtyDao;
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm specialtyForm = (DynaActionForm) form;
		Specialty specialty = new Specialty();
		specialty.setIsFinish(false);          // �����Ƿ��ҵ��Ĭ��Ϊ��
		specialty.setName(specialtyForm.getString("name"));
		specialty.setEnterYear(specialtyForm.getString("enterYear"));
		specialty.setLangthYear(specialtyForm.getString("langthYear"));
		specialtyDao.insert(specialty);
		List list = specialtyDao.findByAll();
		System.out.println(list);
		request.setAttribute("list", list);
		return mapping.findForward("showSpecialty");
	}

	/**
	 * ��רҵ����Ϊ�ѽ�ҵ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateIsFinish(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		specialtyDao.updateIsFinish(id);
		return findAll(mapping, form, request, response);
	}

	public ActionForward findAll(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
		List list = specialtyDao.findByAll();
		System.out.println(list);
		request.setAttribute("list", list);
		return mapping.findForward("showSpecialty");
	}
}