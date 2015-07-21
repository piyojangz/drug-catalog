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
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.Drug;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class NewModelMock {

    private static final Logger log = LoggerFactory.getLogger(NewModelMock.class);

    @Autowired
    private DrugRepo drugRepo;

    @Autowired
    private FundRepo fundRepo;

    @Autowired
    private Icd10Repo icdRepo;
    @Autowired
    private EdNedRepo edNedRepo;

    @Autowired
    private ReimburseGroupItemRepo reimburseGroupItemRepo;

//    @Autowired
//    private ReimburseGroupRepo reimburseGroupRepo;
    private SpringDataLazyDataModelSupport<Drug> drugs;
    private List<Fund> funds;

    private String fundId;
    private String tmtId;
    private String icd10Id;
    private Date dateIn;
    private ReimburseGroupItem reimburseGroupItem;

    private String searchTMT;

    @PostConstruct
    public void postConstruct() {

    }

    public void findGroup() {
        log.info("TMTID     -> {}", tmtId);
        log.info("FUND_ID   -> {}", fundId);
        log.info("ICD0_CODE -> {}", icd10Id);
        log.info("DATE_IN   -> {}", dateIn);
        EdNed edNed;
        List<Object[]> obj = edNedRepo.findByTmtDrugAndFund(tmtId, fundId, dateIn);
        edNed = EdNedMapper.mapToModelAndGetFirst(obj);

        if (edNed != null) {
            reimburseGroupItem = reimburseGroupItemRepo.findOne(new ReimburseGroupItemPK(tmtId, fundId.toUpperCase(), icd10Id.toUpperCase(), edNed.getStatus()));
            log.info("REIMBURSE_GROUP_ID  \t-> {}", reimburseGroupItem.getReimburseGroup().getId());
            log.info("REIMBURSE_GROUP_DESC\t -> {}", reimburseGroupItem.getReimburseGroup().getDescription());
        }

    }

    public void onDialogReturn(SelectEvent event) {
        String tmt = (String) event.getObject();
        if (tmt != null) {
            tmtId = tmt;
        }
        log.info("selected drug from search dialog is => {}", tmtId);
    }

    public List<Fund> completeFund(String query){
        funds = fundRepo.findAll();
        List<Fund> filterFunds = new ArrayList<>();
        for (Fund fund : funds) {
            if(fund.getId().toLowerCase().startsWith(query.toLowerCase())) {
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

    public void search() {
        drugs = new SpringDataLazyDataModelSupport<Drug>() {

            @Override
            public Page<Drug> load(Pageable pageAble) {
                Page<Drug> page = drugRepo.findAll(pageAble);

                return page;
            }

        };
    }

    //****************** getter and setter method ******************
    public SpringDataLazyDataModelSupport<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(SpringDataLazyDataModelSupport<Drug> drugs) {
        this.drugs = drugs;
    }

    public String getSearchTMT() {
        return searchTMT;
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
    //****************** getter and setter method ******************

}
