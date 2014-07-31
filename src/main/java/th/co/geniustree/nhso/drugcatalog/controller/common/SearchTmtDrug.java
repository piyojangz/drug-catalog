/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.common;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class SearchTmtDrug implements Serializable {

    private List<String> selectColumns = new ArrayList<>();
    private Type[] selectTypes = {Type.GPU, Type.TPU, Type.TP};
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    private String keyword;
    private LazyDataModel<TMTDrug> models;

    @PostConstruct
    public void postConstruct() {
        selectColumns.add("FSN");
        selectColumns.add("TMTID");
    }

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

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public Type[] getSelectTypes() {
        return selectTypes;
    }

    public void setSelectTypes(Type[] selectTypes) {
        this.selectTypes = selectTypes;
    }

    public Type[] getTypes() {
        return Type.values();
    }

    public void search() {
        models = new SpringDataLazyDataModelSupport<TMTDrug>() {

            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);
                Specifications<TMTDrug> spec1 = Specifications.where(TMTDrugSpecs.typeIn(Arrays.asList(selectTypes)));
                Specifications<TMTDrug> spec2 = Specifications.where(null);
                if (selectColumns.isEmpty()) {
                    selectColumns.add("FSN");
                    selectColumns.add("TMTID");
                }
                if (selectColumns.contains("FSN")) {
                    spec2 = spec2.and(TMTDrugSpecs.fsnContains(keywords));
                }
                if (selectColumns.contains("TMTID")) {
                    spec2 = spec2.or(TMTDrugSpecs.tmtIdContains(keywords));
                }
                return tmtDrugRepo.findAll(spec1.and(spec2), pageAble);
            }
        };

    }
}
