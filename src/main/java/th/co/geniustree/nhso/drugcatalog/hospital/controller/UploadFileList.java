/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.hospital.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class UploadFileList implements Serializable{
    @Autowired
    private UploadHospitalDrugRepo uploadFileRepo;
    private List<UploadHospitalDrug> uploadHospitalDrugs;
    @PostConstruct
    public void postConstruct(){
        //TODO Should use lasy load.
        uploadHospitalDrugs = uploadFileRepo.findAll();
    }

    public List<UploadHospitalDrug> getUploadHospitalDrugs() {
        return uploadHospitalDrugs;
    }

    public void setUploadHospitalDrugs(List<UploadHospitalDrug> uploadHospitalDrugs) {
        this.uploadHospitalDrugs = uploadHospitalDrugs;
    }
    
}
