/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePricePK;

/**
 *
 * @author Thanthathon
 */
public interface ReimbursePriceRepo extends JpaRepository<ReimbursePrice, ReimbursePricePK>, JpaSpecificationExecutor<ReimbursePrice>{
    
}
