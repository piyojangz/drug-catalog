/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.common;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
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
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.NDC24;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;
import th.co.geniustree.nhso.drugcatalog.model.TMTReleaseFileUpload;
import th.co.geniustree.nhso.drugcatalog.repo.NDC24Repo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReleaseFileUploadRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

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
    private TMTDrugService tmtDrugService;
    private String keyword;
    private LazyDataModel<TMTDrug> models;
    private String drugGroupFilter = "ALL";
    private String latestFile;
    @Autowired
    private TMTReleaseFileUploadRepo tmtReleaseFileUploadRepo;
    @Autowired
    private NDC24Repo ndcRepo;

    @PostConstruct
    public void postConstruct() {
        selectColumns.add("FSN");
        selectColumns.add("TMTID");
        selectColumns.add("NDC24");
        TMTReleaseFileUpload findLastestReleaseDate = tmtReleaseFileUploadRepo.findLastestReleaseDate();
        if (findLastestReleaseDate != null) {
            latestFile = "TMTRF" + DateUtils.format("yyyyMMdd", findLastestReleaseDate.getReleaseDate());
        }
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

    public String getDrugGroupFilter() {
        return drugGroupFilter;
    }

    public void setDrugGroupFilter(String drugGroupFilter) {
        this.drugGroupFilter = drugGroupFilter;
    }

    public String getLatestFile() {
        return latestFile;
    }

    public void setLatestFile(String latestFile) {
        this.latestFile = latestFile;
    }

    public void search() {
        models = new SpringDataLazyDataModelSupport<TMTDrug>() {

            @Override
            public Page<TMTDrug> load(Pageable pageable) {
                List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);

                Specifications<TMTDrug> typeSpec = Specifications.where(null);
                Specifications<TMTDrug> drugGroupSpec = Specifications.where(null);
                if (selectTypes.length > 0) {
                    typeSpec = Specifications.where(TMTDrugSpecs.typeIn(Arrays.asList(selectTypes)));
                }
                if (!Strings.isNullOrEmpty(drugGroupFilter)) {
                    switch (drugGroupFilter) {
                        case "YES":
                            drugGroupSpec = Specifications.where(TMTDrugSpecs.hasDrugGroup());
                            break;
                        case "NO":
                            drugGroupSpec = Specifications.where(TMTDrugSpecs.dontHaveDrugGroup());
                            break;
                        case "ALL":
                            break;
                    }
                }
                Specifications<TMTDrug> spec = Specifications.where(typeSpec).and(drugGroupSpec);
                if (Strings.isNullOrEmpty(keyword)) {
                    return tmtDrugService.findAllAndEagerGroup(spec, pageable);
                }
                Specifications<TMTDrug> columnSpec = Specifications.where(null);
                if (selectColumns.isEmpty()) {
                    selectColumns.add("FSN");
                    selectColumns.add("TMTID");
                    selectColumns.add("NDC24");
                }
                if (selectColumns.contains("FSN")) {
                    columnSpec = columnSpec.or(TMTDrugSpecs.fsnContains(keywords));
                }
                if (selectColumns.contains("TMTID")) {
                    columnSpec = columnSpec.or(TMTDrugSpecs.tmtIdContains(keywords));
                }
                if (selectColumns.contains("NDC24")) {
                    if (keyword.matches("\\d{7,}")) {
                        List<NDC24> ndc24 = ndcRepo.findByNdc24Contains(keyword);
                        if (ndc24 != null && !ndc24.isEmpty()) {
                            for (NDC24 ndc : ndc24) {
                                LOG.debug(ndc.getNdc24());
                            }
                            columnSpec = columnSpec.or(TMTDrugSpecs.ndcContains(ndc24));
                        }
                    }
                }
                spec = Specifications.where(spec).and(columnSpec);
                return tmtDrugService.findAllAndEagerGroup(spec, pageable);
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
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        List<String> tmtIds = new ArrayList<>();
        tmtIds.add(tmtId);
        params.put("tmtId", tmtIds);
        RequestContext.getCurrentInstance().openDialog("/private/common/drug/displayDrugGroupDialog", options, params);
    }

    public void selectDrug(TMTDrug tmtDrug) {
        RequestContext.getCurrentInstance().closeDialog(tmtDrug);
    }

    public void onSaveGroup(SelectEvent event) {
        LOG.info("receive event {}", event.getObject());
    }
}
