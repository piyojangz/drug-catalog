/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.math.BigDecimal;
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
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.BudgetYearConverter;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.ReimbursePriceService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class ReimbursePriceController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ReimbursePriceController.class);

    @Autowired
    private ReimbursePriceService reimbursePriceService;

    private SpringDataLazyDataModelSupport<ReimbursePrice> reimbursePrices;
    private ReimbursePrice selectedReimbursePrice;

    private TMTDrug tmtDrug;
    private BigDecimal price;
    private Integer budgetYear;

    private String keyword;
    List<Integer> budgetYears;

    @PostConstruct
    public void postConstruct() {
        reset();
        Integer yearSelector = BudgetYearConverter.dateToBudgetYear(new Date());
        budgetYears = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            budgetYears.add(yearSelector - i);
        }
        findAll();
    }

    public void reset(){
        tmtDrug = new TMTDrug();
    }
    
    public void save() {
        try {
            reimbursePriceService.save(tmtDrug.getTmtId(), price, budgetYear);
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้ อาจเป็นเพราะมีข้อมูลอยู่แล้ว");
        }
    }

    public void onSelect(ReimbursePrice selected) {
        selectedReimbursePrice = selected;
    }

    public void edit() {
        try {
            reimbursePriceService.edit(selectedReimbursePrice);
            FacesMessageUtils.info("แก้ไขข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถแก้ไขข้อมูลได้");
        }
    }

    public void delete() {
        try {
            reimbursePriceService.delete(selectedReimbursePrice);
            FacesMessageUtils.info("ลบข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้ อาจเป็นเพราะข้อมูลนี้ ถูกใช้งานอยู่");
        }
    }

    public void search() {
        reimbursePrices = new SpringDataLazyDataModelSupport<ReimbursePrice>() {
            @Override
            public Page<ReimbursePrice> load(Pageable pageAble) {
                Page<ReimbursePrice> page = reimbursePriceService.search(keyword, pageAble);
                return page;
            }
        };
    }

    private void findAll() {
        reimbursePrices = new SpringDataLazyDataModelSupport<ReimbursePrice>() {
            @Override
            public Page<ReimbursePrice> load(Pageable pageAble) {
                Page<ReimbursePrice> page = reimbursePriceService.findAll(pageAble);
                return page;
            }
        };
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

    public SpringDataLazyDataModelSupport<ReimbursePrice> getReimbursePrices() {
        return reimbursePrices;
    }

    public void setReimbursePrices(SpringDataLazyDataModelSupport<ReimbursePrice> reimbursePrices) {
        this.reimbursePrices = reimbursePrices;
    }

    public ReimbursePrice getSelectedReimbursePrice() {
        return selectedReimbursePrice;
    }

    public void setSelectedReimbursePrice(ReimbursePrice selectedReimbursePrice) {
        this.selectedReimbursePrice = selectedReimbursePrice;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
