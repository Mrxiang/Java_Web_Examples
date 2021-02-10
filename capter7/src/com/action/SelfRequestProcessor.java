package com.action;

import org.apache.struts.action.RequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class SelfRequestProcessor extends RequestProcessor  {
    public SelfRequestProcessor() {
    }
    protected boolean processPreprocess(HttpServletRequest request,HttpServletResponse response){
        try {
            request.setCharacterEncoding("gb2312");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
