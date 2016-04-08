/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.service.ReimbursePriceService;

/**
 *
 * @author thanthathon.b
 */
@Component
@Scope("view")
public class ReimbursePriceTPController implements Serializable {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReimbursePriceTPController.class);

    @Autowired
    private ReimbursePriceService service;
    
    private SpringDataLazyDataModelSupport<ReimbursePriceTP> data;
    private String keyword;
    
    @PostConstruct
    public void postConstruct(){
        loadData();
    }
    
    public void loadData(){
        data = new SpringDataLazyDataModelSupport<ReimbursePriceTP>(new Sort("id.hcode", "id.hospDrugCode", "id.dateEffective")){

            @Override
            public Page<ReimbursePriceTP> load(Pageable pageAble) {
                return service.findAllTP(pageAble);
            }
          
        };
    }
    
    public void search(){
        data = new SpringDataLazyDataModelSupport<ReimbursePriceTP>(){

            @Override
            public Page<ReimbursePriceTP> load(Pageable pageAble) {
                return service.searchTP(keyword, pageAble);
            }
          
        };
    }

    public SpringDataLazyDataModelSupport<ReimbursePriceTP> getData() {
        return data;
    }

    public void setData(SpringDataLazyDataModelSupport<ReimbursePriceTP> data) {
        this.data = data;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
}
