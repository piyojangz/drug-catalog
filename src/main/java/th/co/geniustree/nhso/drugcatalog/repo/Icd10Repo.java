/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.ICD10;

/**
 *
 * @author Thanthathon
 */
public interface Icd10Repo extends JpaRepository<ICD10, String>{
    
}
