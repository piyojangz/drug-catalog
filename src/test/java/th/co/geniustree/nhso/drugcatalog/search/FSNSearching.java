/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author Thanthathon
 */
public class FSNSearching {

    List<TMTDrug> drugs;

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
        drugs = new ArrayList<>();
        TMTDrug drug = new TMTDrug();
        drug.setTmtId("000001");
        drug.setFsn("ciprofloxacin 5 mg/1.1 g + doxycycline 22 mg/1.1 g + metronidazole 23 mg/1.1 g + polyethylene glycol 4000 1 g/1.1 g + propylene glycol 50 mcL/1.1 g powder and solution for dental cement");
        drug.setType(TMTDrug.Type.GP);
        drugs.add(drug);

        drug.setTmtId("000002");
        drug.setFsn("CIPRO HC OTIC (ALCON CUSI, SPAIN) (ciprofloxacin 200 mg/100 mL + hydrocortisone 1 g/100 mL) ear drops, suspension, 10 mL bottle");
        drug.setType(TMTDrug.Type.TPU);
        drugs.add(drug);

        drug.setTmtId("000003");
        drug.setFsn("ciprofloxacin + hydrocortisone");
        drug.setType(TMTDrug.Type.VTM);
        drugs.add(drug);

        drug.setTmtId("000004");
        drug.setFsn("ciprofloxacin + doxycycline + metronidazole + polyethylene glycol 4000 + propylene glycol");
        drug.setType(TMTDrug.Type.VTM);
        drugs.add(drug);

        drug.setTmtId("000005");
        drug.setFsn(" CIPROHEXAL 500 (P.T. PRIMA HEXAL, INDONESIA) (ciprofloxacin 500 mg) film-coated tablet");
        drug.setType(TMTDrug.Type.TP);
        drugs.add(drug);

        drug.setTmtId("000006");
        drug.setFsn("QUALIMOL (อังกฤษตรางู) (paracetamol 500 mg) tablet, 1 tablet");
        drug.setType(TMTDrug.Type.TPU);
        drugs.add(drug);

        drug.setTmtId("000007");
        drug.setFsn("PAMAPHEN (เคมีภัณฑ์ เมดิคอล) (chlorpheniramine 1 mg + paracetamol 120 mg + phenylephrine 5 mg) tablet");
        drug.setType(TMTDrug.Type.TP);
        drugs.add(drug);

        drug.setTmtId("000008");
        drug.setFsn("ibuprofen 400 mg + paracetamol 325 mg tablet");
        drug.setType(TMTDrug.Type.GP);
        drugs.add(drug);

        drug.setTmtId("000009");
        drug.setFsn("paracetamol");
        drug.setType(TMTDrug.Type.SUB);
        drugs.add(drug);

        drug.setTmtId("000010");
        drug.setFsn("chlorpheniramine 750 mcg/5 mL + guaifenesin 50 mg/5 mL + paracetamol 150 mg/5 mL + phenyephrine 2.5 mg/5 mL syrup, 60 mL bottle");
        drug.setType(TMTDrug.Type.GPU);
        drugs.add(drug);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void searchInboxWithoutTmt() {
        FSNSplitter splitter = new FSNSplitter();
        Set<String> activeIngredients = new HashSet<>();
        Set<String> strengths = new HashSet<>();
        for (TMTDrug drug : drugs) {
            splitter.getActiveIngredientAndStrengthFromFSN(drug);
            activeIngredients.addAll(splitter.getActiveIngredients());
            strengths.addAll(splitter.getStrengths());
        }
        assertTrue(activeIngredients.contains("chlorpheniramine"));
        assertTrue(activeIngredients.contains("guaifenesin"));
        assertTrue(activeIngredients.contains("hydrocortisone"));
        assertTrue(activeIngredients.contains("phenyephrine"));
    }
}
