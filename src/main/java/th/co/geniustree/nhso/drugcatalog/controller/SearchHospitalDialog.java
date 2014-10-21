/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller;

import com.google.common.base.Strings;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.repository.HospitalRepository;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class SearchHospitalDialog implements Serializable {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SearchHospitalDialog.class);
    @Autowired
    private HospitalRepository hospitalRepo;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    private String zone;
    private String province;
    private String keyword;
    private Hospital selectedHospital;
    private SpringDataLazyDataModelSupport<Hospital> hospitals;
    
    public HospitalRepository getHospitalRepo() {
        return hospitalRepo;
    }
    
    public void setHospitalRepo(HospitalRepository hospitalRepo) {
        this.hospitalRepo = hospitalRepo;
    }
    
    public String getZone() {
        return zone;
    }
    
    public void setZone(String zone) {
        this.zone = zone;
    }
    
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public SpringDataLazyDataModelSupport<Hospital> getHospitals() {
        return hospitals;
    }
    
    public void setHospitals(SpringDataLazyDataModelSupport<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
    
    public Hospital getSelectedHospital() {
        return selectedHospital;
    }
    
    public void setSelectedHospital(Hospital selectedHospital) {
        this.selectedHospital = selectedHospital;
        log.info("selected hospital => {}", selectedHospital);
        RequestContext.getCurrentInstance().closeDialog(selectedHospital);
    }
    @PostConstruct
    public void search() {
        log.info("find hospital with keyword => {}", keyword);
        hospitals = new SpringDataLazyDataModelSupport<Hospital>() {
            
            @Override
            public Page<Hospital> load(Pageable pageAble) {
                keyword = "%" + Strings.nullToEmpty(keyword).trim() + "%";
                return uploadHospitalDrugRepo.findHospitalInTmt(keyword, keyword, pageAble);
            }
            
        };
    }
}
