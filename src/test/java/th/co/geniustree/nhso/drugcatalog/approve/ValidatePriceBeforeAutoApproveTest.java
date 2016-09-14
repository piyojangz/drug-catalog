/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.approve;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;
import th.co.geniustree.nhso.drugcatalog.service.impl.UploadHospitalDrugItemServiceImpl;

/**
 *
 * @author thanthathon.b
 */
public class ValidatePriceBeforeAutoApproveTest {

    private UploadHospitalDrugItemService uploadHospitalDrugItemService;
    private UploadHospitalDrugItem newItem;

    @Before
    public void setUp() {
        newItem = new UploadHospitalDrugItem();
        newItem.setUploadDrug(Mockito.mock(UploadHospitalDrug.class));
        UploadHospitalDrugItem latestItem = new UploadHospitalDrugItem();
        latestItem.setUnitPrice("10");
        latestItem.setUpdateFlag("U");

        uploadHospitalDrugItemService = new UploadHospitalDrugItemServiceImpl();
        UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo = Mockito.mock(UploadHospitalDrugItemRepo.class);

        Mockito.when(uploadHospitalDrugItemRepo.findLatestItemThatAcceptAndNotDeleteByUpdateFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(latestItem);
        ReflectionTestUtils.setField(uploadHospitalDrugItemService, "repo", uploadHospitalDrugItemRepo);

    }

    @Test
    public void oldPrice10_newPrice15_MustApprove() {
        newItem.setUnitPrice("15");
        boolean notMoreThanTwiceAsMuch = uploadHospitalDrugItemService.isUnitPriceNotMoreThanDoubleLatestItem(newItem);
        Assertions.assertThat(notMoreThanTwiceAsMuch)
                .isTrue();
    }

    @Test
    public void oldPrice10_newPrice21_MustApprove() {
        newItem.setUnitPrice("21");
        boolean notMoreThanTwiceAsMuch = uploadHospitalDrugItemService.isUnitPriceNotMoreThanDoubleLatestItem(newItem);
        Assertions.assertThat(notMoreThanTwiceAsMuch)
                .isTrue();
    }

    @Test
    public void oldPrice10_newPrice31_MustNotApprove() {
        newItem.setUnitPrice("31");
        boolean notMoreThanTwiceAsMuch = uploadHospitalDrugItemService.isUnitPriceNotMoreThanDoubleLatestItem(newItem);
        Assertions.assertThat(notMoreThanTwiceAsMuch)
                .isFalse();
    }
    
}
