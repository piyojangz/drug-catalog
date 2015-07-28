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
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemID;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class NewModelController {

    private static final Logger log = LoggerFactory.getLogger(NewModelController.class);

    @Autowired
    private FundRepo fundRepo;

    @Autowired
    private EdNedRepo edNedRepo;

    @Autowired
    private ReimburseGroupItemRepo reimburseGroupItemRepo;

//    @Autowired
//    private ReimburseGroupRepo reimburseGroupRepo;
    private List<Fund> funds;
    private List<Date> dateEffectiveList;

    private String fundId;
    private String tmtId;
    private String icd10Id;
    private String edStatus;
    private Date dateIn;
    private Fund selectedFund;

    private ReimburseGroupItem reimburseGroupItem;

    private String searchTMT;
    private String searchKeyword;

    @PostConstruct
    public void postConstruct() {

    }

    public void findGroup() {
        log.info("TMTID     -> {}", tmtId);
        log.info("FUND_ID   -> {}", fundId);
        log.info("ICD0_CODE -> {}", icd10Id);
        log.info("DATE_IN   -> {}", dateIn);
        EdNed edNed;
        fundId = selectedFund.getFundCode();
        List<Object[]> obj = edNedRepo.findByTmtDrugAndFund(tmtId, fundId, dateIn);
        edNed = EdNedMapper.mapToModelAndGetFirst(obj);

        if (edNed != null) {
            log.info("edStatus -> {}", edNed.getStatus());
            reimburseGroupItem = reimburseGroupItemRepo.findOne(new ReimburseGroupItemID(tmtId, fundId.toUpperCase(), edNed.getStatus(), icd10Id.toUpperCase()));
            if (reimburseGroupItem != null) {
            } else {
                FacesMessageUtils.error("ไม่พบข้อมูลในตาราง ReimburseGroupItem");
            }

        } else {
            FacesMessageUtils.error("ไม่พบข้อมูลในตาราง Ed_STATUS");
        }
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
        keywords.add(icd10Id);
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/admin/reimbursegroup/dialog/icd10dialog", options, params);
    }

    public void onTmtDialogReturn(SelectEvent event) {
        String tmt = (String) event.getObject();
        if (tmt != null) {
            tmtId = tmt;
        }
        log.info("selected drug from search dialog is => {}", tmtId);
    }

    public void onIcdDialogReturn(SelectEvent event) {
        String icd = (String) event.getObject();
        if (icd != null) {
            icd10Id = icd;
        }
        log.info("selected drug from search dialog is => {}", tmtId);
    }

    public List<Fund> completeFund(String query) {
        funds = fundRepo.findAll();
        List<Fund> filterFunds = new ArrayList<>();
        for (Fund fund : funds) {
            if (fund.getFundCode().startsWith(query.toUpperCase()) || (fund.getName().contains(query))) {
                filterFunds.add(fund);
            }
        }
        return filterFunds;
    }

    public void onSearchTMTDrug() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(tmtId);
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/admin/reimbursegroup/dialog/tmtdialog", options, params);
    }

    //****************** getter and setter method ******************
    public String getSearchTMT() {
        return searchTMT;
    }

    public Fund getSelectedFund() {
        return selectedFund;
    }

    public void setSelectedFund(Fund selectedFund) {
        this.selectedFund = selectedFund;
    }

    public void setSearchTMT(String searchTMT) {
        this.searchTMT = searchTMT;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getIcd10Id() {
        return icd10Id;
    }

    public void setIcd10Id(String icd10Id) {
        this.icd10Id = icd10Id;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public ReimburseGroupItem getReimburseGroupItem() {
        return reimburseGroupItem;
    }

    public void setReimburseGroupItem(ReimburseGroupItem reimburseGroupItem) {
        this.reimburseGroupItem = reimburseGroupItem;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getEdStatus() {
        return edStatus;
    }

    public void setEdStatus(String edStatus) {
        this.edStatus = edStatus;
    }

    public List<Date> getDateEffectiveList() {
        return dateEffectiveList;
    }

    public void setDateEffectiveList(List<Date> dateEffectiveList) {
        this.dateEffectiveList = dateEffectiveList;
    }

}
