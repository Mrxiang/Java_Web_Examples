package com.action;

import com.actionForm.StockMainForm;
import com.dao.GoodsDAO;
import com.dao.InstorageDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Instorage extends Action {
    InstorageDAO instorageDAO=null;
    public Instorage(){
        instorageDAO=new InstorageDAO();
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String action=request.getParameter("action");
        System.out.println("Action:"+action);
        if(action.equals("changeGoods")){  //ѡ��������Ϣʱִ�еĲ���
            return changeGoods(mapping,form,request,response);
        }else if(action.equals("stockadd")){
            return stockadd(mapping,form,request,response);
        }else if(action.equals("eligible")){
            return eligible(mapping,form,request,response);
        }else if(action.equals("uneligible")){
            return uneligible(mapping,form,request,response);
        }else{
            request.setAttribute("err","���Ĳ�������");
            return mapping.findForward("error");
        }

    }

    //ѡ��ָ��������Ϣ��Ĳ�ѯ
    public ActionForward changeGoods(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        GoodsDAO goodsDAO=new GoodsDAO();
        int goodsid=1;
        if(request.getParameter("id")!=null){
            goodsid=Integer.parseInt(request.getParameter("id"));
        }
        request.setAttribute("selGoods",goodsDAO.query(goodsid));   //��ȡѡ���������Ϣ
        return mapping.findForward("stock");
    }
    //����ɹ�����Ϣ
    public ActionForward stockadd(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        StockMainForm stockMainForm=(StockMainForm)form;
        int rtn=instorageDAO.stockadd(stockMainForm,request);
        if(rtn==1){
            return mapping.findForward("stockaddok");
        }else{
            request.setAttribute("error","�ɹ�����Ϣ����ʧ�ܣ�");
            return mapping.findForward("error");
        }
    }
    //������
    public ActionForward eligible(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        HttpSession httpsession = request.getSession();
        if(httpsession.getAttribute("username")==null){
            request.setAttribute("error","���ȵ�¼��");
            return mapping.findForward("error");
        }
        int id=Integer.valueOf(request.getParameter("id"));
        int rtn=instorageDAO.eligibleAdd(id,request);
        if(rtn==1){
            return mapping.findForward("eligibleAddok");
        }else{
            request.setAttribute("error","���������ʧ�ܣ�");
            return mapping.findForward("error");
        }
    }
    //��˲��ϸ���
    public ActionForward uneligible(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        HttpSession httpsession = request.getSession();
        if(httpsession.getAttribute("username")==null){
            request.setAttribute("error","���ȵ�¼��");
            return mapping.findForward("error");
        }
        int id=Integer.valueOf(request.getParameter("id"));
        int rtn=instorageDAO.uneligibleAdd(id,request);
        if(rtn==1){
            return mapping.findForward("eligibleAddok");
        }else{
            request.setAttribute("error","��˲��ϸ����ʧ�ܣ�");
            return mapping.findForward("error");
        }
    }
}
