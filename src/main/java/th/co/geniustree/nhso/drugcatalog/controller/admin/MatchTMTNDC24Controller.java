/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.NDC24Utils;
import th.co.geniustree.nhso.drugcatalog.model.MatchTMTNDC24;
import th.co.geniustree.nhso.drugcatalog.model.NDC24;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.MatchTMTNDC24Repo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.MatchTMTNDC24Specs;
import th.co.geniustree.nhso.drugcatalog.service.NDC24Service;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class MatchTMTNDC24Controller implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(MatchTMTNDC24Controller.class);

    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrug;

    private SpringDataLazyDataModelSupport<MatchTMTNDC24> matchTMTNDC24s;

    private TMTDrug selectedTMT;
    private NDC24 selectedNDC24;
    private MatchTMTNDC24 selectedMatchTMTNDC24;

    @Autowired
    private NDC24Service ndc24Service;

    @Autowired
    private MatchTMTNDC24Repo matchTMTNDC24Repo;

    private String searchTMTDrug;
    private String searchWord;

    @PostConstruct
    public void initial() {
        searchMatch();
    }

    public void onSearchTMTDrugDialog() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 1000);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(searchTMTDrug);
        params.put("search", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/hospital/create/selectDrugDialog.xhtml", options, params);
    }

    public void onTmtDialogReturn(SelectEvent event) {
        TMTDrug tmt = (TMTDrug) event.getObject();
        if (tmt != null) {
            selectedTMT = tmt;
            searchTMTDrug = tmt.getTmtId();
        }
    }

    public String ndc24Splitter(String ndc24) {
        try {
            String ndc24WithStructure = NDC24Utils.separateWithStructure(ndc24, "-");
            return ndc24WithStructure;
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    public void searchMatch() {
        matchTMTNDC24s = new SpringDataLazyDataModelSupport<MatchTMTNDC24>() {
            @Override
            public Page<MatchTMTNDC24> load(Pageable pageable) {
                if (searchWord == null || searchWord.isEmpty()) {
                    return matchTMTNDC24Repo.findAll(pageable);
                } else {
                    List<String> keywords = Arrays.asList(searchWord.split("\\s+"));
                    Specification<MatchTMTNDC24> spec = Specifications.where(null);
                    spec = Specifications.where(spec)
                            .or(MatchTMTNDC24Specs.tmtLike(keywords))
                            .or(MatchTMTNDC24Specs.fsnLike(keywords))
                            .or(MatchTMTNDC24Specs.ndcLike(keywords));
                    return matchTMTNDC24Repo.findAll(spec, pageable);
                }
            }
        };
    }

    public void save() {
        try {
            MatchTMTNDC24 m = new MatchTMTNDC24();
            m.setTmtid(selectedTMT.getTmtId());
            m.setNdc24(selectedNDC24.getNdc24());
            matchTMTNDC24Repo.save(m);
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้");
        }
//        searchMatch();
    }

    public void edit() {
        try {
            selectedMatchTMTNDC24.setNdc24(selectedNDC24.getNdc24());
            matchTMTNDC24Repo.save(selectedMatchTMTNDC24);
            FacesMessageUtils.info("แก้ไขข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถแก้ไขข้อมูลได้");
        }
    }

    public void delete() {
        try {
            matchTMTNDC24Repo.delete(selectedMatchTMTNDC24);
            FacesMessageUtils.info("ลบข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้");
        }
    }

    public void onSelect(MatchTMTNDC24 item) {
        selectedMatchTMTNDC24 = item;
        selectedNDC24 = null;
        LOG.debug("selected tmt : {}\tndc24 : {}", selectedMatchTMTNDC24.getTmtid(), selectedMatchTMTNDC24.getNdc24());
    }

    public List<NDC24> completeNDC24(String query) {
        return ndc24Service.search(query);
    }

    public void selectTMT(TMTDrug tmtDrug) {
        selectedTMT = tmtDrug;
        searchWord = tmtDrug.getTmtId();
        LOG.debug("selected TMT : {}", selectedTMT.getTmtId());
    }

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(SpringDataLazyDataModelSupport<TMTDrug> tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public TMTDrug getSelectedTMT() {
        return selectedTMT;
    }

    public void setSelectedTMT(TMTDrug selectedTMT) {
        this.selectedTMT = selectedTMT;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public NDC24 getSelectedNDC24() {
        return selectedNDC24;
    }

    public void setSelectedNDC24(NDC24 selectedNDC24) {
        this.selectedNDC24 = selectedNDC24;
    }

    public SpringDataLazyDataModelSupport<MatchTMTNDC24> getMatchTMTNDC24s() {
        return matchTMTNDC24s;
    }

    public void setMatchTMTNDC24s(SpringDataLazyDataModelSupport<MatchTMTNDC24> matchTMTNDC24s) {
        this.matchTMTNDC24s = matchTMTNDC24s;
    }

    public MatchTMTNDC24 getSelectedMatchTMTNDC24() {
        return selectedMatchTMTNDC24;
    }

    public void setSelectedMatchTMTNDC24(MatchTMTNDC24 selectedMatchTMTNDC24) {
        this.selectedMatchTMTNDC24 = selectedMatchTMTNDC24;
    }

    public String getSearchTMTDrug() {
        return searchTMTDrug;
    }

    public void setSearchTMTDrug(String searchTMTDrug) {
        this.searchTMTDrug = searchTMTDrug;
    }

}
