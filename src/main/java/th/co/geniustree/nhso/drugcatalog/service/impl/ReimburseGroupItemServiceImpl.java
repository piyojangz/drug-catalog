/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.controller.admin.EdNedMapper;
import th.co.geniustree.nhso.drugcatalog.model.Drug;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ICD10Group;
import th.co.geniustree.nhso.drugcatalog.model.ICD10GroupID;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10GroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimburseGroupItemSpecs;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupItemService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReimburseGroupItemServiceImpl implements ReimburseGroupItemService {

    @Autowired
    private ReimburseGroupItemRepo reimburseGroupItemRepo;

    @Autowired
    private Icd10GroupRepo icd10GroupRepo;

    @Autowired
    private ReimburseGroupRepo reimburseGroupRepo;

    @Autowired
    private Icd10Repo icd10Repo;

    @Autowired
    private EdNedRepo edNedRepo;

    @Autowired
    private DrugRepo drugRepo;

    @Autowired
    private FundRepo fundReopo;

    @Override
    public ReimburseGroupItem save(String tmtid, String fundCode, String edStatus, String icd10Id, String reimburseGroupId, Date budgetYear) {

        Drug drug = drugRepo.findOne(tmtid);
        Fund fund = fundReopo.findOne(fundCode);
        ICD10Group icd10Group = icd10GroupRepo.findOne(new ICD10GroupID(icd10Id, reimburseGroupId));
        if (budgetYear == null) {
            budgetYear = new GregorianCalendar(2015, 9, 1).getTime();
        }
        if (hasData(tmtid, fundCode, edStatus, icd10Id, reimburseGroupId)) {
            ReimburseGroupItem reimburseGroupItem = new ReimburseGroupItem(edStatus, drug, fund, icd10Group, budgetYear);
            return reimburseGroupItemRepo.save(reimburseGroupItem);
        } else {
            return null;
        }
    }

    private boolean hasData(String tmtid, String fundCode, String edStatus, String icd10Id, String reimburseGroupId) {
        EdNed edNed = edNedRepo.findByTmtDrugAndFundAndStatus(tmtid, fundCode, edStatus);
        ICD10Group icd10Group = icd10GroupRepo.findOne(new ICD10GroupID(icd10Id, reimburseGroupId));
        return edNed != null && icd10Group != null;
    }

    @Override
    public ReimburseGroupItem save(ReimburseGroupItem item) {
        if (hasData(item.getTmtDrug().getTmtId(),
                item.getFund().getFundCode(),
                item.getStatusEd(),
                item.getIcd10Group().getIcd10().getCode(),
                item.getIcd10Group().getReimburseGroup().getId())) {
            return reimburseGroupItemRepo.save(item);
        } else {
            return null;
        }
    }

    @Override
    public List<ReimburseGroupItem> findReimburseGroupItem(String tmtid, String fundCode, String icd10Id, Date budgetYear) {
        List<Object[]> objs = edNedRepo.findByTmtDrugAndFund(tmtid, fundCode, budgetYear);
        if (objs == null) {
            return null;
        }
        EdNed edNed = EdNedMapper.mapToModelAndGetFirst(objs);
        if (edNed == null) {
            return null;
        }
        String edStatus = edNed.getStatus();
        Specification<ReimburseGroupItem> spec = Specifications.where(ReimburseGroupItemSpecs.tmtIdLike(createList(tmtid)))
                .and(ReimburseGroupItemSpecs.edStatusEq(edStatus))
                .and(ReimburseGroupItemSpecs.budgetYearEq(budgetYear));
        return reimburseGroupItemRepo.findAll(spec);
    }

    private List<String> createList(String text) {
        if (text == null) {
            return null;
        }
        String[] sp = text.split(" ");
        return Arrays.asList(sp);
    }

    @Override
    public Page<ReimburseGroupItem> findAllPaging(Pageable pageable) {
        return reimburseGroupItemRepo.findAll(pageable);
    }

    @Override
    public Page<ReimburseGroupItem> findPagingBySpec(Specification spec, Pageable pageable) {
        return reimburseGroupItemRepo.findAll(spec, pageable);
    }

}
