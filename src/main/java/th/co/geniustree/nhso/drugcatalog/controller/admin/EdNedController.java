/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
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
import th.co.geniustree.nhso.drugcatalog.controller.utils.BudgetYearConverter;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.repo.spec.EdNedSpecs;
import th.co.geniustree.nhso.drugcatalog.service.FundService;
import th.co.geniustree.nhso.drugcatalog.service.TMTEdNedService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class EdNedController {

    private static final Logger log = LoggerFactory.getLogger(EdNedController.class);

    @Autowired
    private TMTEdNedService tmtEdNedService;

    @Autowired
    private FundService fundService;

    private TMTDrug tmtDrug;
    private Fund fund;
    private Date budgetYear;
    private String edStatus;

    private String searchWord;
    private TMTEdNed selectedEdNed;

    private SpringDataLazyDataModelSupport<TMTEdNed> tmtEdNeds;

    private List<Integer> budgetYears;
    private List<String> edStatusList;
    private List<Fund> funds;

    @PostConstruct
    public void postConstruct() {
        reset();
        findAll();
        initialBudgetYear();
        initialStatusEd();
    }

    public void reset() {
        tmtDrug = new TMTDrug();
        fund = new Fund();
    }

    public void delete() {
        try {
            tmtEdNedService.delete(selectedEdNed);
            FacesMessageUtils.info("ลบรายการยา สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถลบรายการยาได้");
        }
    }

    public void edit() {
        try {
            tmtEdNedService.edit(selectedEdNed);
            FacesMessageUtils.info("แก้ไขรายการยา สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถแก้ไขรายการยาได้");
        }
    }

    public void onSelect(TMTEdNed e) {
        selectedEdNed = e;
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

    public void findAll() {
        tmtEdNeds = new SpringDataLazyDataModelSupport<TMTEdNed>() {
            @Override
            public Page<TMTEdNed> load(Pageable pageAble) {
                Page<TMTEdNed> page = tmtEdNedService.findAll(pageAble);
                return page;
            }
        };
    }

    public void onSave() {

        try {
            tmtEdNedService.save(tmtDrug, fund,budgetYear, edStatus, new GregorianCalendar().getTime());
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("บันทึกข้อมูล ไม่สำเร็จ");
        }
    }

    public void search() {
        tmtEdNeds = new SpringDataLazyDataModelSupport<TMTEdNed>() {
            @Override
            public Page<TMTEdNed> load(Pageable pageAble) {
                Specification spec = specify(searchWord);
                Page<TMTEdNed> page = tmtEdNedService.findBySpec(spec, pageAble);
                return page;
            }
        };
    }

    private Specification<TMTEdNed> specify(String search) {
        List<String> searches = Arrays.asList(search.split("\\s+"));
        Specification<TMTEdNed> spec = Specifications.where(EdNedSpecs.tmtIdLike(searches));
//                .or(EdNedSpecs.fundCodeLike(searches))
//                .or(EdNedSpecs.fundNameLike(searches));
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

    public void onTmtDialogReturn(SelectEvent event) {
        TMTDrug tmt = (TMTDrug) event.getObject();
        if (tmt != null) {
            tmtDrug = tmt;
        }
        log.info("selected drug from search dialog is => {}", tmtDrug.getTmtId());
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

//    *************************** getter and setter method
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

    public Date getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Date budgetYear) {
        this.budgetYear = budgetYear;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public SpringDataLazyDataModelSupport<TMTEdNed> getTmtEdNeds() {
        return tmtEdNeds;
    }

    public void setTmtEdNeds(SpringDataLazyDataModelSupport<TMTEdNed> tmtEdNeds) {
        this.tmtEdNeds = tmtEdNeds;
    }

    public List<Integer> getBudgetYears() {
        return budgetYears;
    }

    public void setBudgetYears(List<Integer> budgetYears) {
        this.budgetYears = budgetYears;
    }

    public String getEdStatus() {
        return edStatus;
    }

    public void setEdStatus(String edStatus) {
        this.edStatus = edStatus;
    }

    public List<String> getEdStatusList() {
        return edStatusList;
    }

    public void setEdStatusList(List<String> edStatusList) {
        this.edStatusList = edStatusList;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public TMTEdNed getSelectedEdNed() {
        return selectedEdNed;
    }

    public void setSelectedEdNed(TMTEdNed selectedEdNed) {
        this.selectedEdNed = selectedEdNed;
    }

}
