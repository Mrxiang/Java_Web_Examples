package com.yxq.dao;

import com.yxq.model.CreatePage;
import com.yxq.model.InfoSingle;
import com.yxq.tools.DoString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class OpDB {
	private DB mydb;
	public OpDB(){
		mydb=new DB();	
	}
	
	public TreeMap OpGetListBox(String sql,Object[] params){
		TreeMap typeMap=new TreeMap();
		mydb.doPstm(sql, params);
		try {
			ResultSet rs=mydb.getRs();
			if(rs!=null){
				while(rs.next()){					
					Integer sign=Integer.valueOf(rs.getInt("type_sign"));
					String intro=rs.getString("type_intro");
					typeMap.put(sign,intro);					
				}
				rs.close();
			}
		} catch (SQLException e) {			
			System.out.println("OpGetListBox()");
			e.printStackTrace();
		}finally{
			mydb.closed();			
		}
		return typeMap;	
	}
	
	public List OpListShow(String sql,Object[] params){
		List onelist=new ArrayList();
		mydb.doPstm(sql, params);
		try{
			ResultSet rs=mydb.getRs();
			if(rs!=null){
				while(rs.next()){
					InfoSingle infoSingle=new InfoSingle();
					infoSingle.setId(rs.getInt("id"));
					infoSingle.setInfoType(rs.getInt("info_type"));
					infoSingle.setInfoTitle(rs.getString("info_title"));
					infoSingle.setInfoContent(rs.getString("info_content"));
					infoSingle.setInfoLinkman(rs.getString("info_linkman"));
					infoSingle.setInfoPhone(rs.getString("info_phone"));
					infoSingle.setInfoEmail(rs.getString("info_email"));
					infoSingle.setInfoDate(DoString.dateTimeChange(rs.getTimestamp("info_date")));
					infoSingle.setInfoState(rs.getString("info_state"));
					infoSingle.setInfoPayfor(rs.getString("info_payfor"));
					onelist.add(infoSingle);					
				}
			}
			rs.close();
		}catch (Exception e){
			System.out.println("OplistShow");
			e.printStackTrace();
		}finally{
			mydb.closed();			
		}
		return onelist;		
	}
	
	public InfoSingle OpSingleShow(String sql,Object[] params){
		InfoSingle infoSingle=null;
		mydb.doPstm(sql, params);       
		try{
		    ResultSet rs=mydb.getRs();
			if(rs!=null&&rs.next()){
				infoSingle=new InfoSingle();
				infoSingle.setId(rs.getInt("id"));
				infoSingle.setInfoType(rs.getInt("info_type"));
				infoSingle.setInfoTitle(rs.getString("info_title"));
				infoSingle.setInfoContent(rs.getString("info_content"));
				infoSingle.setInfoLinkman(rs.getString("info_linkman"));
				infoSingle.setInfoPhone(rs.getString("info_phone"));
				infoSingle.setInfoEmail(rs.getString("info_email"));
				infoSingle.setInfoDate(DoString.dateTimeChange(rs.getTimestamp("info_date")));
				infoSingle.setInfoState(rs.getString("info_state"));
				infoSingle.setInfoPayfor(rs.getString("info_payfor"));				
				rs.close();				
			}
		}catch(Exception e){
			System.out.println("OpSingleShow");
			e.printStackTrace();			
		}finally{
			mydb.closed();
		}
		return infoSingle;
	}
	
	public int OpUpdate(String sql,Object[] params){		
		int i=-1;
		mydb.doPstm(sql, params);
		try{
			i=mydb.getCount();			
		}catch(SQLException e){
			System.out.println("OpUpdate");
			e.printStackTrace();
		}finally{
			mydb.closed();
		}
		return i;
	}
	
	public CreatePage OpCreatePage(String sqlall,Object[] params,int perR,String strCurrentP,String gowhich){
		CreatePage page=new CreatePage();
		page.setPerR(perR);
		if(sqlall!=null&&!sqlall.equals("")){
			DB mydb=new DB();
			mydb.doPstm(sqlall,params);			
			try {
				ResultSet rs=mydb.getRs();				
				if(rs!=null&&rs.next()){
					rs.last();					
					page.setAllR(rs.getRow());
					page.setAllP();
					page.setCurrentP(strCurrentP);
					page.setPageInfo();
					page.setPageLink(gowhich);
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("OpDB.java/OpCreatePage()");
				e.printStackTrace();
			}finally{				
				mydb.closed();
			}
		}		
		return page;
	}
	
	public boolean LogOn(String sql,Object[] params){
		mydb.doPstm(sql, params);
		try {
			ResultSet rs=mydb.getRs();
			boolean mark=(rs==null||!rs.next()?false:true);
			rs.close();
			return mark;			
		} catch (SQLException e) {
			System.out.println("LogOn");
			e.printStackTrace();
			return false;
		}
		finally{
			mydb.closed();
		}
	}
}
