/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.GenericDrug;

/**
 *
 * @author moth
 */
public interface GenericDrugRepo extends JpaRepository<GenericDrug, String>{
    
}
