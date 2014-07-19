/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.hospital.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.authen.WSUserDetails;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;

/**
 *
 * @author moth
 */
@Scope("request")
@Component
public class HospitalDrugListController implements Serializable {

    @Autowired
    private HospitalDrugRepo hospitalTMTDrugRepo;
    private SpringDataLazyDataModelSupport<HospitalDrug> models;
    private SpringDataLazyDataModelSupport<HospitalDrug> noTmtModels;
    private SpringDataLazyDataModelSupport<HospitalDrug> waitModels;
    private WSUserDetails user;

    @PostConstruct
    public void postConstruct() {
        user = SecurityUtil.getUserDetails();
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getModels() {
        return models;
    }

    public void setModels(SpringDataLazyDataModelSupport<HospitalDrug> models) {
        this.models = models;
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getNoTmtModels() {
        return noTmtModels;
    }

    public void setNoTmtModels(SpringDataLazyDataModelSupport<HospitalDrug> noTmtModels) {
        this.noTmtModels = noTmtModels;
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getWaitModels() {
        return waitModels;
    }

    public void setWaitModels(SpringDataLazyDataModelSupport<HospitalDrug> waitModels) {
        this.waitModels = waitModels;
    }

    public String loadApproveModel() {
        models = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalTMTDrugRepo.findByHcodeAndApproved(user.getOrgId(), true, pageAble);
            }
        };
        return null;
    }

    public String loadNonTmtModel() {
        noTmtModels = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalTMTDrugRepo.findByHcodeAndTmtIdIsNull(user.getOrgId(), pageAble);
            }
        };
        return null;
    }

    public String loadWaitModel() {
        waitModels = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalTMTDrugRepo.findByHcodeAndApprovedAndTmtIdIsNotNull(user.getOrgId(), false, pageAble);
            }

        };
        return null;
    }

}
