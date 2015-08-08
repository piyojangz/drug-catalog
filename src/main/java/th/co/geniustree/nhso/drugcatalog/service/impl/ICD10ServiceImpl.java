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
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.repo.ICD10Repo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ICD10Specs;
import th.co.geniustree.nhso.drugcatalog.service.ICD10Service;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ICD10ServiceImpl implements ICD10Service {

    @Autowired
    private ICD10Repo icd10Repo;

    @Override
    public ICD10 save(String code, String name) {
        if (name == null || name.isEmpty()) {
            name = " - ";
        }
        ICD10 icd10 = new ICD10(code, name);
        return icd10Repo.save(icd10);
    }

    @Override
    public ICD10 findByCode(String code) {
        return icd10Repo.findOne(code);
    }

    @Override
    public Page<ICD10> search(String search, Pageable pageable) {
        List<String> keyList = Arrays.asList(search.split("\\s+"));
        Specification<ICD10> spec = Specifications.where(ICD10Specs.codeLike(keyList)).or(ICD10Specs.nameLike(keyList));
        return icd10Repo.findAll(spec, pageable);
    }

}
