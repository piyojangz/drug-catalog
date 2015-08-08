/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimburseGroupSpecs;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReimburseGroupServiceImpl implements ReimburseGroupService {

    @Autowired
    private ReimburseGroupRepo reimburseGroupRepo;

    @Override
    public ReimburseGroup save(String id, String name, boolean isSpecialProject) {
        if (name == null || name.isEmpty()) {
            name = " - ";
        }
        ReimburseGroup group = new ReimburseGroup(id, name, isSpecialProject);
        return reimburseGroupRepo.save(group);
    }

    @Override
    public ReimburseGroup findByCode(String id) {
        return reimburseGroupRepo.findOne(id);
    }

    @Override
    public List<ReimburseGroup> findOnlySpecialProjectOrGroup(boolean isSpecialProject) {
        return reimburseGroupRepo.findBySpecialProject(isSpecialProject);
    }

    @Override
    public Page<ReimburseGroup> search(String keyword,Pageable pageable) {
        List<String> keyList = Arrays.asList(keyword.split("\\s+"));
        Specification<ReimburseGroup> spec = Specifications.where(ReimburseGroupSpecs.idLike(keyList)).or(ReimburseGroupSpecs.descriptionLike(keyList));
        spec = Specifications.where(ReimburseGroupSpecs.specialProjectEq(true)).and(spec);
        return reimburseGroupRepo.findAll(spec,pageable);
    }

    @Override
    public ReimburseGroup edit(ReimburseGroup reimburseGroup) {
        return reimburseGroupRepo.save(reimburseGroup);
    }

    @Override
    public Page<ReimburseGroup> findAllPaging(Pageable pageable) {
        return reimburseGroupRepo.findAll(pageable);
    }

    @Override
    public void delete(ReimburseGroup reimburseGroup) {
        reimburseGroupRepo.delete(reimburseGroup);
    }
    
    

}
