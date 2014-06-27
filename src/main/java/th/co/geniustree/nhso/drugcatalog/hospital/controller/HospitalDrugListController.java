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
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalTMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalTMTDrugRepo;

/**
 *
 * @author moth
 */
@Scope("request")
@Component
public class HospitalDrugListController implements Serializable {

    @Autowired
    private HospitalTMTDrugRepo hospitalTMTDrugRepo;
    private List<HospitalTMTDrug> models;
    private List<HospitalTMTDrug> noTmtModels;
    private List<HospitalTMTDrug> waitModels;

    @PostConstruct
    public void postConstruct() {

    }

    public List<HospitalTMTDrug> getModels() {
        return models;
    }

    public void setModels(List<HospitalTMTDrug> models) {
        this.models = models;
    }

    public List<HospitalTMTDrug> getNoTmtModels() {
        return noTmtModels;
    }

    public void setNoTmtModels(List<HospitalTMTDrug> noTmtModels) {
        this.noTmtModels = noTmtModels;
    }

    public List<HospitalTMTDrug> getWaitModels() {
        return waitModels;
    }

    public void setWaitModels(List<HospitalTMTDrug> waitModels) {
        this.waitModels = waitModels;
    }

    public String loadApproveModel() {
        models = hospitalTMTDrugRepo.findByApproved(true);
        return null;
    }

    public String loadNonTmtModel() {
        noTmtModels = hospitalTMTDrugRepo.findByTmtIdIsNull();
        return null;
    }

    public String loadWaitModel() {
        waitModels = hospitalTMTDrugRepo.findByApprovedAndTmtIdIsNotNull(false);
        return null;
    }

}
