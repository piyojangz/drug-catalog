/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

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
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.DosageFormGroupService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class MatchTMTAndDosageFormGroup {

    private static final Logger LOG = LoggerFactory.getLogger(MatchTMTAndDosageFormGroup.class);

    @Autowired
    private TMTDrugService tmtDrugService;

    @Autowired
    private DosageFormGroupService dosageFormGroupService;

    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs;

    private List<DosageFormGroup> dosageFormGroups;

    private TMTDrug tmtDrug;

    private DosageFormGroup dosageFormGroup;

    private String searchWord;

    @PostConstruct
    public void initial() {
        tmtDrugs = new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                return tmtDrugService.findAll(pageAble);
            }
        };
    }

    public void reset() {
        tmtDrug = null;
        dosageFormGroup = null;
        searchWord = "";
    }

    public List<DosageFormGroup> completeDosageFormGroup(String query) {
        return dosageFormGroupService.search(query);
    }

    public void searchTMT() {
        if (searchWord == null || searchWord.isEmpty()) {
            return;
        }
        tmtDrugs = new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                return tmtDrugService.search(searchWord, pageAble);
            }
        };
    }

    public void select(TMTDrug selectedTMT) {
        this.tmtDrug = selectedTMT;
        this.dosageFormGroup = null;
        LOG.info("selected drug => {}", selectedTMT.getTmtId());
    }

    public void save() {
        LOG.debug("dosage form group : {}", dosageFormGroup.getIdGroup());
        try {
            tmtDrug.setDosageformGroup(dosageFormGroup.getIdGroup());
            tmtDrugService.save(tmtDrug);
            FacesMessageUtils.info("แก้ไข DosageFormGroup สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถแก้ไข dosage form group ได้");
        }
    }
        
    public SpringDataLazyDataModelSupport<TMTDrug> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
    }

    public List<DosageFormGroup> getDosageFormGroups() {
        return dosageFormGroups;
    }

    public void setDosageFormGroups(List<DosageFormGroup> dosageFormGroups) {
        this.dosageFormGroups = dosageFormGroups;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public DosageFormGroup getDosageFormGroup() {
        return dosageFormGroup;
    }

    public void setDosageFormGroup(DosageFormGroup dosageFormGroup) {
        this.dosageFormGroup = dosageFormGroup;
    }

}
