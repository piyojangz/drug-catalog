/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.BudgetYearConverter;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.FundService;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupItemService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class ReimburseGroupFinder {

    private static final Logger log = LoggerFactory.getLogger(ReimburseGroupFinder.class);

    @Autowired
    private FundService fundService;

    @Autowired
    private ReimburseGroupItemService reimburseGroupItemService;

    private TMTDrug selectedTMTDrug;
    private Fund selectedFund;
    private ICD10 selectedICD10;
    private Integer selectedBudgetYear;
    private ReimburseGroupItem returnReimburseGroupItem;
    
    private List<Fund> funds;
    private List<Integer> budgetYears;
    private SpringDataLazyDataModelSupport<ReimburseGroupItem> reimburseGroupItems;

    @PostConstruct
    public void postConstruct() {
        reset();
        initialBudgetYear();
    }

    public void reset() {
        selectedTMTDrug = new TMTDrug();
        selectedFund = new Fund();
        selectedICD10 = new ICD10();
        selectedBudgetYear = BudgetYearConverter.dateToBudgetYear(new Date());
        returnReimburseGroupItem = new ReimburseGroupItem();
    }
    
    private void initialBudgetYear() {
        Integer yearSelector = BudgetYearConverter.dateToBudgetYear(new Date());
        budgetYears = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            budgetYears.add(yearSelector - i);
        }
    }

    public void findReimburseGroupItem() {
        reimburseGroupItems = new SpringDataLazyDataModelSupport<ReimburseGroupItem>(){
            @Override
            public Page<ReimburseGroupItem> load(Pageable pageable) {
                Page<ReimburseGroupItem> page = reimburseGroupItemService.findReimburseGroupItem(selectedTMTDrug, selectedFund, selectedICD10, selectedBudgetYear,pageable);
                return page;
            }
        };
    }

    public List<Fund> completeFund(String query) {
        if (funds == null || funds.isEmpty()) {
            funds = fundService.findAll();
        }
        List<Fund> filterFunds = new ArrayList<>();
        for (Fund f : funds) {
            if (f.getName() == null) {
                f.setName(" - ");
            }
            if (f.getCode().toUpperCase().contains(query.toUpperCase()) || (f.getName().toUpperCase().contains(query.toUpperCase()))) {
                filterFunds.add(f);
            }
        }
        return filterFunds;
    }
    
    public List<Integer> completeBudgetYear(String query) {
        List<Integer> filterBudgetYear = new ArrayList<>();
        for (Integer year : budgetYears) {
            if (year.toString().startsWith(query)) {
                filterBudgetYear.add(year);
            }
        }
        return filterBudgetYear;
    }
    
    public void onSearchTMTDrugDialog() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(selectedTMTDrug.getTmtId());
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/admin/reimbursegroup/dialog/tmtdialog", options, params);
    }

    public void onSearchIcd10Dialog() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(selectedICD10.getCode());
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/admin/reimbursegroup/dialog/icd10dialog", options, params);
    }

    public void onTmtDialogReturn(SelectEvent event) {
        TMTDrug tmt = (TMTDrug) event.getObject();
        if (tmt != null) {
            selectedTMTDrug = tmt;
        }
        log.info("selected drug from search dialog is => {}", selectedTMTDrug.getTmtId());
    }

    public void onIcdDialogReturn(SelectEvent event) {
        ICD10 icd = (ICD10) event.getObject();
        if (icd != null) {
            selectedICD10 = icd;
        }
        log.info("selected icd10 from search dialog is => {}", selectedICD10.getCode());
    }

//************************ getter and setter ************************
    public TMTDrug getSelectedTMTDrug() {
        return selectedTMTDrug;
    }

    public void setSelectedTMTDrug(TMTDrug selectedTMTDrug) {
        this.selectedTMTDrug = selectedTMTDrug;
    }

    public Fund getSelectedFund() {
        return selectedFund;
    }

    public void setSelectedFund(Fund selectedFund) {
        this.selectedFund = selectedFund;
    }

    public ICD10 getSelectedICD10() {
        return selectedICD10;
    }

    public void setSelectedICD10(ICD10 selectedICD10) {
        this.selectedICD10 = selectedICD10;
    }

    public Integer getSelectedBudgetYear() {
        return selectedBudgetYear;
    }

    public void setSelectedBudgetYear(Integer selectedBudgetYear) {
        this.selectedBudgetYear = selectedBudgetYear;
    }

    public ReimburseGroupItem getReturnReimburseGroupItem() {
        return returnReimburseGroupItem;
    }

    public void setReturnReimburseGroupItem(ReimburseGroupItem returnReimburseGroupItem) {
        this.returnReimburseGroupItem = returnReimburseGroupItem;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public List<Integer> getBudgetYears() {
        return budgetYears;
    }

    public void setBudgetYears(List<Integer> budgetYears) {
        this.budgetYears = budgetYears;
    }

    public SpringDataLazyDataModelSupport<ReimburseGroupItem> getReimburseGroupItems() {
        return reimburseGroupItems;
    }

    public void setReimburseGroupItems(SpringDataLazyDataModelSupport<ReimburseGroupItem> reimburseGroupItems) {
        this.reimburseGroupItems = reimburseGroupItems;
    }

    
}
