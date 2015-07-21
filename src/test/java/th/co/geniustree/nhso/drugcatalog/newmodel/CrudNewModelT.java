/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.newmodel;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.admin.EdNedMapper;
import th.co.geniustree.nhso.drugcatalog.model.Drug;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.EdNedPK;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;

/**
 *
 * @author pramoth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class CrudNewModelT {

    @Autowired
    private ReimburseGroupItemRepo reimburseGroupItemRepo;
    @Autowired
    private DrugRepo drugRepo;
    @Autowired
    private FundRepo fundRepo;
    @Autowired
    private EdNedRepo edNedRepo;
    @Autowired
    private Icd10Repo icd10Repo;

    private static final Logger LOG = LoggerFactory.getLogger(CrudNewModelT.class);

    private Drug drug;
    private Fund fund;
    private ICD10 icd10;

    public CrudNewModelT() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFindEdStatusWhereDateCondition() {
        // In database I have 
        // 101021 UCS 01/10/2012 E
        // 101021 UCS 01/10/2013 N
        findEd("101021", "UCS", new GregorianCalendar(2012, 9, 1).getTime(), "E");
        findEd("101021", "UCS", new GregorianCalendar(2012, 9, 2).getTime(), "N");

        findEd("100005", "UCS", new GregorianCalendar(2012, 8, 30).getTime(), "N");
    }

    private void findEd(String tmtId, String fundId, Date date, String expectedStatus) {
        List<Object[]> objList = edNedRepo.findByTmtDrugAndFund(tmtId, fundId, date);
        EdNed edNed = EdNedMapper.mapToModelAndGetFirst(objList);
//        printEdDetails(edNed);
        String status = "";
        if (edNed != null) {
            status = edNed.getStatus();
        }
        assertEquals(status, expectedStatus);
    }

    @Test
    public void testFindReimburseGroup() {
        findGroup("112336", "UCS", "A00", new GregorianCalendar(2013, 9, 1).getTime(), "E", "VMI (จ2)");
        findGroup("112577", "UCS", "A00", new GregorianCalendar(2013, 9, 1).getTime(), "N", "AA");
        findGroup("118096", "UCS", "A01", new GregorianCalendar(2012, 9, 1).getTime(), "E", "ARBS");
    }

    private void findGroup(String tmtid, String fundId, String icd10Id, Date date, String expectedStatus, String expectedGroup) {

        List<Object[]> objList = edNedRepo.findByTmtDrugAndFund(tmtid, fundId, date);
        EdNed edNed = EdNedMapper.mapToModelAndGetFirst(objList);
        String edStatus = "";
        if (edNed != null) {
            edStatus = edNed.getStatus();
//            printEdDetails(edNed);
        }
        
        ReimburseGroupItemPK reimburseGroupItemPK = new ReimburseGroupItemPK(tmtid, fundId, icd10Id, edStatus);

        if (edStatus.equals(expectedStatus)) {
            ReimburseGroupItem reimburseGroupItem = reimburseGroupItemRepo.findOne(reimburseGroupItemPK);
//            printReimburseGroupItemDetails(reimburseGroupItem);
            assertEquals(reimburseGroupItem.getReimburseGroup().getId(), expectedGroup);
        }
    }

    private void printEdDetails(EdNed obj) {
        System.out.println("edned -> tmtId : " + obj.getPk().getTmtId());
        System.out.println("edned -> fundId : " + obj.getPk().getFundId());
        System.out.println("edned -> dateIn : " + obj.getPk().getTmtId());
        System.out.println("edned -> status : " + obj.getStatus());
    }

    private void printReimburseGroupItemDetails(ReimburseGroupItem obj) {
        System.out.println("ReimburseGroupItem -> tmtId : " + obj.getDrug().getTmtId());
        System.out.println("ReimburseGroupItem -> fundId : " + obj.getFund().getId());
        System.out.println("ReimburseGroupItem -> icd10 : " + obj.getIcd10().getId());
        System.out.println("ReimburseGroupItem -> status : " + obj.getEdStatus());
        System.out.println("ReimburseGroupItem -> groupId : " + obj.getReimburseGroup().getId());
        System.out.println("ReimburseGroupItem -> groupDescription : " + obj.getReimburseGroup().getDescription());
        System.out.println("ReimburseGroupItem -> is group sprcial project : " + obj.getReimburseGroup().isSpecialProject());
    }

}
