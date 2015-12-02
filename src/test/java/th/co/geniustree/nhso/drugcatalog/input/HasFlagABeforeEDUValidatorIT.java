package th.co.geniustree.nhso.drugcatalog.input;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.validation.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.HospitalDrugService;
import static org.assertj.core.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author pramoth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class HasFlagABeforeEDUValidatorIT {

    @Autowired
    private Validator validator;

    @Autowired
    private HospitalDrugService hospitalDrugService;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    @Autowired
    private UploadHospitalDrugItemService uploadItemService;

    private RequestItem uploadItem;

    @Before
    public void before() {

    }

    @Test
    public void flagAMustBeforeFlagEUD() {
        hospitalDrugService.addOrUpdateHospitalDrug(generateData());
        HospitalDrug hDrug = hospitalDrugRepo.findOne(new HospitalDrugPK("C1000", "10666"));
        assertThat(hDrug)
                .isNotNull();
    }

    @After
    public void after() {
        System.gc();
    }

    private static RequestItem generateData() {
        UploadHospitalDrugItem item = new UploadHospitalDrugItem();
        item.setContent("1 tablet");
        Date date = new GregorianCalendar(2015, 9, 0).getTime();
        item.setDateEffectiveDate(date);
        item.setDosageForm("tablet");
        item.setHospDrugCode("C1000");
        item.setIsed("E");
        item.setManufacturer("ร้านขายยา");
        item.setTradeName("SARA");
        item.setGenericName("paracetamol");
        item.setUnitPrice("20");
        item.setUpdateFlag("A");
        item.setTmtDrug(new TMTDrug("100005"));
        RequestItem requestItem = new RequestItem();
        requestItem.setUploadDrugItem(item);
        UploadHospitalDrug uploadHospitalDrug = new UploadHospitalDrug();
        uploadHospitalDrug.setHcode("10666");
        item.setUploadDrug(uploadHospitalDrug);
        return requestItem;
    }
}
