/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.EdNed;
import th.co.geniustree.nhso.drugcatalog.model.EdNedPK;

/**
 *
 * @author Thanthathon
 */
public interface EdNedRepo extends JpaRepository<EdNed, EdNedPK> {

    @Query(value = "select new th.co.geniustree.nhso.drugcatalog.model.EdNed(e.pk.tmtId ,e.pk.fundId, min(e.pk.dateIn))"
            + "from EdNed e "
            + "where e.pk.tmtId = ?1 "
            + "and e.pk.fundId = ?2 "
            + "and e.pk.dateIn = ?3 "
            + "group by e.pk.tmtId , e.pk.fundId")
    public EdNed findByTmtDrugAndFund(String tmtId, String fundId, Date dateIn);
    
    @Query(value = "select e.status "
            + "from EdNed e "
            + "where e.pk.tmtId = ?1 "
            + "and e.pk.fundId = ?2 "
            + "and e.pk.dateIn = (select min(e1.pk.dateIn) from EdNed e1 having min(e1.pk.dateIn) > ?3)")
    public String findStatus(String tmtId, String fundId, Date dateIn);
    
}
