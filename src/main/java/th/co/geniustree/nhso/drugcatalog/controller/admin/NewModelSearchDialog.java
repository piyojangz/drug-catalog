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
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class NewModelSearchDialog {

    private static final Logger log = LoggerFactory.getLogger(NewModelSearchDialog.class);

    @Autowired
    private TMTDrugService tmtDrugService;

    private SpringDataLazyDataModelSupport<TMTDrug> drugs;
    
    private String searchTMT;

    private TMTDrug selectedDrug;
    
    @PostConstruct
    public void postConstruct() {

    }

    public void search() {
        drugs = new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                Page<TMTDrug> page = tmtDrugService.search(searchTMT, pageAble);
                return page;
            }

        };
    }
    
    public void setSelectedDrug(TMTDrug selectTMTDrug) {
        this.selectedDrug = selectTMTDrug;
        log.info("selected drug => {}", selectTMTDrug.getTmtId());
        RequestContext.getCurrentInstance().closeDialog(selectTMTDrug);
    }

    //****************** getter and setter method ******************
    public SpringDataLazyDataModelSupport<TMTDrug> getDrugs() {
        return drugs;
    }

    public void setDrugs(SpringDataLazyDataModelSupport<TMTDrug> drugs) {
        this.drugs = drugs;
    }

    public String getSearchTMT() {
        return searchTMT;
    }

    public void setSearchTMT(String searchTMT) {
        this.searchTMT = searchTMT;
    }

}
