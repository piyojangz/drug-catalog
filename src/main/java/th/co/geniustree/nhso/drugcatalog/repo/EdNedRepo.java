/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.EdNedPK;

/**
 *
 * @author Thanthathon
 */
public interface EdNedRepo extends JpaRepository<EdNed, EdNedPK> {

    @Query(value = "select e.pk.tmtId , e.pk.fundCode , min(e.pk.dateIn) , e.status "
            + "from EdNed e "
            + "where e.pk.tmtId = ?1 "
            + "and UPPER(e.pk.fundCode) = UPPER(?2) "
            + "group by e.pk.tmtId , e.pk.fundCode , e.status "
            + "having min(e.pk.dateIn) >= ?3")
    public List<Object[]> findByTmtDrugAndFund(String tmtId, String fundCode, Date dateIn);

}
