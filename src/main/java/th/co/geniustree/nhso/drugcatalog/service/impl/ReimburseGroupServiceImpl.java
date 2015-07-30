/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReimburseGroupServiceImpl implements ReimburseGroupService{

    @Autowired
    private ReimburseGroupRepo reimburseGroupRepo;
    
    @Override
    public ReimburseGroup save(String id, String name, boolean isSpecialProject) {
        ReimburseGroup group = new ReimburseGroup(id, id, isSpecialProject);
        return reimburseGroupRepo.save(group);
    }

    @Override
    public ReimburseGroup findById(String id) {
        return reimburseGroupRepo.findOne(id);
    }
    
}
