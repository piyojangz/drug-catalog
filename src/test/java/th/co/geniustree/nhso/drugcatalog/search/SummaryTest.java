/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.search;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequest;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequestMapper;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;

/**
 *
 * @author pramoth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class SummaryTest {

    private static final Logger LOG = LoggerFactory.getLogger(SummaryTest.class);

    @Autowired
    private RequestItemRepo requestItemRepo;

    public SummaryTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testQuery() {
        Pageable pageRequest = new PageRequest(0, 5);
        Page page = requestItemRepo.countSummaryRequestByProvince("1000", pageRequest);
        List list = page.getContent();
        Object[] startObject = (Object[]) list.get(0);
        SummaryRequest summaryRequest = SummaryRequestMapper.mapToModel(startObject);
        LOG.info("All hospital Request = {}", page.getTotalElements());
        printDetails(summaryRequest);
        assertThat(summaryRequest.getCountAll())
                .isEqualTo(sumAllFlag(summaryRequest))
                .isEqualTo(sumAllTmt(summaryRequest));
    }
    
    private int sumAllFlag(SummaryRequest model){
        return model.getCountFlagA() + model.getCountFlagD() + model.getCountFlagE() + model.getCountFlagU();
    }
    
    private int sumAllTmt(SummaryRequest model){
        return model.getCountTMTNotNull() + model.getCountTMTNull();
    }

    public void printDetails(SummaryRequest summaryRequest) {
        LOG.info("date {} ", summaryRequest.getRequestDate());
        LOG.info("hcode {} ", summaryRequest.getHcode());
        LOG.info("hname {} ", summaryRequest.getHname());
        LOG.info("TMT_NOT_NULL {} ", summaryRequest.getCountTMTNotNull());
        LOG.info("TMT_NULL {} ", summaryRequest.getCountTMTNull());
        LOG.info("FLAG_A {} ", summaryRequest.getCountFlagA());
        LOG.info("FLAG_E {} ", summaryRequest.getCountFlagE());
        LOG.info("FLAG_U {} ", summaryRequest.getCountFlagU());
        LOG.info("FLAG_D {} ", summaryRequest.getCountFlagD());
        LOG.info("ALL {} ", summaryRequest.getCountAll());
    }
}
