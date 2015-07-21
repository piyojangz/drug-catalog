/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.Drug;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class NewModelSearchDialog {

    private static final Logger log = LoggerFactory.getLogger(NewModelSearchDialog.class);

    @Autowired
    private DrugRepo drugRepo;

    @Autowired
    private FundRepo fundRepo;

    @Autowired
    private Icd10Repo icdRepo;

    private SpringDataLazyDataModelSupport<Drug> drugs;
    
    private String searchTMT;

    private Drug selectedDrug;
    
    @PostConstruct
    public void postConstruct() {

    }

    public void search() {
        drugs = new SpringDataLazyDataModelSupport<Drug>() {
        
            @Override
            public Page<Drug> load(Pageable pageAble) {
                Page<Drug> page = drugRepo.findByIdContains(searchTMT, pageAble);

                return page;
            }

        };
    }
    
    public void setSelectedDrug(Drug selectTMTDrug) {
        this.selectedDrug = selectTMTDrug;
        log.info("selected drug => {}", selectTMTDrug.getId());
        RequestContext.getCurrentInstance().closeDialog(selectTMTDrug.getId());
    }

    //****************** getter and setter method ******************
    public SpringDataLazyDataModelSupport<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(SpringDataLazyDataModelSupport<Drug> drugs) {
        this.drugs = drugs;
    }

    public String getSearchTMT() {
        return searchTMT;
    }

    public void setSearchTMT(String searchTMT) {
        this.searchTMT = searchTMT;
    }
    //****************** getter and setter method ******************

}
