/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.Icd10Group;
import th.co.geniustree.nhso.drugcatalog.model.Icd10GroupPK;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.repo.Icd10GroupRepo;
import th.co.geniustree.nhso.drugcatalog.service.Icd10GroupService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class Icd10GroupServiceImpl implements Icd10GroupService{

    @Autowired
    private Icd10GroupRepo icd10GroupRepo;

    @Override
    public Icd10Group save(ICD10 icd10Id, ReimburseGroup reimburseGroupId) {
        Icd10Group icd10Group = new Icd10Group(icd10Id, reimburseGroupId);
        return icd10GroupRepo.save(icd10Group);
    }

    @Override
    public Icd10Group save(Icd10Group icd10Group) {
        return icd10GroupRepo.save(icd10Group);
    }

    @Override
    public Icd10Group findOne(String icd10Id, String reimburseGroupId) {
        return  icd10GroupRepo.findOne(new Icd10GroupPK(icd10Id, reimburseGroupId));
    }

    @Override
    public Icd10Group findOne(Icd10Group icd10Group) {
        String icd10Id = icd10Group.getIcd10().getId();
        String reimburseGroupId = icd10Group.getReimburseGroup().getId();
        return findOne(icd10Id, reimburseGroupId);
    }

    @Override
    public List<Icd10Group> findByIcd10Code(String icd10Id) {
        return icd10GroupRepo.findByIcd10Id(icd10Id);
    }

    @Override
    public List<Icd10Group> findByReimburseGroupId(String reimburseGroupId) {
        return icd10GroupRepo.findByReimburseGroupId(reimburseGroupId);
    }

    
    
    
}
