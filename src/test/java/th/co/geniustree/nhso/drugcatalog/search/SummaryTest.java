/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.search;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
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
        Pageable pageRequest = new PageRequest(0, 20);
        Page page = requestItemRepo.countSummaryRequest(RequestItem.Status.REQUEST,pageRequest);
        List list = page.getContent();
        Object[] startObject =  (Object[]) list.get(0);
        LOG.info("date {} " , startObject[0]);
        LOG.info("hcode {} " , startObject[1]);
        LOG.info("TMT_NOT_NULL {} " , startObject[2]);
        LOG.info("TMT_NULL {} " , startObject[3]);
        LOG.info("FLAG_A {} " , startObject[4]);
        LOG.info("FLAG_E {} " , startObject[5]);
        LOG.info("FLAG_U {} " , startObject[6]);
        LOG.info("FLAG_D {} " , startObject[7]);
        LOG.info("ALL {} " , startObject[8]);
        if(list.size() > 0){
            assertNotNull(startObject[1]);
        }
    }

}
