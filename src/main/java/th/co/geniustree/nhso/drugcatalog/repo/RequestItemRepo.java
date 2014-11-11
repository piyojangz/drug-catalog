/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author moth
 */
public interface RequestItemRepo extends JpaRepository<RequestItem, Integer> {

    public Page<RequestItem> findByStatus(RequestItem.Status status, Pageable pageable);

    @Query("select  r.uploadDrugItem.tmtDrug from RequestItem r  where r.status = ?1 ")
    public Page<TMTDrug> findTMTDrugByStatus(RequestItem.Status status, Pageable pageable);

    @Query("select distinct r.uploadDrugItem.tmtDrug from RequestItem r where r.status = ?1 and r.uploadDrugItem.tmtId like ?2%")
    public Page<TMTDrug> findTMTDrugByStatusAndTmtIdLike(RequestItem.Status status, String tmtid, Pageable pageable);

    @Query("select r from RequestItem r where r.status = ?1 and r.uploadDrugItem.tmtId = ?2")
    public Page<RequestItem> findByStatusAndTmtId(RequestItem.Status status, String tmtId, Pageable pageable);

    @Query("select r from RequestItem r where r.status = ?1 and r.uploadDrugItem.tmtId = ?2")
    public List<RequestItem> findAllByStatusAndTmtId(RequestItem.Status status, String tmtId);

    @Query("select distinct r from RequestItem r where r.uploadDrugItem.uploadDrug.hcode = ?1 and r.uploadDrugItem.hospDrugCode = ?2 and r.uploadDrugItem.tmtId = ?3 and r.requestDate <= ?4 order by r.requestDate asc")
    public List<RequestItem> findByStatusAndHospDrugCodeAndTmtId(String hcode, String hospDrug, String tmt, Date toDate);

    @Query("select distinct r from RequestItem r where r.status= ?1 and r.uploadDrugItem.uploadDrug.hcode = ?2")
    public Page<RequestItem> findByStatusAndHcode(RequestItem.Status status, String hcode,Pageable pageable);

}
