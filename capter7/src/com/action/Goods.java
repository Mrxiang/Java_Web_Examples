package com.action;

import com.actionForm.GoodsForm;
import com.dao.GoodsDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Goods extends Action {
    private GoodsDAO goodsDAO=null;
    public Goods(){
        goodsDAO=new GoodsDAO();
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String action=request.getParameter("action");

        if(action.equals("") || action==null){
            request.setAttribute("error","���Ĳ�������");
            return mapping.findForward("error");
        }else if(action.equals("goodsRequest")){
            return goodsQuery(mapping,form,request,response);
        }else if(action.equals("goodsadd")){
            return goodsAdd(mapping,form,request,response);
        }else if(action.equals("goodsMquery")){
            return goodsModiQuery(mapping,form,request,response);
        }else if(action.equals("goodsmodify")){
            return goodsModify(mapping,form,request,response);
        }else if(action.equals("goodsdel")){
            return goodsdel(mapping,form,request,response);
        }
        request.setAttribute("error","���Ĳ�������");
        return mapping.findForward("error");
    }
    public ActionForward goodsQuery(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        String str=null;
        int ifdel=0;   //��ȡδ��ɾ��������
        request.setAttribute("goodslist0",goodsDAO.query(str,ifdel));
        ifdel=1;   //��ȡ�ѱ�ɾ��������
        request.setAttribute("goodslist1",goodsDAO.query(str,ifdel));
        return mapping.findForward("goodsQuery");
    }
    //���������Ϣ
    public ActionForward goodsAdd(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        GoodsForm goodsForm = (GoodsForm) form;
//        goodsForm.setName(chStr.toChinese(goodsForm.getName()));
//        goodsForm.setSpec(chStr.toChinese(goodsForm.getSpec()));
//        goodsForm.setUnit(chStr.toChinese(goodsForm.getUnit()));
//        goodsForm.setProducer(chStr.toChinese(goodsForm.getProducer()));
        int rtn=goodsDAO.insert(goodsForm);
        if (rtn == 1) {
            return mapping.findForward("goodsaddok");
        } else if (rtn == 2) {
            request.setAttribute("error", "��������Ϣ�Ѿ���ӣ�");
            return mapping.findForward("error");
        } else {
            request.setAttribute("error", "������Ϣ���ʧ�ܣ�");
            return mapping.findForward("error");
        }
    }
    //�޸�������Ϣ�Ĳ�ѯ
    public ActionForward goodsModiQuery(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        int id=Integer.parseInt(request.getParameter("id"));
        request.setAttribute("goodsForm",goodsDAO.query(id));
        return mapping.findForward("goodsModiQuery");
    }
    //�޸�������Ϣ
    public ActionForward goodsModify(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        GoodsForm goodsForm = (GoodsForm) form;
        int rtn=goodsDAO.update(goodsForm);
        if (rtn == 1) {
            return mapping.findForward("goodsmodifyok");
        }else {
            request.setAttribute("error", "������Ϣ�޸�ʧ�ܣ�");
            return mapping.findForward("error");
        }
    }
    //ɾ��/�ָ�������Ϣ
    public ActionForward goodsdel(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
    int id=Integer.parseInt(request.getParameter("id"));
    byte val=Byte.parseByte(request.getParameter("val"));
    int rtn=goodsDAO.del(id,val);
    if(rtn==1){
         return mapping.findForward("goodsdelok");
    }else{
        request.setAttribute("error", "������Ϣɾ��/�ָ�ʧ�ܣ�");
        return mapping.findForward("error");
    }
}

}
