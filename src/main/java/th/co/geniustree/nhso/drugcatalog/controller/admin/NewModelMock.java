/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.EdNedPK;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class NewModelMock {

    private static final Logger log = LoggerFactory.getLogger(NewModelMock.class);

//    @Autowired
//    private DrugRepo drugRepo;
//
//    @Autowired
//    private FundRepo fundRepo;
//
//    @Autowired
//    private Icd10Repo icdRepo;
    @Autowired
    private EdNedRepo edNedRepo;

    @Autowired
    private ReimburseGroupItemRepo reimburseGroupItemRepo;

//    @Autowired
//    private ReimburseGroupRepo reimburseGroupRepo;
    private List<Fund> funds;

    private String fundId;
    private String tmtId;
    private String icd10Id;
    private Date dateIn;
    private ReimburseGroupItem reimburseGroupItem;

    @PostConstruct
    public void postConstruct() {

    }

    public void findGroup() {
        log.info("TMTID     -> {}", tmtId);
        log.info("FUND_ID   -> {}", fundId);
        log.info("ICD0_CODE -> {}", icd10Id);
        log.info("DATE_IN   -> {}", dateIn);
        EdNed edNed;
//        edNed = edNedRepo.findOne(new EdNedPK(tmtId, fundId, dateIn));
//        edNed = edNedRepo.findByTmtDrugAndFund(tmtId, fundId, dateIn);
        String edStatus = edNedRepo.findStatus(tmtId, fundId, dateIn);
//        if (edNed != null) {
            log.info("ED_STATUS -> {}",edStatus);
            reimburseGroupItem = reimburseGroupItemRepo.findOne(new ReimburseGroupItemPK(tmtId, fundId, icd10Id, edStatus));
//            log.info("REIMBURSE_GROUP_ID  \t-> {}", reimburseGroupItem.getReimburseGroup().getId());
//            log.info("REIMBURSE_GROUP_DESC\t -> {}", reimburseGroupItem.getReimburseGroup().getDescription());
//        } else {
//            log.info("ED_STATUS  no");
//        }

    }

    //****************** getter and setter method ******************
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
