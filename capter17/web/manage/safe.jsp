<%@ page contentType="text/html; charset=gb2312" language="java"%>
<%if (session.getAttribute("manager")==null){
	out.println("<script language='javascript'>alert('����û�е�¼!');window.location.href='../index.jsp';</script>");
}%>
