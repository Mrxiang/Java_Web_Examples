package com.action;

import com.actionForm.BranchForm;
import com.dao.BranchDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Branch extends Action {
    BranchDAO branchDAO=null;
    public Branch() {
        branchDAO = new BranchDAO();
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        BranchForm branchForm = (BranchForm) form;
            String action=request.getParameter("action");
            if(action.equals("") || action==null){
                request.setAttribute("err","���Ĳ�������");
                return mapping.findForward("error");
            }else if(action.equals("branchQuery")){
                return branchQuery(mapping,form,request,response);
            }else if(action.equals("branchdel")){
                return branchDel(mapping, form, request, response);
            }else if(action.equals("branchadd")){
                return branchAdd(mapping, form, request, response);
            }
            return mapping.findForward("error");
        }

        public ActionForward branchQuery(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
            String str = "";
            request.setAttribute("branchList", branchDAO.query(str));
            return mapping.findForward("branchQuery");
        }
        public ActionForward branchDel(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
            int id=Integer.parseInt(request.getParameter("id"));
            int rtn=branchDAO.del(id);
            if(rtn==0){
                request.setAttribute("error","�ò�����Ϣ����ɾ����");
                return mapping.findForward("error");
            }else{
                return mapping.findForward("branchdelok");
            }
        }
        public ActionForward branchAdd(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
           BranchForm branchForm = (BranchForm) form;
           int rtn=branchDAO.insert(branchForm);
           if(rtn==2){
               request.setAttribute("error","�ò�����Ϣ�Ѿ����ڣ�");
               return mapping.findForward("error");
           }else if(rtn==1){
               return mapping.findForward("branchaddok");
           }else{
               request.setAttribute("error","������Ϣ���ʧ�ܣ�");
               return mapping.findForward("error");
           }
       }
}
