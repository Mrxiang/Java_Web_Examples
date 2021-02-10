package com.spring.controller;

import com.hibernate.dao.DAOSupport;
import com.hibernate.model.BookBorrowInfo;
import com.hibernate.model.BookReginster;
import com.hibernate.model.DocuStuInfo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookManager extends MultiActionController {
	private String addPage;

	private String searchPage;

	private String bookDetailPage;

	private String bookBorrowPage;

	private String borrowSearchPage;

	private String borrowReturnPage;

	private DAOSupport dao;

	public ModelAndView add(HttpServletRequest req, HttpServletResponse res) {
		BookReginster bg = modifyBook(req);
		if (dao.InsertOrUpdate(bg))
			return new ModelAndView(getAddPage(), "result", "��ӳɹ������Լ������");
		else
			return new ModelAndView(getAddPage(), "result",
					"���ʧ�ܣ������������ݻ����ݿ�����");
	}

	public ModelAndView modify(HttpServletRequest req, HttpServletResponse res) {
		BookReginster bg = modifyBook(req);
		if (dao.InsertOrUpdate(bg))
			return list(req, res);
		else
			return new ModelAndView(getSearchPage(), "result",
					"�޸�ʧ�ܣ������������ݻ����ݿ�����");
	}

	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) {
		List list = dao.QueryObject("from BookReginster");
		return new ModelAndView(getSearchPage(), "result", list);
	}

	public ModelAndView search(HttpServletRequest req, HttpServletResponse res) {
		List list;
		String condition = req.getParameter("condition");
		if (condition.equals("���"))
			condition = "bookId";
		else if (condition.equals("����"))
			condition = "bookName";
		else
			condition = "bookConcern";
		String conditionContent = req.getParameter("conditionContent");
		list = dao.QueryObject("from BookReginster where " + condition
				+ " like '" + conditionContent + "%'");
		return new ModelAndView(getSearchPage(), "result", list);
	}

	public ModelAndView detail(HttpServletRequest req, HttpServletResponse res) {
		List list;
		String bookID = req.getParameter("bookID");
		list = dao.QueryObject("from BookReginster where bookId=" + bookID);
		BookReginster bookDetail = (BookReginster) list.get(0);
		return new ModelAndView(getBookDetailPage(), "result", bookDetail);
	}

	public ModelAndView borrow(HttpServletRequest req, HttpServletResponse res) {
		String submit = req.getParameter("save");
		List list = null;
		Map model = null;
		DocuStuInfo stuInfo = null;
		BookReginster bookDetail = null;
		BookBorrowInfo borrowInfo = null;
		String stuId = req.getParameter("stu_id");
		String bookID = req.getParameter("book_id");
		if (stuId != null && !stuId.equals("")) {
			list = null;
			list = dao.QueryObject("from DocuStuInfo where stuId=" + stuId);
			if (list != null && list.size() > 0)
				stuInfo = (DocuStuInfo) list.get(0);
		}
		if (bookID != null && !bookID.equals("")) {
			list = null;
			list = dao.QueryObject("from BookReginster where bookId='" + bookID
					+ "'");
			if (list != null && list.size() > 0)
				bookDetail = (BookReginster) list.get(0);
		}
		if (stuInfo != null) {
			list = null;
			list = dao
					.QueryObject("from BookBorrowInfo where docuStuInfo.stuId='"
							+ stuInfo.getStuId() + "'");
			model = new HashMap();
			model.put("stu", stuInfo);
			model.put("book", bookDetail);
			model.put("list", list);
		}
		String price = req.getParameter("price");
		if (submit != null && price != null && !price.equals("")) {
			System.out.println(req.getParameter("price") + "");
			borrowInfo = new BookBorrowInfo();
			borrowInfo.setBookName(req.getParameter("book_name"));
			borrowInfo.setBookReginster(bookDetail);
			borrowInfo.setBookType("book_type");
			borrowInfo.setBorrowDate(new Date(System.currentTimeMillis()));
			borrowInfo.setCzy(req.getParameter("czy"));
			borrowInfo.setDocuStuInfo(stuInfo);
			borrowInfo.setPrice(Double.valueOf(price));
			dao.InsertOrUpdate(borrowInfo);
			list = dao
					.QueryObject("from BookBorrowInfo where docuStuInfo.stuId='"
							+ stuInfo.getStuId() + "'");
			model = new HashMap();
			model.put("stu", stuInfo);
			model.put("list", list);
		}
		return new ModelAndView(getBookBorrowPage(), "info", model);
	}

	public ModelAndView returnBook(HttpServletRequest req,
			HttpServletResponse res) {
		List list = null;
		Map model = null;
		DocuStuInfo stuInfo = null;
		// BookReginster bookDetail = null;
		BookBorrowInfo borrowInfo = null;
		String submit = req.getParameter("return");
		String stuId = req.getParameter("stu_id");
		String bookID = req.getParameter("book_id");
		if (stuId != null && stuId != "") {
			list = null;
			list = dao.QueryObject("from DocuStuInfo where stuId=" + stuId);
			if (list != null && list.size() > 0)
				stuInfo = (DocuStuInfo) list.get(0);
		}
		// if (bookID != null && bookID != "") {
		// list = null;
		// list = dao.QueryObject("from BookBorrowInfo where
		// bookReginster.bookId='" + bookID
		// + "'");
		// if (list != null && list.size() > 0)
		// bookDetail = (BookReginster) list.get(0);
		// }
		if (stuInfo != null && bookID != null) {
			list = null;
			String sql = "from BookBorrowInfo where docuStuInfo.stuId='"
					+ stuInfo.getStuId() + "' and bookReginster.bookId='"
					+ bookID + "'";
			list = dao.QueryObject(sql);
			if (list != null && list.size() > 0)
				borrowInfo = (BookBorrowInfo) list.get(0);
		}
		if (submit != null && stuId != null) {// �����ͨ����ť�ύ��ɾ����¼
			list = null;
			list = dao
					.QueryObject("from BookBorrowInfo where docuStuInfo.stuId='"
							+ stuId
							+ "' and bookReginster.bookId='"
							+ bookID
							+ "'");
			if (list != null && list.size() > 0)
				dao.DeleteObject(list.get(0));
			list = dao
					.QueryObject("from BookBorrowInfo where docuStuInfo.stuId='"
							+ stuId + "'");
			System.out.println("submit list=" + list);
			model = new HashMap();
			model.put("stu", stuInfo);
			model.put("list", list);
			return new ModelAndView(getBorrowReturnPage(), "info", model);
		}
		list = dao.QueryObject("from BookBorrowInfo where docuStuInfo.stuId='"
				+ stuId + "'");

		model = new HashMap();
		model.put("stu", stuInfo);
		// model.put("book", bookDetail);
		model.put("borrow", borrowInfo);// ���ı����Ϣ
		model.put("list", list);
		return new ModelAndView(getBorrowReturnPage(), "info", model);
	}

	public ModelAndView borrowSearch(HttpServletRequest req,
			HttpServletResponse res) {
		List list = null;
		String submit = req.getParameter("Submit");
		String showAll = req.getParameter("ShowAll");
		if (submit != null) {
			String conditionContent = req.getParameter("conditionContent");
			String condition = req.getParameter("condition");
			if (condition.equals("���"))
				condition = "bookReginster.bookId";
			else if (condition.equals("ѧ��"))
				condition = "docuStuInfo.stuId";
			else {
				condition = "borrowDate";
			}
			if (!condition.equals("borrowDate"))
				list = dao.QueryObject("from BookBorrowInfo where " + condition
						+ " = '" + conditionContent + "'");
			else
				list = dao.QueryObject("from BookBorrowInfo where " + condition
						+ " >= '" + conditionContent + " 00:00:00' and "
						+ condition + " <= '" + conditionContent + " 23:59:59'");
			System.out.println(list.size());
		}
		if (showAll != null)
			list = dao.QueryObject("from BookBorrowInfo");
		return new ModelAndView(getBorrowSearchPage(), "list", list);
	}

	public String getAddPage() {
		return addPage;
	}

	public void setAddPage(String addPage) {
		this.addPage = addPage;
	}

	public void setDao(DAOSupport dao) {
		this.dao = dao;
	}

	public String getSearchPage() {
		return searchPage;
	}

	public void setSearchPage(String searchPage) {
		this.searchPage = searchPage;
	}

	public String getBookDetailPage() {
		return bookDetailPage;
	}

	public void setBookDetailPage(String bookDetailPage) {
		this.bookDetailPage = bookDetailPage;
	}

	private BookReginster modifyBook(HttpServletRequest req) {
		BookReginster bg = new BookReginster();
		bg.setBookId(req.getParameter("book_id"));// ͼ��ID
		bg.setBookConcern(req.getParameter("book_concern"));// ������
		bg.setBookCount(Integer.valueOf(req.getParameter("book_count")));// ���ͼ������
		bg.setBookName(req.getParameter("book_name"));// ����
		bg.setBookType(req.getParameter("book_type"));// ͼ�����
		bg.setCzy(req.getParameter("czy"));// ����Ա
		bg.setPrice(Double.valueOf(req.getParameter("price")));// ����
		Date date = Date.valueOf(req.getParameter("publish_date"));
		bg.setPublishDate(date);
		date = Date.valueOf(req.getParameter("reg_date"));
		bg.setRegDate(date);
		String remark = req.getParameter("remark");
		if (remark.equals(""))
			remark = null;
		bg.setRemark(remark);
		bg.setWriter(req.getParameter("writer"));
		return bg;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("gbk");
		return super.handleRequestInternal(req, res);
	}

	public String getBookBorrowPage() {
		return bookBorrowPage;
	}

	public void setBookBorrowPage(String bookBorrowPage) {
		this.bookBorrowPage = bookBorrowPage;
	}

	public String getBorrowSearchPage() {
		return borrowSearchPage;
	}

	public void setBorrowSearchPage(String borrowSearch) {
		this.borrowSearchPage = borrowSearch;
	}

	public String getBorrowReturnPage() {
		return borrowReturnPage;
	}

	public void setBorrowReturnPage(String borrowReturn) {
		this.borrowReturnPage = borrowReturn;
	}
}
