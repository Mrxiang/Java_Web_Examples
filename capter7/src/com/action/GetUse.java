package com.action;

import com.actionForm.GetUseForm;
import com.dao.OutStorageDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetUse extends Action {
    private OutStorageDAO outStorageDAO = null;
    public GetUse() {
        outStorageDAO = new OutStorageDAO();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equals("") || action == null) {
            request.setAttribute("error", "���Ĳ�������");
            return mapping.findForward("error");
        } else if (action.equals("getuseaddquery")) {
            return getuseadd_request(mapping, form, request, response);
        } else if (action.equals("getuseadd")) {//loanaddquery
            return getuseadd(mapping, form, request, response);
        }
        request.setAttribute("error", "���Ĳ�������");
        return mapping.findForward("error");
    }

//��ѯ����е�������Ϣ
    public ActionForward getuseadd_request(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        int goodsid = 0;
        if (request.getParameter("id") != null) {
            goodsid = Integer.parseInt(request.getParameter("id"));
        }
        request.setAttribute("id", goodsid);
        request.setAttribute("GoodsStorage", outStorageDAO.storage_query()); //��ȡ������Ϣ
        return mapping.findForward("selStorageGoods");
    }

//��������
    public ActionForward getuseadd(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        GetUseForm getUseForm = (GetUseForm) form;
        int rtn = outStorageDAO.getuseAdd(getUseForm);
        if (rtn == 1) {
            return mapping.findForward("getUseAddok");
        } else {
            request.setAttribute("error", "����������Ϣ���ʧ�ܣ�");
            return mapping.findForward("error");
        }
    }

}
