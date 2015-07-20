/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.newmodel;

import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    public CrudNewModelT() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFindEdStatus() {
        System.out.println("****************************");
        Drug drug = drugRepo.findOne("110922");
        Fund fund = fundRepo.findOne("UCS");
        Date date = new GregorianCalendar(2013, 9, 1).getTime();

        EdNedPK edNedPK = new EdNedPK(drug.getId(), fund.getId(), date);
        EdNed edNed = edNedRepo.findOne(edNedPK);

        if (edNed != null) {
            System.out.println("tmtid -> " + drug.getId());
            System.out.println("fund -> " + fund.getId());
            System.out.println("Date -> " + date);
            System.out.println("edStatus -> " + edNed.getStatus());
            assertEquals(edNed.getStatus(), "N");
        } else {
            assertTrue(false);
        }

    }

    @Test
    public void testFindReimburseGroup() {
        findGroup("112336", "UCS", "A00", new GregorianCalendar(2013, 9, 1).getTime(), "E", "VMI (จ2)");
        findGroup("112577", "UCS", "A00", new GregorianCalendar(2013, 9, 1).getTime(), "N", "AA");
        findGroup("118096", "UCS", "A01", new GregorianCalendar(2013, 9, 1).getTime(), "N", "ACEI");
    }

    private void findGroup(String tmtid, String fundId, String icd10Id, Date date, String expectedStatus, String expectedGroup) {
        System.out.println("****************************");
        Drug drug = drugRepo.findOne(tmtid);
        Fund fund = fundRepo.findOne(fundId);
        ICD10 icd10 = icd10Repo.findOne(icd10Id);

        EdNedPK edNedPK = new EdNedPK(drug, fund, date);

        EdNed edNed = edNedRepo.findOne(edNedPK);

        assertEquals(edNed.getStatus(), expectedStatus);

        ReimburseGroupItem reimburseGroupItem = null;
        ReimburseGroupItemPK reimburseGroupItemPK = new ReimburseGroupItemPK(drug, fund, icd10, edNed);
        if (edNed.getStatus().equals(expectedStatus)) {
            reimburseGroupItem = reimburseGroupItemRepo.findOne(reimburseGroupItemPK);

            System.out.println("tmtid -> " + drug.getId());
            System.out.println("fund -> " + fund.getId());
            System.out.println("icd10 -> " + icd10.getId());
            System.out.println("edStatus -> " + edNed.getStatus());
            System.out.println("reimburseGroup -> " + reimburseGroupItem.getReimburseGroup().getId());

            assertEquals(reimburseGroupItem.getReimburseGroup().getId(), expectedGroup);
        }
    }

}
