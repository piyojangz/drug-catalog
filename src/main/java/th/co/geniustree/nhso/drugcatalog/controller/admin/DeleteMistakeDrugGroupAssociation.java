/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
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
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItem;
import th.co.geniustree.nhso.drugcatalog.repo.DrugGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author thanthathon.b
 */
@Component
@Scope("view")
public class DeleteMistakeDrugGroupAssociation {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteMistakeDrugGroupAssociation.class);

    @Autowired
    private TMTDrugService tmtDrugService;

    private SpringDataLazyDataModelSupport<TMTDrugGroupItem> drugGroupItems;
    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs;
    private String searchText;
    private TMTDrugGroupItem selectDrugGroupItem;

    @PostConstruct
    public void postConstruct() {
        findTMTDrug();
    }

    public void showDrugGroup(TMTDrug selectedDrug) {
        for (TMTDrugGroupItem drugGroup : selectedDrug.getDrugGroupItems()) {
            LOG.debug("{} : {}", DateUtils.format("dd-MMM-yyyy", drugGroup.getDatein()), drugGroup.getDrugGroup().getId());
        }
    }

    public void searchTMTDrug() {
        if (Strings.isNullOrEmpty(searchText)) {
            findTMTDrug();
            return;
        }
        final List<String> keywords = Arrays.asList(searchText.split("\\s+"));
        tmtDrugs = new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageable) {
                Specification<TMTDrug> spec = Specifications
                        .where(TMTDrugSpecs.hasDrugGroup())
                        .and(Specifications
                                .where(TMTDrugSpecs.tmtIdContains(keywords))
                                .or(TMTDrugSpecs.fsnContains(keywords)));
                return tmtDrugService.findBySpec(spec, pageable);
            }
        };
    }

    private void findTMTDrug() {
        tmtDrugs = new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageable) {
                Specification<TMTDrug> spec = Specifications
                        .where(TMTDrugSpecs.hasDrugGroup());
                return tmtDrugService.findBySpec(spec, pageable);
            }
        };
    }

    public SpringDataLazyDataModelSupport<TMTDrugGroupItem> getDrugGroupItems() {
        return drugGroupItems;
    }

    public void setDrugGroupItems(SpringDataLazyDataModelSupport<TMTDrugGroupItem> drugGroupItems) {
        this.drugGroupItems = drugGroupItems;
    }

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public TMTDrugGroupItem getSelectDrugGroupItem() {
        return selectDrugGroupItem;
    }

    public void setSelectDrugGroupItem(TMTDrugGroupItem selectDrugGroupItem) {
        this.selectDrugGroupItem = selectDrugGroupItem;
    }

}
