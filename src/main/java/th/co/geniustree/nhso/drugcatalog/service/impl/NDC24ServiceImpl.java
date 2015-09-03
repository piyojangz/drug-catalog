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
import th.co.geniustree.nhso.drugcatalog.model.NDC24;
import th.co.geniustree.nhso.drugcatalog.repo.NDC24Repo;
import th.co.geniustree.nhso.drugcatalog.service.NDC24Service;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class NDC24ServiceImpl implements NDC24Service{

    @Autowired
    private NDC24Repo repo;
    
    @Override
    public List<NDC24> findAll() {
        return repo.findAll();
    }

    @Override
    public List<NDC24> search(String code) {
        return repo.findByNdc24Contains(code);
    }
    
}
