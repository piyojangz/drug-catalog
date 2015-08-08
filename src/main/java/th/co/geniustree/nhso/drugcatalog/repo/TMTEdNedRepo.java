/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNedPK;

/**
 *
 * @author moth
 */
public interface TMTEdNedRepo extends JpaRepository<TMTEdNed, TMTEdNedPK>, JpaSpecificationExecutor<TMTEdNed>{
    @Query(value = "select * from (select STATUS_ED from TMT_TMTEDNED where TMTID=?1 and CLASSIFIER='UC' and TRUNC(DATEIN) <= ?2 order by DATEIN desc) where ROWNUM=1",nativeQuery = true)
    public String findLastestEdByTmtId(String tmtId,Date dateEffective);
    
    
}
