package com.action;

import com.actionForm.IfForm;
import com.dao.QueryDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class IfDeal extends Action {
    QueryDAO queryDAO = null;
    public IfDeal() {
        queryDAO = new QueryDAO();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String action = request.getParameter("action");
        if (action.equals("stockQuery")) {
            return stockIf(mapping, form, request, response);
        } else if (action.equals("stockDetail")) {
            return stockDetail(mapping, form, request, response);
        } else if (action.equals("inStockQuery")) {
            return inStockIf(mapping, form, request, response);
        } else if (action.equals("inStorageSQuery")) {
            return inStorageSQuery(mapping, form, request, response);
        } else if (action.equals("loanApproveQuery")) {
            return loanApproveQuery(mapping, form, request, response);
        } else if (action.equals("loanBackQuery")) {
            return loanBackQuery(mapping, form, request, response);
        } else if (action.equals("loanQuery")) {
            return loanQuery(mapping, form, request, response);
        } else if (action.equals("loanDetail")) {
            return loanDetail(mapping, form, request, response);
        } else if (action.equals("damageQuery")) {
            return damageQuery(mapping, form, request, response);
        } else if (action.equals("getuseQuery")) {
            return getuseQuery(mapping, form, request, response);
        } else if (action.equals("getuseTotal")) {
            return getuseTotal(mapping, form, request, response);
        } else if (action.equals("damageTotal")) {
            return damageTotal(mapping, form, request, response);
        } else if (action.equals("branchTotal")) {
            return branchTotal(mapping, form, request, response);
        } else {
            return mapping.findForward("error");
        }
    }

    public ActionForward stockIf(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        // StockMainViewForm stockMVF = new StockMainViewForm();
        String tablename = "StockMainViewForm";
        request.setAttribute("stockQueryM",
                             queryDAO.stockQuery(ifForm, tablename, 3));
        return mapping.findForward("stockQuery");
    }

//�ɹ���ϸ��Ϣ��ѯ
    public ActionForward stockDetail(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("stockDetail", queryDAO.stockDetail(id));
        return mapping.findForward("stockDetail");
    }

    //�������ˡ�ʱ���õĲ�ѯ
    public ActionForward inStockIf(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        String tablename = "StockMainViewForm";
        request.setAttribute("stockQueryM",
                             queryDAO.stockQuery(ifForm, tablename, 0));
        return mapping.findForward("inStockQuery");
    }

    //������ѯ��ʱ���õĲ�ѯ
    public ActionForward inStorageSQuery(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        String tablename = "InStorageForm";
        request.setAttribute("inStorageForm",
                             queryDAO.stockQuery(ifForm, tablename, 3));
        return mapping.findForward("inStorageSQuery");
    }

    //�������ˡ�ʱ���õĲ�ѯ
    public ActionForward loanApproveQuery(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        String tablename = "LoanForm";
        request.setAttribute("loanApproveQuery",
                             queryDAO.stockQuery(ifForm, tablename, 0));
        return mapping.findForward("loanApproveQuery");
    }

    //������黹��ʱ���õĲ�ѯ
    public ActionForward loanBackQuery(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        String tablename = "LoanForm";
        request.setAttribute("loanBackQuery",
                             queryDAO.stockQuery(ifForm, tablename, 1));
        return mapping.findForward("loanBackQuery");
    }

    //�����ѯ
    public ActionForward loanQuery(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        String tablename = "LoanForm";
        request.setAttribute("loanQuery",
                             queryDAO.stockQuery(ifForm, tablename, 3));
        return mapping.findForward("loanQuery");
    }

    //�����ϸ��Ϣ��ѯ
    public ActionForward loanDetail(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("loanDetail", queryDAO.loanDetail(id));
        return mapping.findForward("loanDetail");
    }

    //���ű����ѯ
    public ActionForward damageQuery(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        request.setAttribute("damageQuery",
                             queryDAO.damageQuery(ifForm));
        return mapping.findForward("damageQuery");
    }

    //�������ò�ѯ
    public ActionForward getuseQuery(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        request.setAttribute("getuseQuery",
                             queryDAO.getuseQuery(ifForm));
        return mapping.findForward("getuseQuery");
    }

    //�������û��ܲ�ѯ
    public ActionForward getuseTotal(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        request.setAttribute("getuseTotal",
                             queryDAO.getusedamageTotal(ifForm, "GetUseForm",
                "number"));
        return mapping.findForward("getuseTotal");
    }

    //���ű�����ܲ�ѯ
    public ActionForward damageTotal(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        request.setAttribute("damageTotal",
                             queryDAO.getusedamageTotal(ifForm, "DamageForm",
                "damagenum"));
        return mapping.findForward("damageTotal");
    }

    //���Ż��ܲ�ѯ
    public ActionForward branchTotal(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        IfForm ifForm = (IfForm) form;
        request.setAttribute("sDate", ifForm.getSdate());
        request.setAttribute("eDate", ifForm.getEdate());
        request.setAttribute("branchTotal",
                             queryDAO.branchTotal(ifForm));
        return mapping.findForward("branchTotal");
    }
}
