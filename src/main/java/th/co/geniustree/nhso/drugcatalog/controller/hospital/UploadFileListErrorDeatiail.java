/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import com.google.common.io.Files;
import java.io.File;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugErrorItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugErrorItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class UploadFileListErrorDeatiail implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadFileListErrorDeatiail.class);
    private SpringDataLazyDataModelSupport<UploadHospitalDrugErrorItem> uploadHospitalDrugItems;
    @Autowired
    private UploadHospitalDrugRepo uploadFileRepo;
    @Autowired
    private UploadHospitalDrugErrorItemRepo uploadHospitalDrugErrorItemRepo;
    private Integer uploadId;
    private UploadHospitalDrug uploadFileDrug;
    private String fileName;

    @PostConstruct
    public void postConstruct() {
        LOG.debug("Upload file id {}", uploadId);
        //TODO Must  check permission.
//        if (!uploadFileDrug.getHcode().equals(SecurityUtil.getUserDetails().getHospital().getHcode())) {
//            return;
//        }
        uploadHospitalDrugItems = new SpringDataLazyDataModelSupport<UploadHospitalDrugErrorItem>() {

            @Override
            public Page<UploadHospitalDrugErrorItem> load(Pageable pageAble) {
                return uploadHospitalDrugErrorItemRepo.findByUploadDrugId(uploadId,pageAble);
            }

        };
    }

    public SpringDataLazyDataModelSupport<UploadHospitalDrugErrorItem> getUploadHospitalDrugItems() {
        return uploadHospitalDrugItems;
    }

    public UploadHospitalDrug getUploadFileDrug() {
        return uploadFileDrug;
    }

    public void setUploadFileDrug(UploadHospitalDrug uploadFileDrug) {
        this.uploadFileDrug = uploadFileDrug;
    }

    public Integer getUploadId() {
        return uploadId;
    }

    public void setUploadId(Integer uploadId) {
        this.uploadId = uploadId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = Files.getNameWithoutExtension(fileName);
    }

}
