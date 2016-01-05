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
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTEdNedSpecs;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTEdNedService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class EdNedController {

    private static final Logger LOG = LoggerFactory.getLogger(EdNedController.class);

    @Autowired
    private TMTEdNedService tmtEdNedService;

    private TMTDrug tmtDrug;
    private Date datein;
    private String edStatus;

    private String searchWord;
    private TMTEdNed selectedEdNed;

    private SpringDataLazyDataModelSupport<TMTEdNed> tmtEdNeds;

    @PostConstruct
    public void postConstruct() {
        reset();
        findAll();
    }

    public void reset() {
        tmtDrug = new TMTDrug();
    }

    public void delete() {
        try {
            tmtEdNedService.delete(selectedEdNed);
            FacesMessageUtils.info("ลบรายการยา สำเร็จ");
        } catch (Exception e) {
            LOG.error(null,e);
            FacesMessageUtils.error("ไม่สามารถลบรายการยาได้");
        }
    }

    public void edit() {
        try {
            tmtEdNedService.edit(selectedEdNed);
            FacesMessageUtils.info("แก้ไขรายการยา สำเร็จ");
        } catch (Exception e) {
            LOG.error(null,e);
            FacesMessageUtils.error("ไม่สามารถแก้ไขรายการยาได้");
        }
    }

    public void onSelect(TMTEdNed e) {
        selectedEdNed = e;
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
            tmtEdNedService.save(tmtDrug, datein, edStatus, new GregorianCalendar().getTime());
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
        } catch (Exception e) {
            LOG.error(null,e);
            FacesMessageUtils.error("บันทึกข้อมูล ไม่สำเร็จ");
        }
    }

    public void search() {
        tmtEdNeds = new SpringDataLazyDataModelSupport<TMTEdNed>() {
            @Override
            public Page<TMTEdNed> load(Pageable pageAble) {
                Page<TMTEdNed> page = tmtEdNedService.search(searchWord, pageAble);
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
        options.put("contentWidth", 1000);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(tmtDrug.getTmtId());
        params.put("search", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/hospital/create/selectDrugDialog.xhtml", options, params);
    }

    public void onTmtDialogReturn(SelectEvent event) {
        TMTDrug tmt = (TMTDrug) event.getObject();
        if (tmt != null) {
            tmtDrug = tmt;
            LOG.info("selected drug from search dialog is => {}", tmtDrug.getTmtId());
        }
    }

//    *************************** getter and setter method
    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public Date getDatein() {
        return datein;
    }

    public void setDatein(Date datein) {
        this.datein = datein;
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

    public String getEdStatus() {
        return edStatus;
    }

    public void setEdStatus(String edStatus) {
        this.edStatus = edStatus;
    }

    public TMTEdNed getSelectedEdNed() {
        return selectedEdNed;
    }

    public void setSelectedEdNed(TMTEdNed selectedEdNed) {
        this.selectedEdNed = selectedEdNed;
    }

}
