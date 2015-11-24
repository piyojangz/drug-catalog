/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;

/**
 *
 * @author moth
 */
@Component
@Scope("request")
public class DisplayHistory implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(DisplayHistory.class);
    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    private String hospDrugCode;
    private List<UploadHospitalDrugItem> hospitalDrugs;

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public List<UploadHospitalDrugItem> getHospitalDrugs() {
        return hospitalDrugs;
    }

    public void setHospitalDrugs(List<UploadHospitalDrugItem> hospitalDrugs) {
        this.hospitalDrugs = hospitalDrugs;
    }

    public void load() {
        LOG.info("load hospDrugCode = {}", hospDrugCode);
        hospitalDrugs = uploadHospitalDrugItemRepo.findByHospDrugCodeAndUploadDrugHcode(hospDrugCode, SecurityUtil.getUserDetails().getOrgId(), new Sort(Sort.Direction.ASC, "id"));
    }
}
