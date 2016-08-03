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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;
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
    
    @Autowired
    @Qualifier("TMTReimbursePriceDeletedLogServiceImpl")
    private DeletedLogService deletedLogService;

    private SpringDataLazyDataModelSupport<ReimbursePrice> reimbursePrices;
    private ReimbursePrice selectedReimbursePrice;

    private TMTDrug tmtDrug;
    private BigDecimal price;
    private Date dateEffective;

    private String keyword;
    
    @PostConstruct
    public void postConstruct() {
        findAll();
    }

    public void save() {
        try {
            reimbursePriceService.save(tmtDrug, price, dateEffective);
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้");
            LOG.debug("can't save", e);
        }
    }

    public void onSelect(ReimbursePrice selected) {
        selectedReimbursePrice = selected;
    }

    public void search() {
        reimbursePrices = new SpringDataLazyDataModelSupport<ReimbursePrice>(new Sort("id.tmtId")) {
            @Override
            public Page<ReimbursePrice> load(Pageable pageable) {
                return reimbursePriceService.search(keyword, pageable);
            }
        };
    }

    private void findAll() {
        reimbursePrices = new SpringDataLazyDataModelSupport<ReimbursePrice>(new Sort("id.tmtId")) {
            @Override
            public Page<ReimbursePrice> load(Pageable pageAble) {
                return reimbursePriceService.findAll(pageAble);
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
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/hospital/create/selectDrugDialog.xhtml", options, params);
    }

    public void onTmtDialogReturn(SelectEvent event) {
        TMTDrug tmt = (TMTDrug) event.getObject();
        if (tmt != null) {
            tmtDrug = tmt;
        }
    }
    
    public void delete() {
        try {
            deletedLogService.createLog(selectedReimbursePrice);
            reimbursePriceService.delete(selectedReimbursePrice);
            FacesMessageUtils.info("ลบข้อมูลราคายา เรียบร้อย กรุณาตรวจสอบข้อมูล");
        } catch (Exception e) {
            LOG.error("Can't delete TMT_TMTEDNED", e);
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้");
        }
    }

    public void cancelDelete() {
        selectedReimbursePrice = null;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

}
