package com.mr.webiter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.mr.dao.ObjectDao;
import com.mr.model.ArticleInfo;
import com.mr.model.Info;
import com.mr.model.PhotoInfo;
import com.mr.model.ReArticleInfo;
import com.mr.model.UserInfo;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class UserInfoAction extends ActionSupport implements ModelDriven<UserInfo>, ServletRequestAware{

private static final long serialVersionUID = 3496418228419922282L;

private UserInfo userInfo = new UserInfo();

private String hql = "";

private ObjectDao<UserInfo> objectDao = null;

protected HttpServletRequest request;

public UserInfo getModel() {
	return userInfo;
}

public void setServletRequest(HttpServletRequest request) {
	this.request = request;
}

// ??????????
public String friend_delete() {
	String friendsTypes[] = request.getParameterValues("friends");
	if (friendsTypes != null) {
		String account = (String) request.getSession().getAttribute(
				"account");
		hql = "from UserInfo where account = '" + account + "'";
		objectDao = new ObjectDao<UserInfo>();
		UserInfo user = objectDao.queryFrom(hql);
		String date_types = user.getFriendType();
		for (String type : friendsTypes) {
			date_types = com.mr.tools.ValidateExpression.deleteString(
					date_types, type);
		}
		user.setFriendType(date_types);
		objectDao.updateT(user);
	}
	return friend_query();
}

// ???????û?
public String friend_query() {
	String account = "";
//	try {
		account = (String) request.getSession().getAttribute("account");
		//account = new String(account.getBytes("ISO8859_1"), "gb2312");
		System.out.println("AAAAAAAAaa "+account);
//	} catch (UnsupportedEncodingException e) {
//		e.printStackTrace();
//	}
	
	hql = "from UserInfo where account = '" + account + "'";
	System.out.println("HQL????"+hql);
	objectDao = new ObjectDao<UserInfo>();
	userInfo = objectDao.queryFrom(hql);
	String friend = userInfo.getFriendType();
	if (!friend.equals("")) {
		request.setAttribute("friends", friend.split(","));
	}
	String[]  ff = friend.split(",");
	System.out.println("FFFFFFFFFFf "+ff.length);
	return "friend_query";
}

// ??????
public String friend_add() {
	boolean flag = true;
	objectDao = new ObjectDao<UserInfo>();
	String account = (String) request.getSession().getAttribute("account");
	UserInfo user = objectDao.queryFrom("from UserInfo where account = '"
			+ account + "'");
	String friends = user.getFriendType();
	System.out.println("Friends "+friends);
	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(
			"userInfo");
	String account1 = userInfo.getAccount(); // ????????????û???
	if (friends == null || friends.equals("")) {
		user.setFriendType(account1);
	} else {
		String types[] = friends.split(",");
		for (String type1 : types) {
			if (account1.equals(type1)) {
				flag = false;
			}
		}
		user.setFriendType(user.getFriendType() + "," + account1);
	}
	String result = "?ú???????????????????";
	if (flag) {
		objectDao = new ObjectDao<UserInfo>();
		objectDao.updateT(user);
		System.out.println("AFTER     "+ userInfo.getFriendType() + "," + account1);
		result = "???????????";
	}
	request.setAttribute("account1", account1);
	request.setAttribute("sign", "18");
	request.setAttribute("result", result);
	return "operationUser";
}

// ????????????
public String phtType_delete() {
	String phtTypes[] = request.getParameterValues("type");

	if (phtTypes != null) {
		String account = (String) request.getSession().getAttribute(
				"account");
		hql = "from UserInfo where account = '" + account + "'";
		objectDao = new ObjectDao<UserInfo>();
		UserInfo user = objectDao.queryFrom(hql);
		String date_phtType = user.getPicType();
		for (String phtType : phtTypes) {
			date_phtType = com.mr.tools.ValidateExpression.deleteString(
					date_phtType, phtType);
			hql = "from PhotoInfo where author = '" + account
					+ "' and photoType = '" + phtType + "'";
			ObjectDao<PhotoInfo> dao = new ObjectDao<PhotoInfo>();
			List<PhotoInfo> list = dao.queryList(hql);
			for (PhotoInfo photoInfo : list) {
				dao.deleteT(photoInfo);
				String path = request.getRealPath("/"+ photoInfo.getPhotoAddress());
				File file = new File(path);
				if (file.exists()) {
					file.delete();
				}
			}
		}
		user.setPicType(date_phtType);
		objectDao.updateT(user);
	}
	request.setAttribute("sign", "13");
	return "operationUser";
}

// ?????????????
public String phtType_update() {
	String oldType = request.getParameter("oldType");
	String newsType = request.getParameter("newsType");
	String account = (String) request.getSession().getAttribute("account");
	hql = "from UserInfo where account = '" + account + "'";
	objectDao = new ObjectDao<UserInfo>();
	UserInfo user = objectDao.queryFrom(hql);
	String date_phtType = user.getPicType();
	String arrays[] = date_phtType.split(",");
	boolean flag = true;
	for (String array : arrays) {
		if (array.equals(newsType)) {
			flag = false;
		}
	}
	if (flag) {
		if (!newsType.equals("")) {
			date_phtType = com.mr.tools.ValidateExpression.deleteString(
					date_phtType, oldType);
			date_phtType = date_phtType.trim();
			if (date_phtType.equals("")) {
				date_phtType = newsType;
			} else {
				date_phtType = date_phtType + "," + newsType;
			}
			user.setPicType(date_phtType);
			objectDao.updateT(user);
			hql = "from PhotoInfo where author = '" + account
					+ "' and photoType = '" + oldType + "'";
			ObjectDao<PhotoInfo> dao = new ObjectDao<PhotoInfo>();
			List<PhotoInfo> list = dao.queryList(hql);
			for (PhotoInfo photoInfo : list) {
				photoInfo.setPhotoType(newsType);
				System.out.println(newsType);
				dao.updateT(photoInfo);
			}
		}
	}
	request.setAttribute("sign", "14");
	return "operationUser";
}

// ?????????????????
public String phtType_query() {
	boolean flag = true;
	String account = (String) request.getSession().getAttribute("account");
	hql = "from UserInfo where account = '" + account + "'";
	objectDao = new ObjectDao<UserInfo>();
	UserInfo user = objectDao.queryFrom(hql);
	String pht_type = user.getPicType();
	// ??????
	if (!userInfo.getPicType().equals("")) {
		String arrays[] = pht_type.split(",");
		for (String array : arrays) {
			if (array.equals(userInfo.getPicType())) {
				this.addFieldError("pht_type", "????????????????????????????");
				flag = false;
			}
		}
		if (null == pht_type || pht_type.equals("")) {
			user.setPicType(userInfo.getPicType());
		} else {
			user
					.setPicType(user.getPicType() + ","
							+ userInfo.getPicType());
		}
		if (flag) {
			objectDao.updateT(user);
			user = objectDao.queryFrom(hql);
			pht_type = user.getPicType();
		}
	}
	// ??????
	pht_type = pht_type.trim();
	if (!pht_type.equals("")) {
		String pht_types[] = pht_type.split(",");
		request.setAttribute("pht_types", pht_types);
	}
	return SUCCESS;
}

// ?????????µ????????????????µ?????????????
public String forwardAddArticle() {
	String account = (String) request.getSession().getAttribute("account");
	hql = "from UserInfo where account = '" + account + "'";
	objectDao = new ObjectDao<UserInfo>();
	UserInfo userInfo = (UserInfo) objectDao.queryFrom(hql);
	String type = userInfo.getArtType();
	String types[] = type.split(",");
	request.setAttribute("types", types);
	return "forwardAddArticle";
}

// ??????????????
public String artType_update() {
	String oldType = request.getParameter("oldType");
	String newsType = request.getParameter("newsType");
	String account = (String) request.getSession().getAttribute("account");
	hql = "from UserInfo where account = '" + account + "'";
	objectDao = new ObjectDao<UserInfo>();
	UserInfo user = objectDao.queryFrom(hql);
	String date_artType = user.getArtType();
	String arrays[] = date_artType.split(",");
	boolean flag = true;
	for (String array : arrays) {
		if (array.equals(newsType)) {
			flag = false;
		}
	}
	if (flag) {
		if (!newsType.equals("")) {
			date_artType = com.mr.tools.ValidateExpression.deleteString(
					date_artType, oldType);
			date_artType = date_artType.trim();
			if (date_artType.equals("")) {
				date_artType = newsType;
			} else {
				date_artType = date_artType + "," + newsType;
			}
			user.setArtType(date_artType);
			objectDao.updateT(user);
			hql = "from ArticleInfo where author = '" + account
					+ "' and typeName = '" + oldType + "'";
			ObjectDao<ArticleInfo> dao = new ObjectDao<ArticleInfo>();
			List<ArticleInfo> list = dao.queryList(hql);
			for (ArticleInfo articleInfo : list) {
				articleInfo.setTypeName(newsType);
				dao.updateT(articleInfo);
			}
		}
	}
	request.setAttribute("sign", "9");
	return "operationUser";
}

// ?????????????
public String artType_delete() {
	String artTypes[] = request.getParameterValues("type");
	if (artTypes != null) {
		String account = (String) request.getSession().getAttribute("account");//????û????
		hql = "from UserInfo where account = '" + account + "'";//????û??????hql???
		objectDao = new ObjectDao<UserInfo>();
		UserInfo user = objectDao.queryFrom(hql);//?????
		String date_artType = user.getArtType();
		for (String artType : artTypes) {//???????????
			date_artType = com.mr.tools.ValidateExpression.deleteString(date_artType, artType);
			hql = "from ArticleInfo where author = '" + account+ "' and typeName = '" + artType + "'";
			ObjectDao<ArticleInfo> dao = new ObjectDao<ArticleInfo>();
			List<ArticleInfo> list = dao.queryList(hql);
			for (ArticleInfo articleInfo : list) {
				dao.deleteT(articleInfo);//??????????
			}
		}
		user.setArtType(date_artType);
		objectDao.updateT(user);//?????û????
	}
	request.setAttribute("sign", "8");
	return "operationUser";
}

// ??????????????
public String artType_query() {
	boolean flag = true;
	String account = (String) request.getSession().getAttribute("account");
	hql = "from UserInfo where account = '" + account + "'";
	objectDao = new ObjectDao<UserInfo>();
	UserInfo user = objectDao.queryFrom(hql);
	String art_type = user.getArtType();
	// ??????
	if (!userInfo.getArtType().equals("")) {
		hql = "from UserInfo where account = '" + account + "'";
		objectDao = new ObjectDao<UserInfo>();
		user = objectDao.queryFrom(hql);
		art_type = user.getArtType();
		String arrays[] = art_type.split(",");
		for (String array : arrays) {
			if (array.equals(userInfo.getArtType())) {
				this.addFieldError("ary_type", "?????????????????????????????");
				flag = false;
			}
		}
		if (null == art_type || art_type.equals("")) {
			user.setArtType(userInfo.getArtType());
		} else {
			user
					.setArtType(user.getArtType() + ","
							+ userInfo.getArtType());
		}
		if (flag) {
			objectDao.updateT(user);
			user = objectDao.queryFrom(hql);
			art_type = user.getArtType();
		}
	}
	// ??????
	art_type = art_type.trim();
	if (!art_type.equals("")) {
		String art_types[] = art_type.split(",");
		request.setAttribute("art_types", art_types);
	}
	return SUCCESS;
}

// ????û???????????
public void validateUserPassword_update() {
	String oldPassword = request.getParameter("oldPassword");
	if (null == oldPassword || oldPassword.equals("")) {
		this.addFieldError("oldPassword", "???????????????");
	}
	if (userInfo.getPassword().equals("")) {
		this.addFieldError("password", "????????????");
	}
	if (userInfo.getRepassword().equals("")) {
		this.addFieldError("repassword", "??????????????");
	}
	if (!userInfo.getPassword().equals(userInfo.getRepassword())) {
		this.addFieldError("repassword", "??????????????????????£?");
	}

	oldPassword = com.mr.tools.ValidateExpression.encodeMD5(oldPassword);
	String account = (String) request.getSession().getAttribute("account");
	objectDao = new ObjectDao<UserInfo>();
	hql = "from UserInfo where account = '" + account + "'";
	userInfo = objectDao.queryFrom(hql);
	String password = userInfo.getPassword();
	if (!password.equals(oldPassword)) {
		this.addFieldError("oldPassword", "?????????????????");
	}

}

// ????û???????????
public String userPassword_update() {
	String oldPassword = com.mr.tools.ValidateExpression.encodeMD5(request.getParameter("password"));
	userInfo.setPassword(oldPassword);
	String result = "???????????";
	if (objectDao.updateT(userInfo)) {
		result = "???????????";
	}
	request.setAttribute("result", result);
	return SUCCESS;
}

// ????????????
public void validateUser_update() {
	objectDao = new ObjectDao<UserInfo>();
	if (this.userInfo.getRealname().equals("")) {
		this.addFieldError("realname", "?û????????????????");
	}
	if (userInfo.getEmail().equals("")) {
		this.addFieldError("email", "??????????????");
	} else {
		if (!com.mr.tools.ValidateExpression
				.checkemail(userInfo.getEmail())) {
			this.addFieldError("email", "Email?????????????");
		}
	}
	if (userInfo.getAddress().equals("")) {
		this.addFieldError("address", "??????????????");
	}
	if (null == userInfo.getQq()) {
		this.addFieldError("qq", "QQ???????????????????");
	}
	if (null == userInfo.getTel()) {
		this.addFieldError("tel", "???????????????????");
	}
	if (userInfo.getProfession().equals("")) {
		this.addFieldError("profession", "??????????");
	}
	String rand = (String) request.getSession().getAttribute("rand");
	if (!rand.equals(userInfo.getCode())) {
		this.addFieldError("code", "????????????????");
	}
}

// ?????????????
public String user_update() {
	objectDao = new ObjectDao<UserInfo>();
	hql = "from UserInfo where account = '" + userInfo.getAccount() + "'";
	UserInfo user = objectDao.queryFrom(hql);
	user.setRealname(userInfo.getRealname());
	user.setEmail(userInfo.getEmail());
	user.setQq(userInfo.getQq());
	user.setAddress(userInfo.getAddress());
	user.setSex(userInfo.getSex());
	user.setProfession(userInfo.getProfession());
	user.setTel(userInfo.getTel());
	user.setHeadgif(userInfo.getHeadgif());
	user.setSelf(userInfo.getSelf());
	if (objectDao.updateT(user)) {
		return goinUserManager();
	} else {
		request.setAttribute("sign", "5");
		return "operationUser";
	}
}

// ?????û?id????û???????????????????
public String userSelectOne() {	
	String account = "";
	try {
		account = (String) request.getParameter("account");
		account = new String(account.getBytes("ISO8859_1"), "gb2312");		
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	hql = "from UserInfo where account = '" + account + "'";
	objectDao = new ObjectDao<UserInfo>();
	UserInfo userInfo = objectDao.queryFrom(hql);
	request.setAttribute("userInfo", userInfo);
	return "userSelectOne";
}

// ????session?????????????????
public String goinUserManager() {
	if (null == request.getSession().getAttribute("account")) {
		request.setAttribute("sign", "7");
		return "operationUser";
	} else {
		String account = (String) request.getSession().getAttribute(
				"account");
		objectDao = new ObjectDao<UserInfo>();
		hql = "from UserInfo where account = '" + account + "'";
		userInfo = objectDao.queryFrom(hql);
		if (!userInfo.getAccount().equals(account)
				&& null == request.getSession().getAttribute("userInfo")) {
			userInfo.setVistor(userInfo.getVistor() + 1);
			objectDao.updateT(userInfo);
		}

		request.setAttribute("userInfo", userInfo);
		return "goinUserManager";
	}
}

// ?????û???????????????
public String goinUser() {
	String account = request.getParameter("account");
	try {
		account = new String(account.getBytes("ISO8859_1"), "gb2312");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	hql = "from UserInfo where account = '" + account + "'";
	objectDao = new ObjectDao<UserInfo>();
	UserInfo userInfo = objectDao.queryFrom(hql);
	if (userInfo.getFreeze().equals("????")) {
		request.setAttribute("sign", "7");
		return "operationUser";
	} else {
		objectDao = new ObjectDao<UserInfo>();
		if (!userInfo.getAccount().equals(account)) {
			userInfo.setVistor(userInfo.getVistor() + 1);
			objectDao.updateT(userInfo);
		}
		request.getSession().setAttribute("userInfo", userInfo);
	}
	return "goinUser";
}

// ?û???¼???
public void validateCheckUser() {
	if (userInfo.getAccount().equals("")) {
		this.addFieldError("account", "???????û?????");
	}
	if (userInfo.getPassword().equals("")) {
		this.addFieldError("password", "??????????");
	}
	if (userInfo.getCode().equals("")) {
		this.addFieldError("code", "?????????");
	} else {
		String rand = (String) request.getSession().getAttribute("rand");
		if (!rand.equals(userInfo.getCode())) {
			this.addFieldError("code", "????????????????");
		}
	}
	objectDao = new ObjectDao<UserInfo>();
	hql = "from UserInfo where account = '" + userInfo.getAccount() + "'";
	UserInfo user = objectDao.queryFrom(hql);
	if (null == user) {
		this.addFieldError("account", "????????û????????????");
	} else {
		String password = com.mr.tools.ValidateExpression
				.encodeMD5(userInfo.getPassword());
		if (!password.equals(user.getPassword())) {
			this.addFieldError("password", "???????????????");
		}
	}
}

// ?û???¼????
public String checkUser() {
	objectDao = new ObjectDao<UserInfo>();
	hql = "from UserInfo where account = '" + userInfo.getAccount() + "'";
	UserInfo user = objectDao.queryFrom(hql);
	request.setAttribute("sign", "6");
	request.getSession().setAttribute("account", user.getAccount());
	request.getSession().setAttribute("freeze", user.getFreeze());
	return "operationUser";
}

// ?û?????????????
public String updateCommendUser() {
	objectDao = new ObjectDao<UserInfo>();//?????ObjectDao
	Integer id = Integer.valueOf(request.getParameter("id"));//???id????
	hql = "from UserInfo where id = " + id;//????id?????User????
	UserInfo userInfo = objectDao.queryFrom(hql);
	if (userInfo.getFreeze().equals("??")) {//?????û?????
		if (userInfo.getCommend().equals("??")) {//?????û???????
			userInfo.setCommend("??");
		} else if (userInfo.getCommend().equals("??")) {
			userInfo.setCommend("??");
		}
	} else {
		request.setAttribute("result", "???û?û?????????????????");
	}
	boolean flag = objectDao.updateT(userInfo);//????û???????????
	if (flag) {
		Integer pageCounter = Integer.valueOf(request.getParameter("count"));
		request.setAttribute("pageCounter", pageCounter);
	}
	request.setAttribute("sign", "5");
	return "operationUser";
}

// ?û???????????
public String updateFreezeUser() {
	objectDao = new ObjectDao<UserInfo>();
	Integer id = Integer.valueOf(request.getParameter("id"));
	hql = "from UserInfo where id = " + id;
	UserInfo userInfo = objectDao.queryFrom(hql);
	if (userInfo.getFreeze().equals("??")) {
		userInfo.setFreeze("????");
	} else if (userInfo.getFreeze().equals("????")) {
		userInfo.setFreeze("??");
	}
	boolean flag = objectDao.updateT(userInfo);
	if (flag) {
		Integer pageCounter = Integer
				.valueOf(request.getParameter("count"));
		request.setAttribute("pageCounter", pageCounter);
	}
	request.setAttribute("sign", "5");
	return "operationUser";
}

// ??????
public String landOutUser() {
	request.getSession().invalidate();
	request.setAttribute("sign", "3");
	return "operationUser";
}

// ?û????????
public String queryUser() {
	hql = "from UserInfo order by id desc";
	objectDao = new ObjectDao<UserInfo>();
	List<UserInfo> list = null;
	Integer showNumber = 15;
	Integer count = 0;
	if (null != request.getParameter("count")) {
		count = Integer.valueOf(request.getParameter("count"));
	}
	list = objectDao.queryList(hql);
	Integer maxPage = new Integer(list.size());
	if (maxPage % showNumber == 0) {
		maxPage = maxPage / showNumber;
	} else {
		maxPage = maxPage / showNumber + 1;
	}
	if (0 == count) {
		list = objectDao.queryList(hql, showNumber, count);
	} else {
		count--;
		list = objectDao.queryList(hql, showNumber, count * showNumber);
	}
	request.setAttribute("count", count);
	request.setAttribute("list", list);
	request.setAttribute("maxPage", maxPage);
	return "queryUser";
}

// ?û????????
public void validateAddUserInfo() {
	objectDao = new ObjectDao<UserInfo>();
	if (this.userInfo.getAccount().equals("")) {
		this.addFieldError("account", "?û???????????");
	} else {
		objectDao = new ObjectDao<UserInfo>();
		hql = "from UserInfo where account= '" + userInfo.getAccount()+ "'";
		if (null != objectDao.queryFrom(hql)) {
			this.addFieldError("account", "?û??????????????????");
		}
	}

	if (this.userInfo.getRealname().equals("")) {
		this.addFieldError("realname", "?û????????????????");
	}
	if (userInfo.getPassword().equals("")) {
		this.addFieldError("password", "??????????");
	}
	if (userInfo.getRepassword().equals("")) {
		this.addFieldError("repassword", "??????????????");
	}
	if (!userInfo.getPassword().equals(userInfo.getRepassword())) {
		this.addFieldError("reepassword", "???????????????????????£???????????");
	}
	if (userInfo.getEmail().equals("")) {
		this.addFieldError("email", "??????????????");
	} else {
		if (!com.mr.tools.ValidateExpression
				.checkemail(userInfo.getEmail())) {
			this.addFieldError("email", "Email?????????????");
		}
	}
	if (userInfo.getAddress().equals("")) {
		this.addFieldError("address", "??????????????");
	}
	if (null == userInfo.getQq()) {
		this.addFieldError("qq", "QQ???????????????????");
	}
	if (null == userInfo.getTel()) {
		this.addFieldError("tel", "???????????????????");
	}
	if (userInfo.getProfession().equals("")) {
		this.addFieldError("profession", "??????????");
	}
	String rand = (String) request.getSession().getAttribute("rand");
	if (!rand.equals(userInfo.getCode())) {
		this.addFieldError("code", "????????????????");
	}

}

// ????û????????
public String addUserInfo() {
	objectDao = new ObjectDao<UserInfo>();
	userInfo.setPassword(com.mr.tools.ValidateExpression.encodeMD5(userInfo
			.getPassword()));
	userInfo.setHomepage(userInfo.getHomepage() + userInfo.getAccount());
	boolean flag = objectDao.saveT(userInfo);
	String result = "";
	if (flag) {
		String descPath = ServletActionContext.getRequest().getRealPath("/" + userInfo.getAccount() + "");
		String sourPath = ServletActionContext.getRequest().getRealPath("/templet/index.jsp");
		if (com.mr.tools.FileOperation.buildJSP(sourPath, descPath,userInfo.getAccount())) {
			result = "?????????";
			request.getSession().setAttribute("freeze",userInfo.getFreeze());
			request.getSession().setAttribute("account",userInfo.getAccount());
		}
	}
	request.setAttribute("result", result);
	request.setAttribute("sign", "1");
	return "operationUser";
}

// ?û????????
public String admin_deleteUserInfo() {
	String accounts[] = request.getParameterValues("account");
	for (String account : accounts) {
		// ???????
		ObjectDao<ArticleInfo> objectDao1 = new ObjectDao<ArticleInfo>();
		hql = "from ArticleInfo where author='" + account + "'";
		List<ArticleInfo> articleInfoList = objectDao1.queryList(hql);
		for (ArticleInfo articleInfo : articleInfoList) {
			// ?????????
			ObjectDao<ReArticleInfo> objectDao2 = new ObjectDao<ReArticleInfo>();
			hql = "from ReArticleInfo where re_id=" + articleInfo.getId();
			List<ReArticleInfo> reArticleInfoList = objectDao2
					.queryList(hql);
			for (ReArticleInfo reArticleInfo : reArticleInfoList) {
				objectDao2.deleteT(reArticleInfo);
			}
			objectDao1.deleteT(articleInfo);
		}
		// ??????
		ObjectDao<PhotoInfo> objectDao3 = new ObjectDao<PhotoInfo>();
		hql = "from PhotoInfo where author='" + account + "'";
		List<PhotoInfo> photoInfoList = objectDao3.queryList(hql);
		for (PhotoInfo photoInfo : photoInfoList) {
			String path1 = request.getRealPath("/"
					+ photoInfo.getPhotoAddress());
			File file1 = new File(path1);
			if (file1.exists()) {
				file1.delete();
			}
			objectDao3.deleteT(photoInfo);
		}
		// ?????????????????????
		ObjectDao<Info> objectDao4 = new ObjectDao<Info>();
		hql = "from Info where info_account='" + account + "'";
		List<Info> infoList = objectDao4.queryList(hql);
		for (Info info : infoList) {
			objectDao4.deleteT(info);
		}
		// ????û?
		objectDao = new ObjectDao<UserInfo>();
		hql = "from UserInfo where account='" + account + "'";
		userInfo = objectDao.queryFrom(hql);
		objectDao.deleteT(userInfo);
		String path2 = request.getRealPath("/" + account);
		File file1 = new File(path2);
		if (file1.exists()) {
			file1.delete();
		}

	}
	request.setAttribute("sign", "21");
	return "operationUser";
}
}

