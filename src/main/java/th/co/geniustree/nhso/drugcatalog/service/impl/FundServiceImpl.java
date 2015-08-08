/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;
import th.co.geniustree.nhso.drugcatalog.service.FundService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class FundServiceImpl implements FundService{
    
    @Autowired
    private FundRepo fundRepo;

    @Override
    public Fund findOne(String fundId) {
        return fundRepo.findOne(fundId);
    }

    @Override
    public Fund save(Fund fund) {
        String fundName = fund.getName();
        if(fundName == null || fundName.isEmpty()){
            fund.setName(" - ");
        }
        return fundRepo.save(fund);
    }

    @Override
    public Page<Fund> findAllPaging(Pageable pageable) {
        return fundRepo.findAll(pageable);
    }

    @Override
    public Page<Fund> findAllBySpecs(Specification<Fund> spec, Pageable pageable) {
        return fundRepo.findAll(spec, pageable);
    }

    @Override
    public List<Fund> findAll() {
        return fundRepo.findAll();
    }

    @Override
    public void delete(Fund fund) {
        fundRepo.delete(fund);
    }

   
    
}
