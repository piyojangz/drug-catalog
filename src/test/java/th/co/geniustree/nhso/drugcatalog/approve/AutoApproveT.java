/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.approve;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.utils.BigDecimalUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.schedule.Processor;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author thanthathon.b
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class AutoApproveT {

    @Autowired
    private Processor processor;

    @Autowired
    private RequestItemRepo requestItemRepo;

    @Autowired
    private UploadHospitalDrugItemService uploadDrugItemService;

    @Before
    public void process() {
        processor.process();
    }

    @Test
    public void mustApproveAllItemFlagD() {
        List<RequestItem> items = requestItemRepo.findAllByFlag(RequestItem.Status.REQUEST, "D");
        assertThat(items).isNullOrEmpty();
    }

//    @Test
//    public void mustNotApproveFlagUWhenUnitPriceHasOverflow() {
//        List<RequestItem> requestItems = requestItemRepo.findAllByFlag(RequestItem.Status.REQUEST, "U");
//        for (RequestItem item : requestItems) {
//            UploadHospitalDrugItem lastestDrugItem = uploadDrugItemService.findLatestItemByFlag(item.getUploadDrugItem().getUploadDrug().getHcode(), item.getUploadDrugItem().getHospDrugCode(), "U");
//            if (BigDecimalUtils.checkPrice(lastestDrugItem.getUnitPrice(), item.getUploadDrugItem().getUnitPrice())) {
//                
//            }
//        }
//    }

}
