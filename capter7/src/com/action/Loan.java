package com.action;

import com.actionForm.LoanForm;
import com.dao.InstorageDAO;
import com.dao.OutStorageDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Loan extends Action {
    private OutStorageDAO outStorageDAO = null;
    public Loan() {
        outStorageDAO = new OutStorageDAO();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equals("") || action == null) {
            request.setAttribute("error", "���Ĳ�������");
            return mapping.findForward("error");
        } else if (action.equals("loanaddquery")) {
            return loanadd_request(mapping, form, request, response);
        } else if (action.equals("loanadd")) {
            return loanadd(mapping, form, request, response);
        }else if(action.equals("approveloan")){
            return approveloan(mapping, form, request, response);
        }else if(action.equals("backloan")){
            return backloan(mapping, form, request, response);
        }else{
            request.setAttribute("error", "���Ĳ�������");
            return mapping.findForward("error");
        }
    }

//��ѯ����е�������Ϣ
    public ActionForward loanadd_request(ActionMapping mapping,
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

//���ʽ��
    public ActionForward loanadd(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        LoanForm loanForm = (LoanForm) form;
        int rtn = outStorageDAO.loanAdd(loanForm);
        if (rtn == 1) {
            return mapping.findForward("loanAddok");
        } else {
            request.setAttribute("error", "���ʽ����Ϣ���ʧ�ܣ�");
            return mapping.findForward("error");
        }
    }
//������
   public ActionForward approveloan(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
       HttpSession httpsession = request.getSession();
       if(httpsession.getAttribute("username")==null){
           request.setAttribute("error","���ȵ�¼��");
           return mapping.findForward("error");
       }
       int id=Integer.valueOf(request.getParameter("id"));
       int rtn=outStorageDAO.approveloanAdd(id,request);
       if(rtn==1){
           return mapping.findForward("loanApproveAddok");
       }else if(rtn==2){
           request.setAttribute("error","����������㣬������ɽ����ˣ�");
           return mapping.findForward("error");
       }else{
           request.setAttribute("error","�����˲���ʧ�ܣ�");
           return mapping.findForward("error");
        }
   }
   //����黹
      public ActionForward backloan(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
          HttpSession httpsession = request.getSession();
          if(httpsession.getAttribute("username")==null){
              request.setAttribute("error","���ȵ�¼��");
              return mapping.findForward("error");
          }
          int id=Integer.valueOf(request.getParameter("id"));
          InstorageDAO inStorageDAO=new InstorageDAO();
          int rtn=inStorageDAO.backloan(id,request);
          if(rtn==1){
              return mapping.findForward("loanbackAddok");
          }else{
              request.setAttribute("error","����黹����ʧ�ܣ�");
              return mapping.findForward("error");
           }
   }
}
