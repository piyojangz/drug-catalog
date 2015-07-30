/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;

/**
 *
 * @author Thanthathon
 */
public interface ReimburseGroupItemRepo extends JpaRepository<ReimburseGroupItem, ReimburseGroupItemPK>, JpaSpecificationExecutor<ReimburseGroupItem> {  
    
    
//    public List<ReimburseGroupItem> findByTmtDrugTmtIdAndStatusEdAndFundCodeAndBudgetYear(String tmtid , String statusEd , String fundCode , Date budgetYear);
}
