/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.admin.EdNedMapper;
import th.co.geniustree.nhso.drugcatalog.model.Drug;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.Icd10Group;
import th.co.geniustree.nhso.drugcatalog.model.Icd10GroupPK;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.repo.DrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.EdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10GroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;
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
    public ReimburseGroupItem save(String tmtid, String fundCode, String edStatus, String icd10Id, String reimburseGroupId) {

        Drug drug = drugRepo.findOne(tmtid);
        Fund fund = fundReopo.findOne(fundCode);
        ICD10 icd10 = icd10Repo.findOne(icd10Id);
        ReimburseGroup reimburseGroup = reimburseGroupRepo.findOne(reimburseGroupId);
        if (hasData(tmtid, fundCode, edStatus, icd10Id, reimburseGroupId)) {
            ReimburseGroupItem reimburseGroupItem = new ReimburseGroupItem(edStatus, drug, fund, icd10, reimburseGroup);
            return reimburseGroupItemRepo.save(reimburseGroupItem);
        } else {
            return null;
        }
    }

    private boolean hasData(String tmtid, String fundCode, String edStatus, String icd10Id, String reimburseGroupId) {
        EdNed edNed = edNedRepo.findByTmtDrugAndFundAndStatus(tmtid, fundCode, edStatus);
        Icd10Group icd10Group = icd10GroupRepo.findOne(new Icd10GroupPK(icd10Id, reimburseGroupId));
        return edNed != null && icd10Group != null;
    }

    @Override
    public ReimburseGroupItem save(ReimburseGroupItem item) {
        if (hasData(item.getDrug().getTmtId(),
                item.getFund().getFundCode(),
                item.getEdStatus(),
                item.getIcd10().getId(),
                item.getReimburseGroup().getId())) {
            return reimburseGroupItemRepo.save(item);
        } else {
            return null;
        }
    }

    @Override
    public ReimburseGroupItem findReimburseGroup(String tmtid, String fundCode, String icd10Id, Date dateIn) {
        List<Object[]> objs = edNedRepo.findByTmtDrugAndFund(tmtid, fundCode, dateIn);
        if (objs == null) {
            return null;
        }
        EdNed edNed = EdNedMapper.mapToModelAndGetFirst(objs);
        if (edNed == null) {
            return null;
        }
        String edStatus = edNed.getStatus();
        return reimburseGroupItemRepo.findOne(new ReimburseGroupItemPK(tmtid, fundCode, edStatus, icd10Id));
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
