/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.repo.ICD10Repo;
import th.co.geniustree.nhso.drugcatalog.service.ICD10Service;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ICD10ServiceImpl implements ICD10Service{

    @Autowired
    private ICD10Repo icd10Repo;
    
    @Override
    public ICD10 save(String code,String name) {
        ICD10 icd10 = new ICD10(code, name);
        return icd10Repo.save(icd10);
    }

    @Override
    public ICD10 findByCode(String code) {
        return icd10Repo.findOne(code);
    }

    @Override
    public Page<ICD10> findByCodeContains(String code, Pageable pageable) {
        return icd10Repo.findByCodeContains(code, pageable);
    }
    
}
