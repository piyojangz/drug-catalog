/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author moth
 */
public interface TMTDrugRepo extends JpaRepository<TMTDrug, String>, JpaSpecificationExecutor<TMTDrug> {

    public long countByTmtId(String tmtId);
    
    public List<TMTDrug> findByFsnIgnoreCaseContaining(String fsn);
    
    public List<TMTDrug> findByFsn(String fsn,Specification specs);
    
    public Page<TMTDrug> findByTmtIdContains(String searchTmtid,Pageable pageable);
    
    
    @Query("select distinct d.strength "
            + "from TMTDrug d "
            + "where d.fsn like ?1")
    public List<String> findStrengthByFsn(String fsn);

}