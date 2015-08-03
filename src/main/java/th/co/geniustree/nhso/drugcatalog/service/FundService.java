/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.model.Fund;

/**
 *
 * @author Thanthathon
 */
public interface FundService {

    public Fund findOne(String fundId);

    public Fund save(Fund fund);

    public Page<Fund> findAllPaging(Pageable pageable);

    public Page<Fund> findAllBySpecs(Specification<Fund> spec, Pageable pageable);
    
    public List<Fund> fundAll();

}
