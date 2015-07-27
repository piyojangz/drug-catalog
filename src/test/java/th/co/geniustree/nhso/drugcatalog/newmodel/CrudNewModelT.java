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
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10GroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupItemService;

/**
 *
 * @author pramoth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class CrudNewModelT {

    @Autowired
    private ReimburseGroupItemService reimburseGroupItemService;
    @Autowired
    private DrugRepo drugRepo;
    @Autowired
    private FundRepo fundRepo;
    @Autowired
    private EdNedRepo edNedRepo;
    @Autowired
    private Icd10Repo icd10Repo;

    @Autowired
    private Icd10GroupRepo icd10GroupRepo;

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
    public void testMappingToModel() {
        EdNed edNed;

        edNed = findEdNed("100005", "U", new GregorianCalendar(2013, 9, 1).getTime());
        assertNull(edNed);

        edNed = findEdNed("100005", "UC", new GregorianCalendar(2011, 9, 1).getTime());
        assertNotNull(edNed);
        assertEquals("UC", edNed.getPk().getFundCode());
        assertEquals("100005", edNed.getPk().getTmtId());
        try {
            assertTrue(isInDefindStatus(edNed));
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }

    }

    /**
     * check if EdNed Status in 'E', 'N', or 'E*'
     *
     * @param edNed
     * @return true if EdNed Status is in defindStatus
     */
    private boolean isInDefindStatus(EdNed edNed) {
        String edStatus = edNed.getStatus();
        edStatus = edStatus.toUpperCase();
        return edStatus.equals("E") || edStatus.equals("N") || edStatus.equals("E*");
    }

    private EdNed findEdNed(String tmtId, String fundCode, Date dateIn) {
        List<Object[]> objList = edNedRepo.findByTmtDrugAndFund(tmtId, fundCode, dateIn);
        return EdNedMapper.mapToModelAndGetFirst(objList);
    }

    @Test
    public void testFindReimburseGroup() {
        ReimburseGroupItem reimburseGroupItem;
        reimburseGroupItem = findGroup("100005", "UC", "A00", new GregorianCalendar(2013, 9, 1).getTime());
        String group = reimburseGroupItem.getReimburseGroup().getId();
        assertEquals("CAG", group);
        
        reimburseGroupItem = findGroup("100005", "C", "A00", new GregorianCalendar(2012, 9, 1).getTime());
        group = reimburseGroupItem.getReimburseGroup().getId();
        assertEquals("OCPA", group);
        
        reimburseGroupItem = findGroup("100005", "O", "A00", new GregorianCalendar(2012, 9, 1).getTime());
        assertNull(reimburseGroupItem);
    }

    private ReimburseGroupItem findGroup(String tmtid, String fundId, String icd10Id, Date dateIn) {
        return reimburseGroupItemService.findReimburseGroup(tmtid, fundId, icd10Id, dateIn);
    }

    private void printEdDetails(EdNed obj) {
        System.out.println("edned -> tmtId : " + obj.getPk().getTmtId());
        System.out.println("edned -> fundId : " + obj.getPk().getFundCode());
        System.out.println("edned -> dateIn : " + obj.getPk().getTmtId());
        System.out.println("edned -> status : " + obj.getStatus());
    }

//    private void printReimburseGroupItemDetails(ReimburseGroupItem obj) {
//        System.out.println("ReimburseGroupItem -> tmtId : " + obj.getDrug().getTmtId());
//        System.out.println("ReimburseGroupItem -> fundId : " + obj.getFund().getFundCode());
//        System.out.println("ReimburseGroupItem -> icd10 : " + obj.getIcd10().getIcd10Id().getId());
//        System.out.println("ReimburseGroupItem -> status : " + obj.getEdStatus());
//        System.out.println("ReimburseGroupItem -> groupId : " + obj.getIcd10().getReimburseGroupId().getId());
//        System.out.println("ReimburseGroupItem -> groupDescription : " + obj.getIcd10().getReimburseGroupId().getDescription());
//        System.out.println("ReimburseGroupItem -> is group sprcial project : " + obj.getIcd10().getReimburseGroupId().isSpecialProject());
//    }
}
