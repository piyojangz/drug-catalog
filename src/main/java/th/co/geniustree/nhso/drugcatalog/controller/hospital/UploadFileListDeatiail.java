/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class UploadFileListDeatiail implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadFileListDeatiail.class);
    private List<UploadHospitalDrugItem> uploadHospitalDrugItems;
    @Autowired
    private UploadHospitalDrugRepo uploadFileRepo;
    private Integer uploadId;
    private UploadHospitalDrug uploadFileDrug;
    
    @PostConstruct
    public void postConstruct() {
        //TODO Should use lasy load.
        LOG.debug("Upload file id {}", uploadId);
    }
    
    public List<UploadHospitalDrugItem> getUploadHospitalDrugItems() {
        return uploadHospitalDrugItems;
    }
    
    public void setUploadHospitalDrugItems(List<UploadHospitalDrugItem> uploadHospitalDrugItems) {
        this.uploadHospitalDrugItems = uploadHospitalDrugItems;
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
    public void load(){
        LOG.debug("Load call. Upload file id {}", uploadId);
        uploadFileDrug = uploadFileRepo.findOne(uploadId);
        uploadHospitalDrugItems = uploadFileRepo.findOne(uploadId).getPassItems();
    }
    
}
