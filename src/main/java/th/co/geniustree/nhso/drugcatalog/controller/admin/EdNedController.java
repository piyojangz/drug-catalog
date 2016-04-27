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
import javax.annotation.Resource;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;
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

    @Autowired
    @Qualifier("TMTEdNedDeletedLogServiceImpl")
    private DeletedLogService deletedLogService;

    private TMTDrug tmtDrug;
    private Date datein;
    private String edStatus;

    private String searchType = "noSelect";
    private String searchWord;
    private String searchEdStatus;
    private Date searchStartDate;
    private Date searchEndDate;

    private TMTEdNed selectedEdNed;

    private SpringDataLazyDataModelSupport<TMTEdNed> tmtEdNeds;

    private String deleteAction;

    @PostConstruct
    public void postConstruct() {
        reset();
        findAll();
    }

    public void reset() {
        tmtDrug = new TMTDrug();
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
            LOG.error(null, e);
            FacesMessageUtils.error("บันทึกข้อมูล ไม่สำเร็จ");
        }
    }

    public void checkDateAfter() {
        if (searchEndDate.before(searchStartDate)) {
            searchEndDate = searchStartDate;
            LOG.debug("start date -> end date");
        }
    }

    public void search() {
        tmtEdNeds = new SpringDataLazyDataModelSupport<TMTEdNed>() {
            @Override
            public Page<TMTEdNed> load(Pageable pageAble) {
                Specification<TMTEdNed> spec = specifiedBySearchInput(searchWord, searchEdStatus, searchStartDate, searchEndDate);
                Page<TMTEdNed> page = tmtEdNedService.findBySpec(spec, pageAble);
                return page;
            }
        };
    }

    private Specification<TMTEdNed> specifiedBySearchInput(String tmt, String edStatus, Date start, Date end) {
        if (searchType.equals("tmt")) {
            List<String> list = Arrays.asList(tmt.split("\\s+"));
            return Specifications.where(TMTEdNedSpecs.tmtIdLike(list)).or(TMTEdNedSpecs.fsnLike(list));
        } else if (searchType.equals("status")) {
            List<String> list = Arrays.asList(edStatus.split("\\s+"));
            return Specifications.where(TMTEdNedSpecs.edStatusLike(list));
        } else if (searchType.equals("date")) {
            Specification<TMTEdNed> specs = null;
            if (start != null) {
                if (end != null) {
                    specs = Specifications.where(TMTEdNedSpecs.dateInRange(start, end));
                } else {
                    specs = Specifications.where(TMTEdNedSpecs.dateAfter(start));
                }
            } else if (end != null) {
                specs = Specifications.where(TMTEdNedSpecs.dateBefore(end));
            }
            return specs;
        } else {
            return null;
        }

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

    public void deleteED() {
        try {
            deletedLogService.createLog(selectedEdNed);
            tmtEdNedService.delete(selectedEdNed);
            FacesMessageUtils.info("ลบข้อมูลสถานะ ED เรียบร้อย กรุณาตรวจสอบข้อมูล");
        } catch (Exception e) {
            LOG.error("Can't delete TMT_TMTEDNED", e);
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้");
        }
    }

    public void cancelDelete() {
        selectedEdNed = null;
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

    public String getSearchEdStatus() {
        return searchEdStatus;
    }

    public void setSearchEdStatus(String searchEdStatus) {
        this.searchEdStatus = searchEdStatus;
    }

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(String deleteAction) {
        this.deleteAction = deleteAction;
    }

}
