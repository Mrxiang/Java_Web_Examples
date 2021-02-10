package com.action;

import com.actionForm.UserForm;
import com.dao.UserDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class User extends Action {
    private UserDAO userDAO=null;
    public User(){
        userDAO=new UserDAO();
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String action = request.getParameter("action");
        if(action.equals("userQuery")){
            return userQuery(mapping,form,request,response);
        }else if(action.equals("userdel")){
            return userDel(mapping, form, request, response);
        }else if(action.equals("useradd")){
            return userAdd(mapping, form, request, response);
        }else if(action.equals("userMQuery")){
            return userQModify(mapping, form, request, response);
        }else if(action.equals("userModify")){
            return userModify(mapping, form, request, response);
        }else if(action.equals("pwsModify")){
            return pwsModify(mapping,form,request,response);
        }else if(action.equals("login")){
            return login(mapping,form,request,response);
        }else{
            request.setAttribute("err","���Ĳ�������");
            return mapping.findForward("error");
        }
    }
    //��ѯ�û���Ϣ
    public ActionForward userQuery(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String str = "";
        request.setAttribute("userList", userDAO.query(str));
        return mapping.findForward("userQuery");
    }
    //ɾ���û���Ϣ
    public ActionForward userDel(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {


        int id=Integer.parseInt(request.getParameter("id"));
        int rtn=userDAO.del(id);
        if(rtn==0){
            request.setAttribute("error","���û���Ϣ����ɾ����");
            return mapping.findForward("error");
        }else{
            return mapping.findForward("userdelok");
        }
    }
    //����û���Ϣ
    public ActionForward userAdd(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) {
       UserForm userForm = (UserForm) form;
//       ChStr chStr=new ChStr();
//       userForm.setName(chStr.toChinese(userForm.getName()));
//       userForm.setPwd(chStr.toChinese(userForm.getPwd()));
       System.out.println("���л�ȡ��Instorage:"+userForm.getSetInstorage());
       int rtn=userDAO.insert(userForm);
       if(rtn==2){
           request.setAttribute("error","���û���Ϣ�Ѿ����ڣ�");
           return mapping.findForward("error");
       }else if(rtn==1){
           return mapping.findForward("useraddok");
       }else{
           request.setAttribute("error","�û���Ϣ���ʧ�ܣ�");
           return mapping.findForward("error");
       }
   }
   //�޸��û�Ȩ��ʱ�Ĳ�ѯ
   public ActionForward userQModify(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
       int id=Integer.parseInt(request.getParameter("id"));
       request.setAttribute("userList",userDAO.query(id));
       return mapping.findForward("userQModify");
   }
   //�޸��û�����
   public ActionForward pwsModify(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
       UserForm userForm = (UserForm) form;
//       ChStr chStr=new ChStr();
//       userForm.setName(chStr.toChinese(userForm.getName()));
//       userForm.setPwd(chStr.toChinese(userForm.getPwd()));
       userForm.setName(userForm.getName());
       userForm.setPwd(userForm.getPwd());
       int flag=1;    //ֵΪ1ʱ�޸�����
       int rtn=userDAO.Modify(userForm,flag);
       if(rtn==1){
           return mapping.findForward("usermodipwdok");
       }else{
           request.setAttribute("error","�û������޸�ʧ�ܣ�");
           return mapping.findForward("error");
       }
   }
   //�޸��û���Ϣ
   public ActionForward userModify(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) {

       UserForm userForm = (UserForm) form;
      // userForm.setName(userForm.getName());  ����˾�ȡ��ע�ͣ���Ҫ�����޸��ĵ���wgh�������Ҫɾ����ע��
//       ChStr chStr=new ChStr();
//       userForm.setName(chStr.toChinese(userForm.getName()));
       int flag=2;  // ֵΪ2ʱ�޸�Ȩ��
       int rtn=userDAO.Modify(userForm,flag);
       if(rtn==1){
           return mapping.findForward("usermodiok");
       }else{
           request.setAttribute("error","�޸��û�Ȩ����Ϣʧ�ܣ�");
           return mapping.findForward("error");
       }
    }
    //�û���¼
    public ActionForward login(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        UserForm userForm = (UserForm) form;
        String rtn=userDAO.login(userForm,request);
        if(rtn.equals("ok")){
            return mapping.findForward("loginok");
        }else{
            request.setAttribute("error",rtn);
            return mapping.findForward("error");
        }
    }

}
