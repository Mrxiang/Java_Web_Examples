package com.action;

import com.actionForm.StockGoodsForm;
import com.dao.InstorageDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cart extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String action = request.getParameter("action");
        System.out.println("Action:" + action);
        if (action.equals("add")) { //�������ɹ�����ťʱִ�еĲ���
            return add(mapping, form, request, response);
        } else if (action.equals("remove")) {
            return remove(mapping, form, request, response);
        } else if(action.equals("clear")){
            return clear(mapping, form, request, response);
        }else {
            request.setAttribute("err", "���Ĳ�������");
            return mapping.findForward("error");
        }
    }
//���ָ��������Ϣ
    public ActionForward add(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        InstorageDAO instorageDAO = new InstorageDAO();
        StockGoodsForm stockGoodsForm = (StockGoodsForm) form;
        instorageDAO.cart_add(stockGoodsForm, request);
        return mapping.findForward("add");
    }
    //��ȥָ��������Ϣ
    public ActionForward remove(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        InstorageDAO instorageDAO = new InstorageDAO();
        int id=Integer.parseInt(request.getParameter("removeid"));
        instorageDAO.cart_remove(id, request);
        return mapping.findForward("add");
    }
    //��ձ���������Ϣ��Session
    public ActionForward clear(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        InstorageDAO instorageDAO = new InstorageDAO();
        instorageDAO.cart_clear(request);
        return mapping.findForward("add");
    }
}
