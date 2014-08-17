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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.event.FacesEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
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

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SearchTmtDrug.class);

    private List<String> selectColumns = new ArrayList<>();
    private Type[] selectTypes = {Type.TPU};
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
                Specifications<TMTDrug> spec = Specifications.where(null);
                if (selectTypes.length > 0) {
                    spec = spec.and(Specifications.where(TMTDrugSpecs.typeIn(Arrays.asList(selectTypes))));
                }

                if (selectColumns.isEmpty()) {
                    selectColumns.add("FSN");
                    selectColumns.add("TMTID");
                }
                if (selectColumns.contains("FSN")) {
                    spec = spec.and(TMTDrugSpecs.fsnContains(keywords));
                }
                if (selectColumns.contains("TMTID")) {
                    spec = spec.or(TMTDrugSpecs.tmtIdContains(keywords));
                }
                return tmtDrugRepo.findAll(spec, pageAble);
            }
        };

    }

    public void prepareAssigngroup(String tmtId) {
        LOG.info("Prepare assign druggroup data for tmt id {}", tmtId);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        List<String> tmtIds = new ArrayList<>();
        tmtIds.add(tmtId);
        params.put("tmtId", tmtIds);
        RequestContext.getCurrentInstance().openDialog("/private/common/drug/selectDrugGroupDialog", options, params);
    }

    public void selectDrug(TMTDrug tmtDrug) {
        RequestContext.getCurrentInstance().closeDialog(tmtDrug);
    }

    public void onSaveGroup(SelectEvent event) {
        LOG.info("receive event {}", event.getObject());
    }
}
