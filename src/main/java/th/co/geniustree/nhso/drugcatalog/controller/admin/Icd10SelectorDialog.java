/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.service.ICD10Service;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class Icd10SelectorDialog {

    private static final Logger log = LoggerFactory.getLogger(Icd10SelectorDialog.class);

    @Autowired
    private ICD10Service icd10Service;

    private String searchIcd10;

    private ICD10 selectedIcd10;
    
    private SpringDataLazyDataModelSupport<ICD10> icd10s;

    public void search() {
        icd10s = new SpringDataLazyDataModelSupport<ICD10>() {
            @Override
            public Page<ICD10> load(Pageable pageAble) {
                Page<ICD10> page = icd10Service.search(searchIcd10, pageAble);
                return page;
            }
        };
    }

    public void setSelectedDrug(ICD10 selectedIcd10) {
        this.selectedIcd10 = selectedIcd10;
        log.info("selected drug => {}", selectedIcd10.getCode());
        RequestContext.getCurrentInstance().closeDialog(selectedIcd10);
    }
    public String getSearchIcd10() {
        return searchIcd10;
    }

    public void setSearchIcd10(String searchIcd10) {
        this.searchIcd10 = searchIcd10;
    }

    public SpringDataLazyDataModelSupport<ICD10> getIcd10s() {
        return icd10s;
    }

    public void setIcd10s(SpringDataLazyDataModelSupport<ICD10> icd10s) {
        this.icd10s = icd10s;
    }

    public ICD10 getSelectedIcd10() {
        return selectedIcd10;
    }

    public void setSelectedIcd10(ICD10 selectedIcd10) {
        this.selectedIcd10 = selectedIcd10;
    }
    
    

}
