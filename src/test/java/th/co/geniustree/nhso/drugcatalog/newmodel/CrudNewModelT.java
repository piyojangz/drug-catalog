/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.newmodel;

import java.util.GregorianCalendar;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem.ED;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.FundService;
import th.co.geniustree.nhso.drugcatalog.service.ICD10Service;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupItemService;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author pramoth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class CrudNewModelT {

    @Autowired
    private TMTDrugService tmtDrugService;

    @Autowired
    private FundService fundService;

    @Autowired
    private ICD10Service icd10Service;

    @Autowired
    private ReimburseGroupService reimburseGroupService;

    @Autowired
    private ReimburseGroupItemService reimburseGroupItemService;

    @Before
    public void before() {
//        initial sample data
        saveReimburseGroup("CAP",",มะเร็ง protocol",true);
        saveReimburseGroup("OCPA","OCPA",true);
        saveReimburseGroup("IVIG","IVIG",false);
        saveReimburseGroup("J2","J2",false);
        saveReimburseGroup("CAP",",มะเร็ง protocol",true);
        saveReimburseGroupItem("100005", "UC", "A00", ED.E, "IVIG", 2555);
        saveReimburseGroupItem("100005", "UC", "A000", ED.N, "CAP", 2556);
        saveReimburseGroupItem("100005", "UC", "A001", ED.N, "OCPA", 2556);
        saveReimburseGroupItem("100005", "UC", "A001", ED.EX, "J2", 2557);
    }

    public CrudNewModelT() {

    }

    private void saveReimburseGroup(String id, String name,boolean specialProject) {
        reimburseGroupService.save(id, name, specialProject);
    }

    private void saveReimburseGroupItem(String tmtid, String fundCode, String icd10Code, ED statusEd, String reimburseGroupId, int budgetYear) {
        TMTDrug tmtDrug = tmtDrugService.findOneWithoutTx(tmtid);
        Fund fund = fundService.findOne(fundCode);
        ICD10 icd10 = icd10Service.findByCode(icd10Code);

        ReimburseGroup reimburseGroup = reimburseGroupService.findByCode(reimburseGroupId);
//        ReimburseGroupItem reimburseGroupItem = reimburseGroupItemService.save(tmtDrug, fund, icd10, statusEd, reimburseGroup, budgetYear);
//        assertNotNull(reimburseGroupItem);
    }

    @Test
    public void testFindById() {
        ReimburseGroupItem groupItem;
        groupItem = reimburseGroupItemService.findById("100005", "UC", "A000", "CAP", new GregorianCalendar(2556, 9, 1).getTime());
        assertNotNull(groupItem);

        groupItem = reimburseGroupItemService.findById("100005", "UC", null, "CAP", new GregorianCalendar(2556, 9, 1).getTime());
        assertNull(groupItem);
    }

    @Test
    public void testFindReimburseGroup() {
        List<ReimburseGroupItem> reimburseGroupItems = null;
        reimburseGroupItems = reimburseGroupItemService.findReimburseGroupItem("100005", "UC", null, new GregorianCalendar(2556, 9, 1).getTime());
        assertNotNull(reimburseGroupItems);

        reimburseGroupItems = null;
        reimburseGroupItems = reimburseGroupItemService.findReimburseGroupItem("100005", "C", "A001", new GregorianCalendar(2556, 9, 1).getTime());
        assertEquals(0, reimburseGroupItems.size()); // find not found 

        reimburseGroupItems = null;
        reimburseGroupItems = reimburseGroupItemService.findReimburseGroupItem("100005", "C", "A0012", new GregorianCalendar(2556, 9, 1).getTime());
        assertEquals(0, reimburseGroupItems.size()); // find not found because icdCode input range is overflow
    }

}
