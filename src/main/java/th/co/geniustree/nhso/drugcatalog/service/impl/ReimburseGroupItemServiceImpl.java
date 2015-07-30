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
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ICD10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
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

    @Override
    public ReimburseGroupItem save(TMTDrug tmtDrug, Fund fund, ICD10 icd10, ReimburseGroupItem.ED edStatus, ReimburseGroup reimburseGroup, Date budgetYear) {
        ReimburseGroupItem reimburseGroupItem = new ReimburseGroupItem(tmtDrug, fund, icd10, reimburseGroup, edStatus, budgetYear);
        return reimburseGroupItemRepo.save(reimburseGroupItem);
    }

    @Override
    public ReimburseGroupItem findById(String tmtid, String fundCode, String icd10Id,  String reimburseGroupId , Date budgetYear) {
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

}
