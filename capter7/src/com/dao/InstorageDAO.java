package com.dao;

import com.actionForm.*;
import com.core.MySession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class InstorageDAO {
    private Session session = null;
    //       session=MySession.openSession();   //��Session
//       MySession.closeSession(session);    //�ر�Session
    public void cart_add(StockGoodsForm goodsForm, HttpServletRequest request) {
        HttpSession httpsession = request.getSession();
        System.out.println("******************:" +
                           httpsession.getAttribute("stockgoods") +
                           "**********");
        if (httpsession.getAttribute("stockgoods") != null &&
            !httpsession.getAttribute("stockgoods").equals("")) {
            List list = (List) httpsession.getAttribute("stockgoods");
            boolean flag = true;
            for (int i = 0; i < list.size(); i++) { //�ж��Ƿ��Ѿ�����˸ü�¼
                StockGoodsForm goodsF = (StockGoodsForm) list.get(i);
                if (goodsF.getId() == goodsForm.getId()) { //�Ѿ��ɹ�
                    goodsF.setNumber(goodsF.getNumber() + goodsForm.getNumber());
                    list.set(i, goodsF); //�����趨ָ��λ��i����Ԫ��
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.add(goodsForm);
            }
            httpsession.setAttribute("stockgoods", list);
        } else {
            System.out.println("goodsForm:" + goodsForm);
            List list = new LinkedList();
            list.add(goodsForm);
            httpsession.setAttribute("stockgoods", list);
        }
    }

    public void cart_remove(int id, HttpServletRequest request) {
        HttpSession httpsession = request.getSession();
        List list = (List) httpsession.getAttribute("stockgoods");
        if (list.size() > 1) {
            list.remove(id); //��ȥָ����������Ϣ
            httpsession.setAttribute("stockgoods", list);
        } else {
            httpsession.removeAttribute("stockgoods");
        }
    }

    //��ձ���������Ϣ��Session
    public void cart_clear(HttpServletRequest request) {
        HttpSession httpsession = request.getSession();
        httpsession.removeAttribute("stockgoods");
    }

//��Ӳɹ�����Ϣ
    public int stockadd(StockMainForm stockMainForm, HttpServletRequest request) {
        int rtn = 1;
        HttpSession httpsession = request.getSession();
        List list = (List) httpsession.getAttribute("stockgoods");
        Transaction tx = null;
        if (list.size() <= 0) {
            rtn = 0;
        } else {
            session = MySession.openSession(); //��Session
            try {
                tx = session.beginTransaction();
                System.out.println("��Ӧ��ID��" + stockMainForm.getProviderid());

                for (int i = 0; i < list.size(); i++) {
                    StockDetailForm stockDetailForm = new StockDetailForm();
                    StockGoodsForm stockGoods = (StockGoodsForm) list.get(i);
                    stockMainForm.setCreateTime(new Date());

                    int goodsid = stockGoods.getId();
                    GoodsForm goodsForm = (GoodsForm) session.get(GoodsForm.class,
                            goodsid);
                    stockDetailForm.setGoods(goodsForm);
//                    System.out.println("GOODSID:"+goodsForm.getId());

//                    stockDetailForm.setGoodsid(stockGoods.getId());
                    stockDetailForm.setNumber(stockGoods.getNumber());
                    stockDetailForm.setPrice(stockGoods.getPrice());
                    stockMainForm.getStockDetail().add(stockDetailForm);
                    stockDetailForm.setStockMain(stockMainForm);
                    System.out.println("goods.getId:" + stockGoods.getId() +
                                       "**********" +
                                       stockDetailForm.getGoodsid());
                }
                session.save(stockMainForm);

                /*********���ɲɹ�����*******************/
                NumberFormat formater = NumberFormat.getNumberInstance();
                int id = stockMainForm.getId();
                formater.setMinimumIntegerDigits(5);
                Date createTime = stockMainForm.getCreateTime();
                java.sql.Date date = new java.sql.Date(createTime.getTime());
                String sNo = "CG" + date +
                             formater.format(id).toString().replace(",", ""); //��ϲɹ�����
                /**************************************/
                //����ɹ�����
                StockMainForm stockmaniF = (StockMainForm) session.get(
                        StockMainForm.class, id);
                stockmaniF.setSno(sNo);
                session.update(stockmaniF);
                tx.commit();
                httpsession.removeAttribute("stockgoods");
                rtn=1;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                rtn=0;
                e.printStackTrace();
                System.out.println("����ɹ���Ϣʱ�Ĵ�����ʾ��" + e.getMessage());
            } finally {
                MySession.closeSession(session); //�ر�Session
            }
        }
        return rtn;
    }

    //�����������Ϣ
    public int eligibleAdd(int id, HttpServletRequest request) {
        int rtn = 1;
        session = MySession.openSession(); //��session
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            InStorageForm inStorageForm = new InStorageForm();
            inStorageForm.setStockid(id);
            inStorageForm.setCreateTime(new Date());
            HttpSession httpsession = request.getSession();
            inStorageForm.setUsername((String) httpsession.getAttribute(
                    "username")); //���ò���Ա
            session.save(inStorageForm); //������������Ϣ
            /********���������ⵥ��Ϣ***********/
            NumberFormat formater = NumberFormat.getNumberInstance();
            int iid = inStorageForm.getId();
            formater.setMinimumIntegerDigits(5);
            Date inTime = inStorageForm.getCreateTime();
            java.sql.Date date = new java.sql.Date(inTime.getTime());
            String ino = "RK" + date +
                         formater.format(iid).toString().replace(",", ""); //�����ⵥ��
            /**********************************/
            //������ⵥ��
            InStorageForm inStorageF = (InStorageForm) session.get(
                    InStorageForm.class, iid);
            inStorageF.setIno(ino);
            session.update(inStorageF);
            //�޸Ĳɹ���״̬Ϊ�����
            StockMainForm stockMainForm = (StockMainForm) session.get(
                    StockMainForm.class, id);
            stockMainForm.setState(1);
            session.update(stockMainForm);
            //�޸Ŀ����Ϣ��
            String hql = "FROM StockDetailForm WHERE stockid=" + id + "";
            System.out.println("HQL:" + hql);
            List list = null;
            Query query = session.createQuery(hql);
            list = query.list();
            System.out.println("�����˵�list.size:" + list.size());
            int goodsid = 0;
            int goodsnumber = 0;
            String sql = "";
            for (int i = 0; i < list.size(); i++) {
                StockDetailForm stockdetailF = (StockDetailForm) list.get(i);
                goodsid = stockdetailF.getGoods().getId(); //��ƷID
                goodsnumber = stockdetailF.getNumber(); //�ɹ�����
                //�ж��Ƿ��Ѿ�����Ҫ������Ʒ
                String hql_goods = "FROM StorageForm WHERE goodsid=" + goodsid +
                                   "";
                List list_goods = session.createQuery(hql_goods).list();
                if (list_goods.size() > 0) {
                    String hql_up = "UPDATE StorageForm set number=number+" +
                                    goodsnumber + " WHERE goodsid=" + goodsid +
                                    "";
                    System.out.println("HQL_UP:" + hql_up);
                    session.createQuery(hql_up).executeUpdate();

                } else { //������ʱ
                    StorageForm storageF = new StorageForm();
                    storageF.setGoodsid(goodsid);
                    storageF.setNumber(goodsnumber);
                    session.save(storageF);
                    System.out.println("���");
                }

            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("������ʱ�Ĵ�����Ϣ��" + e.getMessage());
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            rtn = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return rtn;
    }

    //��˲��ϸ���
    public int uneligibleAdd(int id, HttpServletRequest request) {
        int rtn = 1;
        session = MySession.openSession(); //��session
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            CheckForm checkForm = new CheckForm();
            checkForm.setStockid(id);
            checkForm.setCheckTime(new Date());
            HttpSession httpsession = request.getSession();
            checkForm.setChecker((String) httpsession.getAttribute("username")); //���ò���Ա
            session.save(checkForm); //������˲��ϸ���Ϣ
            //�޸Ĳɹ���״̬Ϊ���ϸ�
            StockMainForm stockMainForm = (StockMainForm) session.get(
                    StockMainForm.class, id);
            stockMainForm.setState(2);
            session.update(stockMainForm);
            tx.commit();
        } catch (Exception e) {
            System.out.println("��˲��ϸ�ʱ�Ĵ�����Ϣ��" + e.getMessage());
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            rtn = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return rtn;
    }

//����黹
    public int backloan(int id, HttpServletRequest request) {
        int rtn = 1;
        session = MySession.openSession(); //��session
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LoanForm loanForm = (LoanForm) session.get(LoanForm.class, id);
            loanForm.setBacktime(new Date());
            HttpSession httpsession = request.getSession();
            loanForm.setBackperson((String) httpsession.getAttribute("username")); //���ù黹��
            loanForm.setState(new Short("2")); //�޸Ľ����״̬Ϊ�ѹ黹
            session.update(loanForm); //�������黹��Ϣ
            //�޸Ŀ����Ϣ��
            GoodsForm goodsF = loanForm.getGoods();
            int goodsid = goodsF.getId();
            int goodsnumber = loanForm.getNumber();
            String hql_goods = "FROM StorageForm WHERE goodsid=" + goodsid + "";
            List list_goods = session.createQuery(hql_goods).list();
            if (list_goods.size() > 0) {
                String hql_up = "UPDATE StorageForm set number=number+" +
                                goodsnumber + " WHERE goodsid=" + goodsid +
                                "";
                System.out.println("HQL_UP:" + hql_up);
                session.createQuery(hql_up).executeUpdate();
            } else {
                rtn = 0;
            }
            if (rtn == 1) {
                tx.commit();
            } else {
                if (tx != null) {
                    tx.rollback();
                }
            }

        } catch (Exception e) {
            System.out.println("����黹ʱ�Ĵ�����Ϣ��" + e.getMessage());
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            rtn = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return rtn;
    }

}
