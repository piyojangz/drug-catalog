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
import th.co.geniustree.nhso.drugcatalog.model.DrugGroup;
import th.co.geniustree.nhso.drugcatalog.repo.DrugGroupRepo;
import th.co.geniustree.nhso.drugcatalog.service.DrugGroupService;

/**
 *
 * @author moth
 */
@Service
public class DrugGroupServiceImpl implements DrugGroupService {

    @Autowired
    private DrugGroupRepo drugGroupRepo;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public DrugGroup findOne(String id) {
        return drugGroupRepo.findOne(id);
    }

}
