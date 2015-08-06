/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.controller.utils.BudgetYearConverter;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.ICD10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupItemService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReimburseGroupItemServiceImpl implements ReimburseGroupItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ReimburseGroupItemServiceImpl.class);

    private final String EMPTY_ICD10_CODE = "_EMPTY";

    @Autowired
    private ReimburseGroupItemRepo reimburseGroupItemRepo;

    @Autowired
    private ICD10Repo icd10Repo;

    @Override
    public ReimburseGroupItem save(TMTDrug tmtDrug, Fund fund, ICD10 icd10, ReimburseGroupItem.ED edStatus, ReimburseGroup reimburseGroup, Integer budgetYear) {
        if (isNullICD10(icd10)) {
            icd10 = icd10Repo.findOne(EMPTY_ICD10_CODE);
        }
        ReimburseGroupItem reimburseGroupItem = new ReimburseGroupItem(tmtDrug, fund, icd10, reimburseGroup, edStatus, budgetYear);
        return reimburseGroupItemRepo.save(reimburseGroupItem);
    }

    @Override
    public ReimburseGroupItem findById(String tmtid, String fundCode, String icd10Id, String reimburseGroupId, Date searchDate) {
        if (icd10Id == null || icd10Id.isEmpty()) {
            icd10Id = EMPTY_ICD10_CODE;
        }
        int budgetYear = BudgetYearConverter.dateToBudgetYear(searchDate);
        ReimburseGroupItemPK pk = new ReimburseGroupItemPK(tmtid, fundCode, icd10Id, reimburseGroupId, budgetYear);
        return reimburseGroupItemRepo.findOne(pk);
    }

    @Override
    public Page<ReimburseGroupItem> findAllPaging(Pageable pageable) {
        return reimburseGroupItemRepo.findAll(pageable);
    }

    @Override
    public Page<ReimburseGroupItem> findPagingBySpec(Specification spec, Pageable pageable) {
        return reimburseGroupItemRepo.findAll(spec, pageable);
    }

    @Override
    public List<ReimburseGroupItem> findReimburseGroupItem(String tmtid, String fundCode, String icd10Id, Date searchDate) {
        if (icd10Id == null || icd10Id.isEmpty()) {
            icd10Id = EMPTY_ICD10_CODE;
        }
        int budgetYear = BudgetYearConverter.dateToBudgetYear(searchDate);
        List<ReimburseGroupItem> items = reimburseGroupItemRepo.findbyTMTFundICD10BudgetYear(tmtid, fundCode, icd10Id, budgetYear);
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    @Override
    public Page<ReimburseGroupItem> findReimburseGroupItem(String tmtid, String fundCode, String icd10Code,Integer budgetYear, Pageable pageable) {
        if (isNullICD10(icd10Code)) {
            icd10Code = EMPTY_ICD10_CODE;
        }
        return reimburseGroupItemRepo.findbyTMTFundICD10BudgetYear(tmtid, fundCode, icd10Code, budgetYear, pageable);
    }

    @Override
    public Page<ReimburseGroupItem> findReimburseGroupItem(TMTDrug tmtDrug, Fund fund, ICD10 icd10, Date searchDate, Pageable pageable) {
        if (isNullICD10(icd10)) {
            icd10 = icd10Repo.findOne(EMPTY_ICD10_CODE);
        }
        Integer budgetYear = BudgetYearConverter.dateToBudgetYear(searchDate);
        String tmtid = tmtDrug.getTmtId();
        String fundCode = fund.getCode();
        String icd10Code = icd10.getCode();
        return reimburseGroupItemRepo.findbyTMTFundICD10BudgetYear(tmtid, fundCode, icd10Code, budgetYear, pageable);
    }

    private boolean isNullICD10(ICD10 icd10) {
        return icd10 == null || icd10.getCode() == null || icd10.getCode().isEmpty();
    }

    private boolean isNullICD10(String icd10) {
        return icd10 == null || icd10.isEmpty();
    }
}
