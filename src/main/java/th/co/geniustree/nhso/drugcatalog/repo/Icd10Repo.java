/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;

/**
 *
 * @author Thanthathon
 */
public interface Icd10Repo extends JpaRepository<ICD10, String> , JpaSpecificationExecutor<ICD10>{
    
    public Page<ICD10> findByCodeContains(String search,Pageable pageable);
    
    public List<ICD10> findByCodeContains(String searchId);
}
