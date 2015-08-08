/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.repo.DosageFormGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.DosageFormGroupSpecs;
import th.co.geniustree.nhso.drugcatalog.service.DosageFormGroupService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DosageFormGroupServiceImpl implements DosageFormGroupService {

    @Autowired
    private DosageFormGroupRepo dosageFormGroupRepo;

    @Override
    public DosageFormGroup save(String id, String description) {
        DosageFormGroup dfg = new DosageFormGroup(id, description);
        dfg.setCreateDate(new Date());
        return dosageFormGroupRepo.save(dfg);
    }

    @Override
    public DosageFormGroup edit(DosageFormGroup dosageFormGroup) {
        return dosageFormGroupRepo.save(dosageFormGroup);
    }

    @Override
    public boolean delete(DosageFormGroup dosageFormGroup) {
        try {
            dosageFormGroupRepo.delete(dosageFormGroup);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            dosageFormGroupRepo.delete(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<DosageFormGroup> findAll() {
        return dosageFormGroupRepo.findAll();
    }

    @Override
    public Page<DosageFormGroup> findAll(Pageable pageable) {
        return dosageFormGroupRepo.findAll(pageable);
    }

    @Override
    public List<DosageFormGroup> search(String keyword) {
        List<String> keywords = Arrays.asList(keyword.split("\\s+"));
        Specification spec = Specifications.where(DosageFormGroupSpecs.idGroupLike(keywords)).or(DosageFormGroupSpecs.descriptionLike(keywords));
        return dosageFormGroupRepo.findAll(spec);
    }

    @Override
    public Page<DosageFormGroup> search(String keyword, Pageable pageable) {
        List<String> keywords = Arrays.asList(keyword.split("\\s+"));
        Specification spec = Specifications.where(DosageFormGroupSpecs.idGroupLike(keywords)).or(DosageFormGroupSpecs.descriptionLike(keywords));
        return dosageFormGroupRepo.findAll(spec, pageable);
    }

    @Override
    public DosageFormGroup findById(String id) {
        return dosageFormGroupRepo.findOne(id);
    }

    @Override
    public void saveAll(List<DosageFormGroup> groups) {
        dosageFormGroupRepo.save(groups);
    }

    
}
