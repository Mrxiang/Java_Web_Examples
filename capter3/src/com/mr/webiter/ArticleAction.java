package com.mr.webiter;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.mr.dao.ObjectDao;
import com.mr.model.ArticleInfo;
import com.mr.model.ReArticleInfo;
import com.mr.model.UserInfo;

public class ArticleAction extends ActionSupport implements
		ModelDriven<ArticleInfo>, ServletRequestAware {
	private String hql;

	private ArticleInfo articleInfo = new ArticleInfo();

	protected HttpServletRequest request;

	private ObjectDao<ArticleInfo> objectDao = null;

	String dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(Calendar.getInstance().getTime());

	public ArticleInfo getModel() {
		return articleInfo;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	// ����Ա��¼�󣬶����µ���ϸ��ѯ
	public String admin_articleQueryOne() {
		objectDao = new ObjectDao<ArticleInfo>();//ʵ�����־û�����
		hql = "select author from ArticleInfo group by author";//��ѯ������ϸ��Ϣ��hql���
		List authorList = objectDao.queryListObject(hql);//ִ�в�ѯ
		request.setAttribute("authorList", authorList);
		Integer id = new Integer(request.getParameter("id"));//��ȡ����id
		hql = "from ArticleInfo where id = " + id;//����id��ѯ������ϸ��Ϣ��hql���
		articleInfo = objectDao.queryFrom(hql);//ִ�в�ѯ
		if (null != request.getParameter("commend")) {//�޸����µ��Ƽ�״̬
			if (articleInfo.getCommend().equals("��")) {
				articleInfo.setCommend("��");
			} else {
				articleInfo.setCommend("��");
			}
			objectDao.updateT(articleInfo);//�޸����µ��Ƽ�״̬
		}
		articleInfo = objectDao.queryFrom(hql);//�ٴβ�ѯ������ϸ��Ϣ
		request.setAttribute("articleInfo", articleInfo);
		return "admin_articleQueryOne";
	}

	// ����Ա��¼�����²�ѯ����
	public String admin_articleQuery() {
		// �����Ƕ����µ�ȫ����ѯ
		hql = "from ArticleInfo";//���ö�����ȫ����ѯHQL���
		String account = request.getParameter("account");//ҳ���е�account����
		
	
		if (null != account) {//�ж�account�����Ƿ�Ϊ��
			try {
				account = new String(account.getBytes("ISO8859_1"),"gbk");
				System.out.println("ssssssss "+account);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hql = "from ArticleInfo where author = '" + account + "'";
			request.setAttribute("account", account);
		}
		objectDao = new ObjectDao<ArticleInfo>();//�־û���objectDao�����ʵ����
		List<ArticleInfo> list = objectDao.queryList(hql);//ִ�в�ѯ��HQL���
		//�Է�ҳ���в���
		int showNumber = 10;
		Integer count = 0;
		if (null != request.getParameter("count")) {
			count = new Integer(request.getParameter("count"));
		}
		list = objectDao.queryList(hql);
		int maxPage = list.size();
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
		// ��������Ӧ�ķ�����
		hql = "select author from ArticleInfo group by author";
		List authorList = objectDao.queryListObject(hql);
		request.setAttribute("authorList", authorList);
		return "admin_articleQuery";
	}

	// ǰ̨���µ���ϸ��ѯ
	public String f_article_query() {
		// ���µ���ϸ��ѯ
		Integer id = new Integer(request.getParameter("id"));//��ȡid����
		hql = "from ArticleInfo where id =" + id;//����id������ѯ��hql���
		objectDao = new ObjectDao<ArticleInfo>();//ʵ�����־û���
		articleInfo = objectDao.queryFrom(hql);//ִ�в�ѯ
		String account = (String) request.getSession().getAttribute("account");//��ȡaccount����
		if(null==account){//���	account����Ϊ��
			account=articleInfo.getAuthor();//��ȡ�û���
			hql = "from UserInfo where account = '" + account + "'";//�����û�����ѯ��hql���
			ObjectDao<UserInfo>	objectDao1 = new ObjectDao<UserInfo>();//ʵ�����־û���
			UserInfo userInfo = objectDao1.queryFrom(hql);//ִ�в�ѯ
			request.getSession().setAttribute("userInfo", userInfo);//����ѯ���������Session��
		}
		if (null == request.getParameter("count")) {
			if (!articleInfo.getAuthor().equals(account)) {
				articleInfo.setVisit(articleInfo.getVisit() + 1);
				objectDao.updateT(articleInfo);
			}
		}
		request.setAttribute("articleInfo", articleInfo);//����articleInfo
		// ���»ظ����ݵ���ϸ��ѯ
		hql = "from ReArticleInfo where re_id=" + id + " order by id desc";//��ѯ������ϸ���ݵ�hql���
		ObjectDao<ReArticleInfo> re_objectDao = new ObjectDao<ReArticleInfo>();//ʵ�����־û���
		List<ReArticleInfo> list = null;//����List����
		// ��ҳ����
		int showNumber = 3;
		Integer count = 0;
		if (null != request.getParameter("count")) {
			count = Integer.valueOf(request.getParameter("count"));
		}
		list = re_objectDao.queryList(hql);
		if(list.size()!=0){
		int maxPage = list.size();
		if (maxPage % showNumber == 0) {
			maxPage = maxPage / showNumber;
		} else {
			maxPage = maxPage / showNumber + 1;
		}
		if (0 == count) {
			list = re_objectDao.queryList(hql, showNumber, count);
		} else {
			count--;
			list = re_objectDao.queryList(hql, showNumber, count * showNumber);
		}
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("maxPage", maxPage);
		}	
		return "f_article_query";
	}

	public String article_forwardUpdate() {
		objectDao = new ObjectDao<ArticleInfo>();
		Integer id = new Integer(request.getParameter("id"));
		hql = "from ArticleInfo where id =" + id;
		articleInfo = objectDao.queryFrom(hql);
		request.setAttribute("articleInfo", articleInfo);
		return "article_forwardUpdate";
	}

	public void validateArticle_update() {
		if (articleInfo.getTitle().equals("")) {
			this.addFieldError("title", "���������±��⣡<br><br>");
		}
		if (articleInfo.getContent().equals("")) {
			this.addFieldError("content", "�������������ݣ�");
		}

	}

	// �����޸Ĳ���
	public String article_update() {
		objectDao = new ObjectDao<ArticleInfo>();
		this.articleInfo.setSendTime(this.dateTimeFormat);
		String result = "�޸����³ɹ���";
		if (!objectDao.updateT(articleInfo)) {
			result = "�޸�����ʧ�ܣ�";
		}
		request.setAttribute("result", result);
		request.setAttribute("sign", "10");
		return "operationArticle";

	}

	// ����ɾ������
	public String article_delete() {
		Integer id = new Integer(request.getParameter("id"));
		hql = "from ArticleInfo where id =" + id;
		objectDao = new ObjectDao<ArticleInfo>();
		articleInfo = objectDao.queryFrom(hql);
		String hql1 = "from ReArticleInfo where re_id =" + id + "";
		ObjectDao<ReArticleInfo> objectDao1 = new ObjectDao<ReArticleInfo>();
		List<ReArticleInfo> list = objectDao1.queryList(hql1);
		for (ReArticleInfo reArticleInfo : list) {
			objectDao1.deleteT(reArticleInfo);
		}
		boolean flag = objectDao.deleteT(articleInfo);
		String result = "ɾ�����³ɹ���";
		if (!flag) {
			result = "ɾ������ʧ�ܣ�";
		}
		request.setAttribute("result", result);
		request.setAttribute("sign", "10");
		return "operationArticle";
	}

	// ������ϸ��ѯ
	public String article_queryContent() {
		Integer id = new Integer(request.getParameter("id"));
		hql = "from ArticleInfo where id =" + id;
		objectDao = new ObjectDao<ArticleInfo>();
		articleInfo = objectDao.queryFrom(hql);
		request.setAttribute("form", articleInfo);
		return SUCCESS;
	}

	// ���²�ѯ����
	public String article_query() {
		String account = (String) request.getSession().getAttribute("account");
		hql = "from ArticleInfo where author='" + account
				+ "' order by id desc";
		objectDao = new ObjectDao<ArticleInfo>();
		List<ArticleInfo> list = null;
		Integer showNumber = 15;
		Integer count = 0;
		if (null != request.getParameter("count")) {
			count = Integer.valueOf(request.getParameter("count"));
		}
		list = objectDao.queryList(hql);
		Integer maxPage = list.size();
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
		return SUCCESS;
	}

	// ���±���У��
	public void validateArticle_add() {
		if (articleInfo.getTitle().equals("")) {
			this.addFieldError("title", "���������±��⣡<br><br>");
		}
		if (articleInfo.getContent().equals("")) {
			this.addFieldError("content", "�������������ݣ�");
		}

	}

	// �������
	public String article_add() {
		objectDao = new ObjectDao<ArticleInfo>();
		this.articleInfo.setSendTime(this.dateTimeFormat);
		if (objectDao.saveT(articleInfo)) {
			request.setAttribute("result", "������³ɹ���");
		} else {
			request.setAttribute("result", "�������ʧ�ܣ�");
		}
		return SUCCESS;
	}
}
