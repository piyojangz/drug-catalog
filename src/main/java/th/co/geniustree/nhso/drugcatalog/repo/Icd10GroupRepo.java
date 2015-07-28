/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.ICD10Group;
import th.co.geniustree.nhso.drugcatalog.model.ICD10GroupID;

/**
 *
 * @author Thanthathon
 */
public interface Icd10GroupRepo  extends JpaRepository<ICD10Group, ICD10GroupID>{
    
    public List<ICD10Group> findByReimburseGroupId(String icd10Id);
    
}
