/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.DrugGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class DrugGroupAssociation implements Serializable{
    private List<DrugGroup> drugGroups;
    private DrugGroup selectedDrugGoup;
    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs;
    @PostConstruct
    public void posConstruct(){
        tmtDrugs = new SpringDataLazyDataModelSupport<TMTDrug>(){

            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                return super.load(pageAble); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
    }
    
}
