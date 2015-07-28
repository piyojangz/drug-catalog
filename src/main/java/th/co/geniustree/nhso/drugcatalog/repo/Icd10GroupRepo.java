/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.Icd10Group;
import th.co.geniustree.nhso.drugcatalog.model.Icd10GroupPK;

/**
 *
 * @author Thanthathon
 */
public interface Icd10GroupRepo  extends JpaRepository<Icd10Group, Icd10GroupPK>{
    
    public List<Icd10Group> findByIcd10Id(String icd10Id);
    
    public List<Icd10Group> findByReimburseGroupId(String icd10Id);
    
}
