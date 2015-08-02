/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;

/**
 *
 * @author Thanthathon
 */
public interface ICD10Service {

    public ICD10 save(String code, String name);

    public ICD10 findByCode(String code);
    
    public Page<ICD10> findByCodeContains(String code,Pageable pageable);
}
