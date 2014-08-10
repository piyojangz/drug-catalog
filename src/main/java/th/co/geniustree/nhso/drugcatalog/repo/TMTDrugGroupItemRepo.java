/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItemPK;

/**
 *
 * @author moth
 */
public interface TMTDrugGroupItemRepo extends JpaRepository<TMTDrugGroupItem, TMTDrugGroupItemPK>{
    public long countByDrugGroupId(String drugGroupId);
    @Query("select a from TMTDrugGroupItem a where a.tmtDrug.tmtId = ?1 order by a.drugGroup.id asc")
    public List<TMTDrugGroupItem> findByTMTDrugTMTId(String tmtId);
}
