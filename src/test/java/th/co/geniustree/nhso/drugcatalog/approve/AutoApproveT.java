/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.approve;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.schedule.Processor;

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

    @Test
    public void mustApproveAllItemFlagUD() {
        processor.process();
        List<RequestItem> items = new ArrayList<>();
        items.addAll(requestItemRepo.findAllByFlag(RequestItem.Status.REQUEST, "U"));
        items.addAll(requestItemRepo.findAllByFlag(RequestItem.Status.REQUEST, "D"));
        assertThat(items).isEmpty();
    }
}
