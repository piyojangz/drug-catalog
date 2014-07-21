/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.common;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import java.io.Serializable;
import java.util.List;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class SearchTmtDrug implements Serializable {

    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    private String keyword;
    private LazyDataModel<TMTDrug> models;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public LazyDataModel<TMTDrug> getModels() {
        return models;
    }

    public void setModels(LazyDataModel<TMTDrug> models) {
        this.models = models;
    }
    

    public void search() {
        models = new SpringDataLazyDataModelSupport<TMTDrug>() {

            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);
                Specifications<TMTDrug> where = Specifications.where(TMTDrugSpecs.fsnContains(keywords));
                return tmtDrugRepo.findAll(where, pageAble);
            }
        };

    }
}
