/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.controller.utils.BudgetYearConverter;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ICD10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimburseGroupItemSpecs;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupItemService;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class ReimburseGroupItemController {

    private static final Logger log = LoggerFactory.getLogger(ReimburseGroupItemController.class);

    @Autowired
    private FundRepo fundRepo;

    @Autowired
    private ICD10Repo icdRepo;

    @Autowired
    private ReimburseGroupItemService reimburseGroupItemService;

    @Autowired
    private ReimburseGroupService reimburseGroupService;

    private TMTDrug tmtDrug;
    private Fund fund;
    private TMTEdNed edNed;
    private ICD10 icd10;
    private ReimburseGroup reimburseGroup;
    private ReimburseGroupItem reimburseGroupItem;

    private Integer budgetYear;
    private String icd10Id = "";
    private String edStatus = "";
    private String searchKeyword = "";
    private ReimburseGroupItem.ED selectedEDStatus;

    private boolean specialProject = true;
    private boolean oldSelectSpecialProject = true;

    private List<Fund> funds;
    private List<ICD10> icd10s;
    private List<String> edStatusList;
    private List<ReimburseGroup> reimburseGroups;
    private List<Integer> budgetYears;
    private SpringDataLazyDataModelSupport<ReimburseGroupItem> reimburseGroupItems;

    @PostConstruct
    public void postConstruct() {
        tmtDrug = new TMTDrug();
        icd10 = new ICD10();
        initialStatusEd();
        initialBudgetYear();
        resetData();
        reimburseGroupItems = new SpringDataLazyDataModelSupport<ReimburseGroupItem>() {
            @Override
            public Page<ReimburseGroupItem> load(Pageable pageAble) {
                Page<ReimburseGroupItem> page = reimburseGroupItemService.findAllPaging(pageAble);

                return page;
            }
        };
    }

    private void initialBudgetYear() {
        Integer yearSelector = BudgetYearConverter.dateToBudgetYear(new Date());
        budgetYears = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            budgetYears.add(yearSelector - i);
        }
    }

    private void initialStatusEd() {
        edStatusList = new ArrayList<>();
        edStatusList.add("E");
        edStatusList.add("N");
        edStatusList.add("E*");
    }

    public void onSave() {
        if ((fund != null && reimburseGroup != null)) {
            try {
                reimburseGroupItemService.save(tmtDrug, fund, icd10, selectEdStatus(edStatus), reimburseGroup, budgetYear);
                FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
            } catch (Exception e) {
                FacesMessageUtils.error("บันทึกข้อมูล ไม่สำเร็จ");
            }

        } else {
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้");
        }
        resetData();
    }

    private ReimburseGroupItem.ED selectEdStatus(String ed) {
        if (ed.equalsIgnoreCase("E")) {
            return ReimburseGroupItem.ED.E;
        } else if (ed.equalsIgnoreCase("N")) {
            return ReimburseGroupItem.ED.N;
        } else if (ed.equalsIgnoreCase("E*")) {
            return ReimburseGroupItem.ED.EX;
        } else {
            return null;
        }
    }

    public void resetData() {
        icd10Id = "";
        edStatus = "";
        tmtDrug = new TMTDrug();
        fund = new Fund();
        icd10 = new ICD10();
        reimburseGroup = new ReimburseGroup();
        reimburseGroupItem = new ReimburseGroupItem();
    }

    public void search() {
        reimburseGroupItems = new SpringDataLazyDataModelSupport<ReimburseGroupItem>() {
            @Override
            public Page<ReimburseGroupItem> load(Pageable pageAble) {
                Specification spec = specify(searchKeyword);
                Page<ReimburseGroupItem> page = reimburseGroupItemService.findPagingBySpec(spec, pageAble);
                return page;
            }
        };
    }

    private Specification specify(String keyword) {
        List<String> keyList = Arrays.asList(keyword.split("\\s+"));
        Specification<ReimburseGroupItem> spec = Specifications.where(ReimburseGroupItemSpecs.tmtIdLike(keyList))
                .or(ReimburseGroupItemSpecs.icd10IdLike(keyList))
                .or(ReimburseGroupItemSpecs.icd10NameLike(keyList))
                .or(ReimburseGroupItemSpecs.reimburseGroupIdLike(keyList))
                .or(ReimburseGroupItemSpecs.fundIdLike(keyList))
                .or(ReimburseGroupItemSpecs.fundNameLike(keyList));
        return spec;
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
        keywords.add(tmtDrug.getTmtId());
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
        keywords.add(icd10.getCode());
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/admin/reimbursegroup/dialog/icd10dialog", options, params);
    }

    public void onTmtDialogReturn(SelectEvent event) {
        TMTDrug tmt = (TMTDrug) event.getObject();
        if (tmt != null) {
            tmtDrug = tmt;
        }
        log.info("selected drug from search dialog is => {}", tmtDrug.getTmtId());
    }

    public void onIcdDialogReturn(SelectEvent event) {
        ICD10 icd = (ICD10) event.getObject();
        if (icd != null) {
            icd10 = icd;
        }
        log.info("selected icd10 from search dialog is => {}", icd10.getCode());
    }

    public List<Fund> completeFund(String query) {
        if (funds == null || funds.isEmpty()) {
            funds = fundRepo.findAll();
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

    public List<ReimburseGroup> completeReimburseGroup(String query) {
        if (oldSelectSpecialProject != specialProject) {
            reimburseGroups = null;
            oldSelectSpecialProject = specialProject;
        }
        if (reimburseGroups == null || reimburseGroups.isEmpty()) {
            reimburseGroups = reimburseGroupService.findOnlySpecialProjectOrGroup(specialProject);
        }
        List<ReimburseGroup> filterGroup = new ArrayList<>();
        for (ReimburseGroup g : reimburseGroups) {
            if (g.getDescription() == null) {
                g.setDescription(" - ");
            }
            if (g.getId().toUpperCase().contains(query.toUpperCase()) || (g.getDescription().toUpperCase().contains(query.toUpperCase()))) {
                filterGroup.add(g);
            }
        }
        return filterGroup;
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

    public void onChangeSpecialProject() {
        if (isSpecialProject()) {
            edStatus = "N";
        }
    }

    //***************** getter and setter *****************
    public ReimburseGroupItem getReimburseGroupItem() {
        return reimburseGroupItem;
    }

    public void setReimburseGroupItem(ReimburseGroupItem reimburseGroupItem) {
        this.reimburseGroupItem = reimburseGroupItem;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public ICD10 getIcd10() {
        return icd10;
    }

    public void setIcd10(ICD10 icd10) {
        this.icd10 = icd10;
    }

    public TMTEdNed getEdNed() {
        return edNed;
    }

    public void setEdNed(TMTEdNed edNed) {
        this.edNed = edNed;
    }

    public ReimburseGroup getReimburseGroup() {
        return reimburseGroup;
    }

    public void setReimburseGroup(ReimburseGroup reimburseGroup) {
        this.reimburseGroup = reimburseGroup;
    }

    public String getIcd10Id() {
        return icd10Id;
    }

    public void setIcd10Id(String icd10Id) {
        this.icd10Id = icd10Id;
    }

    public String getEdStatus() {
        return edStatus;
    }

    public void setEdStatus(String edStatus) {
        this.edStatus = edStatus;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public SpringDataLazyDataModelSupport<ReimburseGroupItem> getReimburseGroupItems() {
        return reimburseGroupItems;
    }

    public void setReimburseGroupItems(SpringDataLazyDataModelSupport<ReimburseGroupItem> reimburseGroupItems) {
        this.reimburseGroupItems = reimburseGroupItems;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public List<ICD10> getIcd10s() {
        return icd10s;
    }

    public void setIcd10s(List<ICD10> icd10s) {
        this.icd10s = icd10s;
    }

    public List<String> getEdStatusList() {
        return edStatusList;
    }

    public void setEdStatusList(List<String> edStatusList) {
        this.edStatusList = edStatusList;
    }

    public List<ReimburseGroup> getReimburseGroups() {
        return reimburseGroups;
    }

    public void setReimburseGroups(List<ReimburseGroup> reimburseGroups) {
        this.reimburseGroups = reimburseGroups;
    }

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }

    public ReimburseGroupItem.ED getSelectedEDStatus() {
        return selectedEDStatus;
    }

    public void setSelectedEDStatus(ReimburseGroupItem.ED selectedEDStatus) {
        this.selectedEDStatus = selectedEDStatus;
    }

    public List<Integer> getBudgetYears() {
        return budgetYears;
    }

    public void setBudgetYears(List<Integer> budgetYears) {
        this.budgetYears = budgetYears;
    }

    public boolean isSpecialProject() {
        return specialProject;
    }

    public void setSpecialProject(boolean specialProject) {
        this.specialProject = specialProject;
    }

}
