/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;

/**
 *
 * @author Thanthathon
 */
@RunWith(Parameterized.class)
public class SplitActiveIngredientFromFSNTest {

    private static final Logger LOG = LoggerFactory.getLogger(SplitActiveIngredientFromFSNTest.class);

    private final FSNSplitter splitter = new FSNSplitter();

    private final TMTDrug tmtDrug;
    private final String[] expectedActiveIngredient;
    private final String[] expectedStrength;
    private final String[] expectedActiveIngredientAndStrength;
    private final String expectedDosageForm;
    private final String expectedContent;
    private final String[] unexpectedData;

    private static Set<String> activeIngredients;
    private static Set<String> strengths;
    private static Set<String> activeIngredientAndStrength;

    /**
     * Constructor for generate TMTDrug instance for test
     *
     * @param type input Type
     * @param fsn input fsn for TMTDrug
     * @param expAI expected activeIngredient
     * @param expS expected strength
     * @param expAIAndS expected Active ingredient and strength using pattern
     * "<b>activeIngredient + strength</b>"
     * @param expC expected content
     * @param expD expected dosageForm
     * @param unexpData unexpected data
     */
    public SplitActiveIngredientFromFSNTest(TMTDrug.Type type, String fsn, String[] expAI, String[] expS, String[] expAIAndS, String expD, String expC, String[] unexpData) {
        this.tmtDrug = new TMTDrug();
        this.tmtDrug.setType(type);
        this.tmtDrug.setFsn(fsn);
        this.expectedActiveIngredient = expAI;
        this.expectedActiveIngredientAndStrength = expAIAndS;
        this.expectedContent = expC;
        this.expectedDosageForm = expD;
        this.expectedStrength = expS;
        this.unexpectedData = unexpData;
    }

    @Parameters
    public static Collection<Object[]> generateData() {
        return Arrays.asList(new Object[][]{
            {Type.TPU,
                "CIPRO HC OTIC (ALCON CUSI, SPAIN) (ciprofloxacin 200 mg/100 mL + hydrocortisone 1 g/100 mL) ear drops, suspension, 10 mL bottle",
                new String[]{"ciprofloxacin", "hydrocortisone"},
                new String[]{"200 mg/100 mL", "1 g/100 mL"},
                new String[]{"ciprofloxacin 200 mg/100 mL", "hydrocortisone 1 g/100 mL"},
                "ear drops",
                "10 mL bottle",
                new String[]{"(ciprofloxacin", "ALCON CUSI", "OTIC", "200 mg", "200 mg/100", "100 mL", "mg/100", " ear drops,", "suspension", "bottle", "mL bottle"}
            },
            {Type.TP,
                " CIPROHEXAL 500 (P.T. PRIMA HEXAL, INDONESIA) (ciprofloxacin 500 mg) film-coated tablet",
                new String[]{"ciprofloxacin"},
                new String[]{"500 mg"},
                new String[]{"ciprofloxacin 500 mg"},
                "film-coated tablet",
                null,
                new String[]{"CIPROHEXAL 500", "CIPROHEXAL", "500", "P.T.", "PRIMA HEXAL,"}
            },
            {Type.TPU,
                "QUALIMOL (อังกฤษตรางู) (paracetamol 500 mg) tablet, 1 tablet",
                new String[]{"paracetamol"},
                new String[]{"500 mg"},
                new String[]{"paracetamol 500 mg"},
                "tablet",
                "1 tablet",
                new String[]{"อังกฤษตรางู", "QUALIMOL", "tablet,", ", 1 tablet"}
            },
            {Type.TP,
                "PAMAPHEN (เคมีภัณฑ์ เมดิคอล) (chlorpheniramine 1 mg + paracetamol 120 mg + phenylephrine 5 mg) tablet",
                new String[]{"chlorpheniramine", "paracetamol", "phenylephrine"},
                new String[]{"1 mg", "120 mg", "5 mg"},
                new String[]{"chlorpheniramine 1 mg", "paracetamol 120 mg", "phenylephrine 5 mg"},
                "tablet",
                null,
                new String[]{"mg", "+ phenylephrine 5 mg", "เมดิคอล,", " tablet"}
            },
            {Type.TPU,
                "FILGEN (BIOPROFARMA, ARGENTINA) (filgrastim 300 mcg/1 mL) solution for injection in pre-filled syringe, 1 mL prefilled syr",
                new String[]{"filgrastim"},
                new String[]{"300 mcg/1 mL"},
                new String[]{"filgrastim 300 mcg/1 mL"},
                "solution for injection in pre-filled syringe",
                "1 mL prefilled syr",
                new String[]{"pre-filled", "pre-filled syringe", "300 mcg", "mcg/1", "1 mL", "ARGENTINA", "BIOPROFARMA, ARGENTINA", "solution for injection"}
            },
            {Type.TPU,
                "GAVISCON 250 (RECKITT BENCKISER HEALTHCARE, U.K.) (calcium carbonate 80 mg + sodium alginate 250 mg + sodium bicarbonate 133.5 mg) tablet, 1 tablet",
                new String[]{"calcium carbonate", "sodium alginate", "sodium bicarbonate"},
                new String[]{"80 mg", "250 mg", "133.5 mg"},
                new String[]{"calcium carbonate 80 mg", "sodium alginate 250 mg", "sodium bicarbonate 133.5 mg"},
                "tablet",
                "1 tablet",
                new String[]{"mg", "U.K.", "HEALTHCARE,", ", U.K.", "GAVISCON 250"}
            },
            {Type.TPU,
                "FUNGIZID-RATIOPHARM 200 (MERCKLE, GERMANY) (clotrimazole 200 mg) vaginal tablet, 1 tablet",
                new String[]{"clotrimazole"},
                new String[]{"200 mg"},
                new String[]{"clotrimazole 200 mg"},
                "vaginal tablet",
                "1 tablet",
                new String[]{"FUNGIZID-RATIOPHARM", "RATIOPHARM 200", "GERMANY", "vaginal", "MERCKLE,"}
            },
            {Type.TPU,
                "CLOXA T.O. 250 (ที.โอ. แลบ) (cloxacillin 250 mg) capsule, hard, 1 capsule",
                new String[]{"cloxacillin"},
                new String[]{"250 mg"},
                new String[]{"cloxacillin 250 mg"},
                "capsule",
                "1 capsule",
                new String[]{"hard", "T.O. 250", "ที.โอ. แลบ", "CLOXA T.O. 250", "hard, 1 capsule"}
            },
            {Type.TPU,
                "DEXTROSE 5% IN NORMAL SALINE (ไทยนครพัฒนา) (dextrose 5 g/100 mL + sodium chloride 900 mg/100 mL) solution for infusion, 500 mL bag",
                new String[]{"dextrose", "sodium chloride"},
                new String[]{"5 g/100 mL", "900 mg/100 mL"},
                new String[]{"dextrose 5 g/100 mL", "sodium chloride 900 mg/100 mL"},
                "solution for infusion",
                "500 mL bag",
                new String[]{"5% IN NORMAL SALINE", "ไทยนครพัฒนา", "ที.โอ. แลบ", "5%", "bag", "SALINE"}
            },
            {Type.TPU,
                "HYDROX-10 (ที.แมน. ฟาร์มา) (hydroxyzine 10 mg) film-coated tablet, 1 tablet",
                new String[]{"hydroxyzine"},
                new String[]{"10 mg"},
                new String[]{"hydroxyzine 10 mg"},
                "film-coated tablet",
                "1 tablet",
                new String[]{"5% IN NORMAL SALINE", "ไทยนครพัฒนา", "ที.โอ. แลบ", "5%", "bag", "SALINE"}
            },
            {Type.TPU,
                "CLOXACILLIN 500 (ชุมชนเภสัชกรรม) (cloxacillin 500 mg) capsule, hard, 1 capsule",
                new String[]{"cloxacillin"},
                new String[]{"500 mg"},
                new String[]{"cloxacillin 500 mg"},
                "capsule",
                "1 capsule",
                new String[]{"500", "(cloxacillin 500 mg)", "ชุมชนเภสัชกรรม", "capsule, hard,", "bag", "hard"}
            },
            {Type.TPU,
                "SPIRO R P C (อาร์.พี.ซี. อินเตอร์เนชั่นแนล) (spironolactone 25 mg) tablet, 1 tablet",
                new String[]{"spironolactone"},
                new String[]{"25 mg"},
                new String[]{"spironolactone 25 mg"},
                "tablet",
                "1 tablet",
                new String[]{"อาร์.พี.ซี.", "R P C", "ชุมชนเภสัชกรรม", "อินเตอร์เนชั่นแนล", "mg", "SPIRO R P C"}
            },
            {Type.TPU,
                "MITOTAX 100 (DR. REDDY'S LABORATORIES, INDIA) (paclitaxel 100 mg) injection, 16.7 mL vial",
                new String[]{"paclitaxel"},
                new String[]{"100 mg"},
                new String[]{"paclitaxel 100 mg"},
                "injection",
                "16.7 mL vial",
                new String[]{"16.7 mL", "mL vial", "LABORATORIES", "INDIA", "REDDY'S", "MITOTAX 100"}
            },
            {Type.TP,
                "CLAMIX (ห้างขายยาตราเจ็ดดาว) (amoxicillin 200 mg/5 mL + clavulanic acid 28.5 mg/5 mL) powder for oral suspension",
                new String[]{"amoxicillin", "clavulanic acid"},
                new String[]{"200 mg/5 mL", "28.5 mg/5 mL"},
                new String[]{"amoxicillin 200 mg/5 mL", "clavulanic acid 28.5 mg/5 mL"},
                "powder for oral suspension",
                null,
                new String[]{"clavulanic", "acid", "suspension", "ห้างขายยาตราเจ็ดดาว", "mg/5", "powder"}
            },
            {Type.TPU,
                "PEGINTRON CLEARCLICK (MSD INTERNATIONAL, SINGAPORE) (peginterferon alfa-2b 120 mcg) powder for solution for injection, 120 mcg pen",
                new String[]{"peginterferon alfa-2b"},
                new String[]{"120 mcg"},
                new String[]{"peginterferon alfa-2b 120 mcg"},
                "powder for solution for injection",
                "120 mcg pen",
                new String[]{"alfa", "2b", "alfa-2b", ", SINGAPORE", "CLEARCLICK", "pen"}
            },
            {Type.TPU,
                "REBIF 22 (MERCK SERONO, ITALY) (interferon beta-1a 22 mcg/0.5 mL) solution for injection, 0.5 mL vial",
                new String[]{"interferon beta-1a"},
                new String[]{"22 mcg/0.5 mL"},
                new String[]{"interferon beta-1a 22 mcg/0.5 mL"},
                "solution for injection",
                "0.5 mL vial",
                new String[]{", ITALY", "beta", "beta-1a", "0.5 mL", "22", "injection"}
            },
            {Type.TPU,
                "BOSTON ADVANCE CLEANER (BAUSCH & LOMB, U.S.A.) (alkyl ether sulfate 263.1 mg + makon OP-9 3.57 mg + phospholipid-PTC 1.78 mg + syloid 244 19.44 mg) irrigation solution, 30 mL bottle",
                new String[]{"alkyl ether sulfate", "makon OP-9", "phospholipid-PTC", "syloid 244"},
                new String[]{"263.1 mg", "3.57 mg", "1.78 mg", "19.44 mg"},
                new String[]{"alkyl ether sulfate 263.1 mg", "makon OP-9 3.57 mg", "phospholipid-PTC 1.78 mg", "syloid 244 19.44 mg"},
                "irrigation solution",
                "30 mL bottle",
                new String[]{"sulfate", "9", "PTC", "244", "syloid", "OP-9", "makon OP", "19.44","BAUSCH","BAUSCH & LOMB, U.S.A.","U.S.A."}
            }
        });
    }

    @BeforeClass
    public static void boforeClass() {
        activeIngredients = new HashSet<String>();
        strengths = new HashSet<String>();
        activeIngredientAndStrength = new HashSet<String>();
    }

    @Before
    public void before() {
        try {
            splitter.getActiveIngredientAndStrengthFromFSN(this.tmtDrug);
            System.out.println("FSN : " + this.tmtDrug.getFsn());
        } catch (Exception e) {
            LOG.error(null, e);
        }
    }

    @Test
    public void findActiveIngredient() {
        activeIngredients.addAll(splitter.getOnlyActiveIngredients());
        assertThat(activeIngredients)
                .contains(expectedActiveIngredient)
                .doesNotContain(unexpectedData);
    }

    @Test
    public void findStrength() {
        strengths.addAll(splitter.getOnlyStrengths());
        assertThat(strengths)
                .contains(expectedStrength)
                .doesNotContain(unexpectedData);
    }

    @Test
    public void activeIngredientAndStrengthMustMatch() {
        activeIngredientAndStrength.addAll(splitter.getActiveIngredientAndStrength());
        assertThat(activeIngredientAndStrength)
                .contains(expectedActiveIngredientAndStrength)
                .doesNotContain(unexpectedData);
    }

    @Test
    public void findDosageForm() {
        String dosageForm = splitter.getDosageForm();
        assertThat(dosageForm)
                .contains(expectedDosageForm)
                .isNotEmpty()
                .isNotNull();
    }

    @Test
    public void findContent() {
        String content = splitter.getContent();
        if(this.tmtDrug.getType().equals(Type.TPU)){
            assertThat(content)
                .contains(expectedContent);
        } else if(this.tmtDrug.getType().equals(Type.TPU)){
            assertThat(content)
                    .isNull();
        }
    }
}
