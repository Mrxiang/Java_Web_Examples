package com.yxq.model;

public class CreatePage {
	private int CurrentP;
	private int AllP;
	private int AllR;
	private int PerR;
	private String PageLink;
	private String PageInfo;
	
	public CreatePage(){
		CurrentP=1;
		AllP=1;
		AllR=0;
		PerR=3;
		PageLink="";
		PageInfo="";
	}
	
	/* */
	public void setPerR(int PerR){
		this.PerR=PerR;
	}
	
	/* */
	public void setAllR(int AllR){
		this.AllR=AllR;
	}
	/* */
	public void setAllP(){
		AllP=(AllR%PerR==0)?(AllR/PerR):(AllR/PerR+1);
	}
	
	/* */
	public void setCurrentP(String currentP) {		
		if(currentP==null||currentP.equals(""))
			currentP="1";
		try{
			CurrentP=Integer.parseInt(currentP);
		}catch(NumberFormatException e){
			CurrentP=1;
			e.printStackTrace();
		}
		if(CurrentP<1)
			CurrentP=1;
		if(CurrentP>AllP)
			CurrentP=AllP;		
	}

	/* */
	public void setPageInfo(){
		if(AllP>1){
			PageInfo="<table border='0' cellpadding='3'><tr><td>";
			PageInfo+="per page show "+PerR+"/"+AllR+" records! ";
			PageInfo+="cur page "+CurrentP+"/"+AllP+" pages!";
			PageInfo+="</td></tr></table>";			
		}				
	}
	
    /* */
	public void setPageLink(String gowhich){
		if(gowhich==null)
			gowhich="";
		if(gowhich.indexOf("?")>=0)
			gowhich+="&";
		else
			gowhich+="?";
		if(AllP>1){
			PageLink="<table border='0' cellpadding='3'><tr><td>";
			if(CurrentP>1){
				PageLink+="<a href='"+gowhich+"showpage=1'> first page </a>&nbsp;";
				PageLink+="<a href='"+gowhich+"showpage="+(CurrentP-1)+"'>pre</a>&nbsp;";
			}
			if(CurrentP<AllP){
				PageLink+="<a href='"+gowhich+"showpage="+(CurrentP+1)+"'>next</a>&nbsp;";
				PageLink+="<a href='"+gowhich+"showpage="+AllP+"'>last</a>";
			}
			PageLink+="</td></tr></table>";			
		}		
	}
	
	/* */
	public int getPerR(){
		return PerR;
	}
	
	/* */
	public int getAllR() {
		return AllR;
	}	
	
    /* */
	public int getAllP() {		
		return AllP;
	}

	/* */
	public int getCurrentP() {
		return CurrentP;
	}
	
	/* */
	public String getPageInfo(){
		return PageInfo;
	}

	/* */
	public String getPageLink(){
		return PageLink;
	}
}
