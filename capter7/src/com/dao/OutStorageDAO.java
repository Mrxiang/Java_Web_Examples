package com.dao;

import com.actionForm.*;
import com.core.MySession;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class OutStorageDAO {
    private Session session = null;
    //��ѯ�����Ϣ
    public List storage_query() {
        session = MySession.openSession(); //��Session
        String sql = "select {goods.*},{storage.*} from tb_goods goods inner join tb_storage storage on goods.id=storage.goodsid"; //ʹ�������Ӳ�ѯ�����Ϣ
        List list = null;
        try {
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity("goods", GoodsForm.class); //�����ݱ���ʵ���������һ��
            query.addEntity("storage", StorageForm.class);
            list = query.list();
        } catch (Exception e) {
            System.out.println("��ѯʱ�Ĵ�����Ϣ��" + e.getMessage());
        }
        return list;
    }

    //�ڲ��ű���ʱ��ѯ����������������Ϣ
    public List getuseGoods_query() {
        session = MySession.openSession(); //��Session
        //�����������ص����
        String sql = "select * from tb_goods where id in (select goodsid from tb_getuse group by goodsid)";
        List list = null;
        try {
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity("goods", GoodsForm.class); //�����ݱ���ʵ���������һ��
            list = query.list();
        } catch (Exception e) {
            System.out.println("��ѯʱ�Ĵ�����Ϣ��" + e.getMessage());
        }
        return list;
    }

    //��ѯ����������Ϣ
    public List getuse_query() {
        session = MySession.openSession(); //��Session
        String sql = "select {getuse.*},{branch.*},{goods.*} from tb_getuse getuse inner join tb_goods goods on getuse.goodsid=goods.id inner join tb_branch branch on getuse.branchid=branch.id"; //ʹ�������Ӳ�ѯ����������Ϣ
        List list = null;
        try {
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity("goods", GoodsForm.class); //�����ݱ���ʵ���������һ��
            query.addEntity("getuse", GetUseForm.class);
            query.addEntity("branch", BranchForm.class);
            list = query.list();
        } catch (Exception e) {
            System.out.println("��ѯʱ�Ĵ�����Ϣ��" + e.getMessage());
        }
        return list;
    }

//��Ӳ���������Ϣ
    public int getuseAdd(GetUseForm getUseForm) {
        session = MySession.openSession(); //��Session
        Transaction tx = null;
        int rtn = 1;
        try {
            //���沿��������Ϣ
            tx = session.beginTransaction();
            int goodsid = getUseForm.getGoodsid();
            GoodsForm goodsForm = (GoodsForm) session.get(GoodsForm.class,
                    goodsid);
            getUseForm.setGoods(goodsForm);
            int branchid = getUseForm.getBranchid();
            BranchForm branchForm = (BranchForm) session.get(BranchForm.class,
                    branchid);
            getUseForm.setBranch(branchForm);
            getUseForm.setCreatetime(new Date());
            session.save(getUseForm);
            /*********�������õ���*******************/
            NumberFormat formater = NumberFormat.getNumberInstance();
            int id = getUseForm.getId();
            formater.setMinimumIntegerDigits(5);
            Date createTime = getUseForm.getCreatetime();
            java.sql.Date date = new java.sql.Date(createTime.getTime());
            String gNo = "LY" + date +
                         formater.format(id).toString().replace(",", ""); //������õ���
            /**************************************/
//�������õ���
            GetUseForm getUseF = (GetUseForm) session.get(
                    GetUseForm.class, id);
            getUseF.setGno(gNo);
            session.update(getUseF);
//�޸Ŀ����Ϣ
//            StorageForm storageF = (StorageForm) session.get(StorageForm.class,
//                    getUseForm.getGoodsid());
            List list = session.createQuery("FROM StorageForm WHERE goodsid=" +
                                            getUseForm.getGoodsid() + "").list();
            StorageForm storageF = (StorageForm) list.get(0);
            int storageNum = storageF.getNumber() - getUseForm.getNumber();
            if (storageNum >= 0) {
                storageF.setNumber(storageNum);
                session.update(storageF);
            } else { //�����������

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
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("��Ӳ���������Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            rtn = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return rtn;
    }

    //��Ӳ��ű�����Ϣ
    public int damageAdd(DamageForm damageForm) {
        session = MySession.openSession(); //��Session
        Transaction tx = null;
        int rtn = 0;
        try {
            //���沿�ű�����Ϣ
            tx = session.beginTransaction();
            int goodsid = damageForm.getGoodsid();
            GoodsForm goodsForm = (GoodsForm) session.get(GoodsForm.class,
                    goodsid);
            damageForm.setGoods(goodsForm);
            int branchid = damageForm.getBranchid();
            BranchForm branchForm = (BranchForm) session.get(BranchForm.class,
                    branchid);
            damageForm.setBranch(branchForm);
            damageForm.setCreatetime(new Date());
            session.save(damageForm);
            tx.commit();
            rtn = 1;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("��Ӳ��ű�����Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            rtn = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return rtn;
    }

    //������ʽ����Ϣ
    public int loanAdd(LoanForm loanForm) {
        session = MySession.openSession(); //��Session
        Transaction tx = null;
        int rtn = 0;
        try {
            //�������ʽ����Ϣ
            tx = session.beginTransaction();
            int goodsid = loanForm.getGoodsid();
            GoodsForm goodsForm = (GoodsForm) session.get(GoodsForm.class,
                    goodsid);
            loanForm.setGoods(goodsForm);
            loanForm.setCreatetime(new Date());
            session.save(loanForm);
            /*********���ɽ������*******************/
            NumberFormat formater = NumberFormat.getNumberInstance();
            int id = loanForm.getId();
            formater.setMinimumIntegerDigits(5);
            Date createTime = loanForm.getCreatetime();
            java.sql.Date date = new java.sql.Date(createTime.getTime());
            String lNo = "JC" + date +
                         formater.format(id).toString().replace(",", ""); //��Ͻ������
            /**************************************/
//����������
            LoanForm loanF = (LoanForm) session.get(
                    LoanForm.class, id);
            loanF.setLno(lNo);
            session.update(loanF);
            tx.commit();
            rtn = 1;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("������ʽ����Ϣʱ�Ĵ�����Ϣ��" + e.getMessage());
            rtn = 0;
        } finally {
            MySession.closeSession(session); //�ر�Session
        }
        return rtn;
    }

    //�������
    public int approveloanAdd(int id, HttpServletRequest request) {
        int rtn = 1;
        session = MySession.openSession(); //��session
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LoanForm loanForm = (LoanForm) session.get(LoanForm.class, id);
            loanForm.setApprovetime(new Date());
            HttpSession httpsession = request.getSession();
            loanForm.setTaster((String) httpsession.getAttribute("username")); //�������Ա
            loanForm.setState(new Short("1")); //�޸Ľ����״̬Ϊ�����
            session.update(loanForm); //�����������Ϣ
            //�޸Ŀ����Ϣ��
            GoodsForm goodsF = loanForm.getGoods();
            int goodsid = goodsF.getId();
            int goodsnumber = loanForm.getNumber();
            String hql_goods = "FROM StorageForm WHERE goodsid=" + goodsid + "";
            List list_goods = session.createQuery(hql_goods).list();
            System.out.println("LIST_GOODS.SIZE:" + list_goods.size() +
                               "********GOODSID:" + goodsid);
            if (list_goods.size() > 0) {
                StorageForm storageF = (StorageForm) list_goods.get(0);
                if (storageF.getNumber() - goodsnumber >= 0) {
                    String hql_up = "UPDATE StorageForm set number=number-" +
                                    goodsnumber + " WHERE goodsid=" + goodsid +
                                    "";
                    System.out.println("HQL_UP:" + hql_up);
                    session.createQuery(hql_up).executeUpdate();
                } else {
                    rtn = 2; //��治��
                }
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
}
