/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
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
import th.co.geniustree.nhso.drugcatalog.model.Drug;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimburseGroupItemSpecs;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class ReimburseGroupItemController {

    private static final Logger log = LoggerFactory.getLogger(ReimburseGroupItemController.class);

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

    @Autowired
    private ReimburseGroupRepo reimburseGroupRepo;

    private Drug drug;
    private Fund fund;
    private ICD10 icd10;
    private EdNed edNed;
    private ReimburseGroup reimburseGroup;
    private ReimburseGroupItem reimburseGroupItem;

    private String tmtId = "";
    private String fundCode = "";
    private String icd10Id = "";
    private String edStatus = "";
    private String group = "";
    private String searchKeyword = "";

    private List<Fund> funds;
    private List<ICD10> icd10s;
    private List<String> edStatusList;
    private List<ReimburseGroup> reimburseGroups;
    private SpringDataLazyDataModelSupport<ReimburseGroupItem> reimburseGroupItems;

    @PostConstruct
    public void postConstruct() {
        edStatusList = new ArrayList<>();
        edStatusList.add("E");
        edStatusList.add("N");
        edStatusList.add("E*");
        resetData();
        reimburseGroupItems = new SpringDataLazyDataModelSupport<ReimburseGroupItem>() {
            @Override
            public Page<ReimburseGroupItem> load(Pageable pageAble) {
                Page<ReimburseGroupItem> page = reimburseGroupItemRepo.findAll(pageAble);

                return page;
            }
        };
    }

    public void onSave() {
        if (!(fund == null || reimburseGroup == null)) {
            fundCode = fund.getFundCode();
            group = reimburseGroup.getId();
            if (!(tmtId.isEmpty() || fundCode.isEmpty() || icd10Id.isEmpty() || edStatus.isEmpty() || group.isEmpty())) {
                Drug d = drugRepo.findOne(tmtId);
                Fund f = fundRepo.findOne(fundCode);
                ICD10 i = icdRepo.findOne(icd10Id);
                ReimburseGroup g = reimburseGroupRepo.findOne(group);
                reimburseGroupItem = new ReimburseGroupItem(edStatus, d, f, i, g);
                try {
                    reimburseGroupItemRepo.save(reimburseGroupItem);
                    FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
                } catch (Exception e) {
                    FacesMessageUtils.error("บันทึกข้อมูล ไม่สำเร็จ");
                }
            }
        } else {

            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้");
        }
        resetData();
    }

    public void resetData() {
        tmtId = "";
        fundCode = "";
        icd10Id = "";
        edStatus = "";
        group = "";
        drug = new Drug();
        fund = new Fund();
        icd10 = new ICD10();
        edNed = new EdNed();
        reimburseGroup = new ReimburseGroup();
        reimburseGroupItem = new ReimburseGroupItem();
    }

    public void search() {
        reimburseGroupItems = new SpringDataLazyDataModelSupport<ReimburseGroupItem>() {
            @Override
            public Page<ReimburseGroupItem> load(Pageable pageAble) {
                Specification spec = specify(searchKeyword);
                Page<ReimburseGroupItem> page = reimburseGroupItemRepo.findAll(spec, pageAble);
                return page;
            }
        };
    }

    private Specification specify(String keyword) {
        List<String> keyList = Arrays.asList(keyword.split(" "));
        Specification<ReimburseGroupItem> spec = Specifications.where(ReimburseGroupItemSpecs.tmtIdLike(keyList))
                .or(ReimburseGroupItemSpecs.fundIdLike(keyList))
                .or(ReimburseGroupItemSpecs.fundNameLike(keyList))
                .or(ReimburseGroupItemSpecs.icd10IdLike(keyList));
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
        keywords.add(tmtId);
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
        for (Fund f : funds) {
            if (f.getFundCode().toUpperCase().contains(query.toUpperCase()) || (f.getName().toUpperCase().contains(query.toUpperCase()))) {
                filterFunds.add(f);
            }
        }
        return filterFunds;
    }

    public List<ICD10> completeIcd10(String query) {
        icd10s = icdRepo.findAll();
        List<ICD10> filterIcd10 = new ArrayList<>();
        for (ICD10 i : icd10s) {
            if (i.getId().toUpperCase().contains(query.toUpperCase()) || (i.getName().toUpperCase().contains(query.toUpperCase()))) {
                filterIcd10.add(i);
            }
        }
        return filterIcd10;
    }

    public List<ReimburseGroup> completeReimburseGroup(String query) {
        reimburseGroups = reimburseGroupRepo.findAll();
        List<ReimburseGroup> filterGroup = new ArrayList<>();
        for (ReimburseGroup g : reimburseGroups) {
            if (g.getId().toUpperCase().contains(query.toUpperCase()) || (g.getDescription().toUpperCase().contains(query.toUpperCase()))) {
                filterGroup.add(g);
            }
        }
        return filterGroup;
    }

    //***************** getter and setter *****************
    public ReimburseGroupItem getReimburseGroupItem() {
        return reimburseGroupItem;
    }

    public void setReimburseGroupItem(ReimburseGroupItem reimburseGroupItem) {
        this.reimburseGroupItem = reimburseGroupItem;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
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

    public EdNed getEdNed() {
        return edNed;
    }

    public void setEdNed(EdNed edNed) {
        this.edNed = edNed;
    }

    public ReimburseGroup getReimburseGroup() {
        return reimburseGroup;
    }

    public void setReimburseGroup(ReimburseGroup reimburseGroup) {
        this.reimburseGroup = reimburseGroup;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

}