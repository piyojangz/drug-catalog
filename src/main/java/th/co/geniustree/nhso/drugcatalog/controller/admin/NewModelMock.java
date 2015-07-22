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
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimburseGroupItemSpecs;

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
    private SpringDataLazyDataModelSupport<ReimburseGroupItem> reimburseGroupItems;
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
        reimburseGroupItem = new ReimburseGroupItem();
        reimburseGroupItems = new SpringDataLazyDataModelSupport<ReimburseGroupItem>() {
            @Override
            public Page<ReimburseGroupItem> load(Pageable pageAble) {
                Page<ReimburseGroupItem> page = reimburseGroupItemRepo.findAll(pageAble);

                return page;
            }
        };
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
            reimburseGroupItem = reimburseGroupItemRepo.findOne(new ReimburseGroupItemPK(tmtId, fundId.toUpperCase(), icd10Id.toUpperCase(), edNed.getStatus()));
            if (reimburseGroupItem != null) {
                log.info("REIMBURSE_GROUP_ID  \t-> {}", reimburseGroupItem.getReimburseGroup().getId());
                log.info("REIMBURSE_GROUP_DESC\t -> {}", reimburseGroupItem.getReimburseGroup().getDescription());
            } else {
                FacesMessageUtils.error("ไม่พบข้อมูลในตาราง ReimburseGroupItem");
            }

        } else {
            FacesMessageUtils.error("ไม่พบข้อมูลในตาราง Ed_STATUS");
        }
    }

    public void onDialogReturn(SelectEvent event) {
        String tmt = (String) event.getObject();
        if (tmt != null) {
            tmtId = tmt;
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

    public void onSave() {
        if (!(reimburseGroupItem.getDrug().getTmtId().isEmpty()
                || reimburseGroupItem.getFund().getFundCode().isEmpty()
                || reimburseGroupItem.getIcd10().getId().isEmpty()
                || reimburseGroupItem.getEdStatus().isEmpty())) {
            try {
                reimburseGroupItemRepo.save(reimburseGroupItem);
                FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
            } catch (Exception e) {
                FacesMessageUtils.error("บันทึกข้อมูล ไม่สำเร็จ");
            }
        }
        reimburseGroupItem = new ReimburseGroupItem();
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
