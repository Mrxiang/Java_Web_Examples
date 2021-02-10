package com.wgh.action;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.wgh.actionForm.LessonForm;
import com.wgh.actionForm.MoreSelect;
import com.wgh.actionForm.QuestionsForm;
import com.wgh.actionForm.TaoTiForm;
import com.wgh.core.ChStr;
import com.wgh.dao.LessonDAO;
import com.wgh.dao.StartExamDAO;
import com.wgh.dao.TaoTiDAO;
import java.util.*;

public class StartExam extends Action {
	private StartExamDAO startExamDAO = null;
	ChStr chStr=new ChStr();

	public StartExam() {
		this.startExamDAO = new StartExamDAO();
	}
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		//System.out.println("��ȡ�Ĳ�ѯ�ַ�����" + action);
		if ("startExam".equals(action)) {
			return startExam(mapping, form, request, response);
		}else if("submitTestPaper".equals(action)){
			return submitTestPaper(mapping,form,request,response);
		}else if("showStartTime".equals(action)){//��ʾ���Լ�ʱ
			return showStartTime(mapping,form,request,response);
		}else if("showRemainTime".equals(action)){//��ʾ����ʱ��
			return showRemainTime(mapping,form,request,response);
		}else{
			request.setAttribute("error", "����ʧ�ܣ�");
			return mapping.findForward("error");
		}
	}
	//��ʱ
	private ActionForward showStartTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String startTime=session.getAttribute("startTime").toString();
		long a=Long.parseLong(startTime);
		long b=new Date().getTime();
		int h=(int)Math.abs((b-a)/3600000);
		String hour=chStr.formatNO(h,2);
		int m=(int)(b-a)%3600000/60000;
		String minute=chStr.formatNO(m,2);
		int s=(int)((b-a)%3600000)%60000/1000;
		String second=chStr.formatNO(s,2);
		String time=hour+":"+minute+":"+second;
		request.setAttribute("showStartTime",time);
		return mapping.findForward("showStartTime");
	}
	//����ʣ��ʱ��
	private ActionForward showRemainTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String startTime=session.getAttribute("startTime").toString();
		long a=Long.parseLong(startTime);
		long b=new Date().getTime();
		long r=20*60000-(b-a-1000);
		int h=(int)Math.abs(r/3600000);
		String hour=chStr.formatNO(h,2);
		int m=(int)(r)%3600000/60000;
		String minute=chStr.formatNO(m,2);
		int s=(int)((r)%3600000)%60000/1000;
		String second=chStr.formatNO(s,2);
		String time=hour+":"+minute+":"+second;
		request.setAttribute("showRemainTime",time);
		return mapping.findForward("showRemainTime");
	}

	// ��ȡ������Ŀ��Ϣ
	private ActionForward startExam(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//System.out.println("�γ�ID��"+lessonForm.getID()+lessonForm.getName());
		HttpSession session = request.getSession();
		if(session.getAttribute("student")==null || session.getAttribute("student").equals("")){
			return mapping.findForward("dealNull");	//ת��ǰ̨��¼ҳ��
		}else{
			String student=session.getAttribute("student").toString();					//׼��֤��
			if(session.getAttribute("lessonID")==null || session.getAttribute("lessonID").equals("")){
				return mapping.findForward("dealNull");	//ת��ǰ̨��¼ҳ��
			}else{
				int lessonID=Integer.parseInt(session.getAttribute("lessonID").toString());	//�γ�ID
				//�����ȡ����
				int questions=startExamDAO.randomGetQuestion(lessonID);
				//�տ�ʼ����ʱ���濼�Խ��
				int ret=startExamDAO.startSaveResult(student,lessonID);
	//			System.out.println("�տ�ʼ����ʱ���濼�Խ����"+ret);
				List singleQue=(List)startExamDAO.queryExam(questions,0);
				QuestionsForm q=(QuestionsForm)form;
				q.setSize(singleQue.size());
				request.setAttribute("singleQue",singleQue);			//��ȡ��ѡ��
				List moreQue=(List)startExamDAO.queryExam(questions,1);	//��ȡ��ѡ��
				q.setMoreSize(moreQue.size());	
				request.setAttribute("moreQue",moreQue);
				session.setAttribute("startTime",new Date().getTime());
				return mapping.findForward("testPaper");
			}
		}
	}
//�ύ�Ծ�
	private ActionForward submitTestPaper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		QuestionsForm q=(QuestionsForm)form;
		String rightAnswer="";
		float singleMark=0;
		float moreMark=0;
		/****************************ͳ�Ƶ�ѡ��ĵ÷�**************************************/
		String[] single=q.getAnswerArrS();
		int[] singleId=q.getIdArrS();
		//System.out.println(q.getID());
		System.out.println("���ݵĳ��ȣ�"+single.length);
		float markS=40/(single.length);
		for(int i=0;i<single.length;i++){
			//����getRightAnswer()������ȡ��ȷ��
			rightAnswer=startExamDAO.getRightAnswer(singleId[i]);
			System.out.println("��ѡ���飺"+i+"********ID��"+singleId[i]+"********"+single[i]+"****��ȷ��"+rightAnswer);
			if(rightAnswer.equals(single[i])){
				singleMark=singleMark+markS;	//�ۼӵ�ѡ��ķ���
			}			
		}
		System.out.println("��ѡ��÷֣�"+singleMark);
		/*********************************************************************************/
		/****************************ͳ�ƶ�ѡ��ĵ÷�**************************************/
		MoreSelect[] more=q.getMoreSelect();
		System.out.println("��ѡ����ĳ��ȣ�"+more.length);
		float markM=60/(more.length);
		String str="";
		for(int i=0;i<more.length;i++){
			String[] ans=more[i].getAnswerArr();
			int[] moreId=q.getIdArrM();
			rightAnswer=startExamDAO.getRightAnswer(moreId[i]);
			System.out.println("��ѡ���飺"+i+"********ID��"+moreId[i]+"********"+more[i]+"****��ȷ��"+rightAnswer);
			for(int j=0;j<ans.length;j++){
				if(ans[j]!=null) str=str+ans[j]+",";
    		}
    		if(str.length()>1){
    			str=str.substring(0,str.length()-1);
    		}
    		
			System.out.println("��ȡ�Ķ�ѡ��𰸣�"+str);
			if(rightAnswer.equals(str)){
				moreMark=moreMark+markM;	//�ۼӶ�ѡ��ķ���
			}	
			str="";
		}
		System.out.println("��ѡ��÷֣�"+moreMark);
		/*********************************************************************************/
		HttpSession session = request.getSession();
		String student=session.getAttribute("student").toString();
		int lessonID=Integer.parseInt(session.getAttribute("lessonID").toString());	//�γ�ID
		int ret=startExamDAO.saveResult(student,lessonID,(int)Math.round(singleMark),(int)Math.round(moreMark));
		if(ret>0){
			request.setAttribute("submitTestPaperok", "�Ծ����ύ�������ο��Եĳɼ�Ϊ��"+(Math.round(singleMark)+Math.round(moreMark))+"�֣�");
			return mapping.findForward("submitTestPaperok");
		}else{
			return mapping.findForward("dealNull");
		}
	}

}
