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
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
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
    private EdNedRepo edNedRepo;

    private static final Logger LOG = LoggerFactory.getLogger(CrudNewModelT.class);

    public CrudNewModelT() {
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
        List<ReimburseGroupItem> reimburseGroupItems;
        reimburseGroupItems = findGroup("100005", "UC", "A00", new GregorianCalendar(2012, 9, 1).getTime());
        String group = reimburseGroupItems.get(0).getIcd10Group().getReimburseGroup().getId();
        assertEquals("CAG", group);

//        
        reimburseGroupItems = findGroup("100005", "C", "A00", new GregorianCalendar(2012, 9, 1).getTime());
        group = reimburseGroupItems.get(0).getIcd10Group().getReimburseGroup().getId();
        assertEquals("OCPA", group);
        reimburseGroupItems = findGroup("100005", "O", "A00", new GregorianCalendar(2012, 9, 1).getTime());
        assertNull(reimburseGroupItems);
    }

    private List<ReimburseGroupItem> findGroup(String tmtid, String fundId, String icd10Id, Date budgetYear) {
        return reimburseGroupItemService.findReimburseGroupItem(tmtid, fundId, icd10Id, budgetYear);
    }
}
