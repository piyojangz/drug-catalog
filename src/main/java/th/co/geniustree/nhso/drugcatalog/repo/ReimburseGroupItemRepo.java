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
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;

/**
 *
 * @author Thanthathon
 */
public interface ReimburseGroupItemRepo extends JpaRepository<ReimburseGroupItem, ReimburseGroupItemPK>, JpaSpecificationExecutor<ReimburseGroupItem> {  
    
    @Query("select i "
            + "from ReimburseGroupItem i "
            + "where i.pk.tmtid = ?1 "
            + "and i.pk.fundCode = ?2 "
            + "and i.pk.icd10Code = ?3 "
            + "and i.pk.budgetYear = ?4 ")
    public List<ReimburseGroupItem> findbyTMTFundICD10BudgetYear(String tmtid, String fundCode, String icd10Code , Integer budgetYear);
    
    @Query("select i "
            + "from ReimburseGroupItem i "
            + "where i.pk.tmtid = ?1 "
            + "and i.pk.fundCode = ?2 "
            + "and i.pk.icd10Code = ?3 "
            + "and i.pk.budgetYear = ?4 ")
    public Page<ReimburseGroupItem> findbyTMTFundICD10BudgetYear(String tmtid, String fundCode, String icd10Code , Integer budgetYear,Pageable pageable);

}
