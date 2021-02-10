package com.jwy.action;

import com.jwy.dao.ICourseDao;
import com.jwy.dao.ISpecialtyDao;
import com.jwy.dto.Course;
import com.jwy.dto.Specialty;
import com.jwy.dto.StuUser;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatInfoAction extends DispatchAction {

	private ISpecialtyDao specialtyDao;
	private ICourseDao courseDao;

	/**
	 * @param specialtyDao
	 *            the specialtyDao to set
	 */
	public void setSpecialtyDao(ISpecialtyDao specialtyDao) {
		this.specialtyDao = specialtyDao;
	}

	/**
	 * @param courseDao
	 *            the courseDao to set
	 */
	public void setCourseDao(ICourseDao courseDao) {
		this.courseDao = courseDao;
	}

	/**
	 * ����רҵ��ţ��γ����ƣ��ڿν�ʦ������������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward findBySearch(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response) {

		List<Specialty> sList = specialtyDao.findByAll();
		Map<String, String> map = new HashMap<String, String>();
		if (request.getParameter("specialtyId") != null
				&& !request.getParameter("specialtyId").equals("-1")) {
			map.put("specialtyId", request.getParameter("specialtyId"));
		}
		if (request.getParameter("name") != null
				&& !request.getParameter("name").equals("")) {
			map.put("name", request.getParameter("name"));
		}
		if (request.getParameter("teacherName") != null
				&& !request.getParameter("teacherName").equals("")) {
			map.put("teacherName", request.getParameter("teacherName"));
		}
		List<Object[]> clist = courseDao.findByStat(map);
		request.setAttribute("sList", sList);
		request.setAttribute("cList", clist);
		return mapping.findForward("showStat");
	}

	/**
	 * ���ݿγ̱�Ų�ѯѡ����ſγ̵�ѧ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward stuList(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
		
		Integer specialtyId = Integer.valueOf(request
				.getParameter("specialtyId"));
		Specialty specialty = specialtyDao.findById(specialtyId);// רҵ��Ϣ
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		List<Object[]> list = courseDao.findSelectStu(courseId);
		Course course = courseDao.findByID(courseId);
		System.out.println(courseId);
		request.setAttribute("specialty", specialty);
		request.setAttribute("stuList", list);
		request.setAttribute("course", course);
		return mapping.findForward("stuList");
	}

	/**
	 * ���Ͽ�ѧ����������PDF�ĵ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exPDF(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Integer specialtyId = Integer.valueOf(request.getParameter("specialtyId"));
		Specialty specialty = specialtyDao.findById(specialtyId);// רҵ��Ϣ
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		List<Object[]> list = courseDao.findSelectStu(courseId);
		Course course = courseDao.findByID(courseId);

		response.setContentType("text/html;charset=GBK");
		response.setContentType("application/xml");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(course.getName().getBytes(), "iso-8859-1")+".pdf");

		OutputStream outs = response.getOutputStream(); // ��ȡ�����

		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, outs);
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light",
				"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

		Font t1 = new Font(bfChinese, 20, Font.BOLD); // ����һ����������
		Font t2 = new Font(bfChinese, 15, Font.BOLD); // ���ö�����������
		Font f1 = new Font(bfChinese, 12, Font.NORMAL); // ������������

		Paragraph pragraph = new Paragraph(
				specialty.getEnterYear() + "��" + specialty.getLangthYear()
						+ "����" + specialty.getName() + "רҵ", t1);
		pragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(pragraph);

		pragraph = new Paragraph(course.getName() + "�γ�������Ա����"+"   ������������"+list.size()+" ��", t2);
		pragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(pragraph);
		pragraph = new Paragraph("�ڿν�ʦ" + course.getTeacherName(), t2);
		pragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(pragraph);

		// ����һ�����
		PdfPTable table = new PdfPTable(4);
		table.setSpacingBefore(40f);// ���ñ������հ׿��

		// ���ɱ�ͷ
		String[] bt = new String[] { "ѧ������", "ѧ��", "�Ա�", "��ϵ�绰" };
		for (int i = 0; i < bt.length; i++) {
			PdfPCell cell = new PdfPCell(new Paragraph(bt[i], f1)); // ����һ����Ԫ��
			cell.setBackgroundColor(Color.GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ��������ˮƽ������ʾ
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ���ô�ֱ����
			table.addCell(cell);
		}

		for (int i = 0; i < list.size(); i++) {
			Object[] o = list.get(i);
			StuUser stuUser = (StuUser) o[0];
			PdfPCell nameCell = new PdfPCell(new Paragraph(stuUser.getStuName(), f1)); // ����һ����Ԫ��
			nameCell.setHorizontalAlignment(Element.ALIGN_CENTER); // ��������ˮƽ������ʾ
			nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ���ô�ֱ����
			PdfPCell stuNoCell = new PdfPCell(new Paragraph(stuUser.getStuNo(),f1)); // ����һ����Ԫ��
			stuNoCell.setHorizontalAlignment(Element.ALIGN_CENTER); // ��������ˮƽ������ʾ
			stuNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ���ô�ֱ����
			PdfPCell stuSexCell = new PdfPCell(new Paragraph(stuUser.getStuSex(), f1)); // ����һ����Ԫ��
			stuSexCell.setHorizontalAlignment(Element.ALIGN_CENTER); // ��������ˮƽ������ʾ
			stuSexCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ���ô�ֱ����
			PdfPCell telCell = new PdfPCell(new Paragraph(stuUser.getTel(), f1)); // ����һ����Ԫ��
			telCell.setHorizontalAlignment(Element.ALIGN_CENTER); // ��������ˮƽ������ʾ
			telCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ���ô�ֱ����
			table.addCell(nameCell);
			table.addCell(stuNoCell);
			table.addCell(stuSexCell);
			table.addCell(telCell);
		}
		document.add(table);
		document.close();
		outs.close();

		return null;
	}

	/**
	 * ���Ͽ�ѧ����Ϣ����ΪExcel�ĵ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exExcel(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)throws Exception {
		 
		Integer specialtyId = Integer.valueOf(request.getParameter("specialtyId"));
		Specialty specialty = specialtyDao.findById(specialtyId);// רҵ��Ϣ
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		List<Object[]> list = courseDao.findSelectStu(courseId);
		Course course = courseDao.findByID(courseId);
		
		response.setContentType("text/html;charset=GBK");
		response.setContentType("application/xml");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(course.getName().getBytes(), "iso-8859-1")+ ".xls");
		
		HSSFWorkbook workBook = new HSSFWorkbook(); // ���� һ��excel�ĵ�����
		HSSFSheet sheet = workBook.createSheet(); 	// ����һ������������
		HSSFCell cell = null;						//������Ԫ�����
		
		sheet.setColumnWidth(0,3000);
		sheet.setColumnWidth(1,3000);
		sheet.setColumnWidth(2,2000);
		sheet.setColumnWidth(3,4000);
		
		HSSFCellStyle style = workBook.createCellStyle();	//������ʽ����
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		// �ϲ���Ԫ�����
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
		cell = sheet.createRow(0).createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(new HSSFRichTextString(specialty.getEnterYear() + "��" + specialty.getLangthYear()
				+ "����" + specialty.getName() + "רҵ"));
		cell = sheet.createRow(1).createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(new HSSFRichTextString(course.getName() + "�γ�������Ա����"+"   ������������"+list.size()+" ��"));
		cell = sheet.createRow(2).createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(new HSSFRichTextString("�ڿν�ʦ" + course.getTeacherName()));
		
		//���ñ߿���ʽ 
		HSSFCellStyle tableStyle = workBook.createCellStyle();
		tableStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		tableStyle.setBorderTop((short)1);
		tableStyle.setBorderBottom((short)1);
		tableStyle.setBorderLeft((short)1);
		tableStyle.setBorderRight((short)1);
		
		// ���ɱ�ͷ
		String[] bt = new String[] { "ѧ������", "ѧ��", "�Ա�", "��ϵ�绰" };
		HSSFRow row = sheet.createRow(3);  // ����һ���ж���
		for (int i = 0; i < bt.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(tableStyle);
			cell.setCellValue(new HSSFRichTextString(bt[i]));
		}
		//д��������
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i+4);   // ����һ���ж���
			Object[] o = list.get(i);
			StuUser stuUser = (StuUser) o[0];
			cell = row.createCell(0); 
			cell.setCellStyle(tableStyle);
			cell.setCellValue(new HSSFRichTextString(stuUser.getStuName()));
			cell = row.createCell(1);
			cell.setCellStyle(tableStyle);
			cell.setCellValue(new HSSFRichTextString(stuUser.getStuNo()));
			cell = row.createCell(2);
			cell.setCellStyle(tableStyle);
			cell.setCellValue(new HSSFRichTextString(stuUser.getStuSex()));
			cell = row.createCell(3);
			cell.setCellStyle(tableStyle);
			cell.setCellValue(new HSSFRichTextString(stuUser.getTel()));
		}	
		workBook.write(response.getOutputStream());								//���ĵ�����д���ļ������
		return null;
	}
}