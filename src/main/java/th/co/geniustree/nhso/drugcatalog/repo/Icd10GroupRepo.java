/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.Icd10Group;

/**
 *
 * @author Thanthathon
 */
public interface Icd10GroupRepo  extends JpaRepository<Icd10Group, Integer>{
    
    public List<Icd10Group> findByIcd10Id(String icd10Id);
    
}
