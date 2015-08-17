/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author Thanthathon
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class FSNSearching {

    @Autowired
    private TMTDrugService tmtDrugService;

    @Autowired
    private TMTDrugRepo drugRepo;

    public FSNSearching() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void searchInboxWithoutTmt() {
        FSNSplitter splitter = new FSNSplitter();
        String activeIngredient = "ciprofloxacin";
        List<String> list = Arrays.asList(activeIngredient.split("\\s+"));
        Specifications<TMTDrug> spec = Specifications.where(TMTDrugSpecs.fsnContains(list));
        List<TMTDrug> tmtDrugs = tmtDrugService.findBySpec(spec);
        for(TMTDrug drug : tmtDrugs){
            System.out.println("-----------------------");
            System.out.println("TMTID : " + drug.getTmtId());
            System.out.println("TYPE : " + drug.getType());
            System.out.println("FSN : " + drug.getFsn());
            splitter.getActiveIngredientAndStrengthFromFSN(drug);
            System.out.println("-----------------------");
            System.out.println("\n\n");
        }
    }
}
