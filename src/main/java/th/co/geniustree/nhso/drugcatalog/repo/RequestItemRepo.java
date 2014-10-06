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
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author moth
 */
public interface RequestItemRepo extends JpaRepository<RequestItem, Integer> {

    public Page<RequestItem> findByStatus(RequestItem.Status status, Pageable pageable);

    @Query("select distinct t from RequestItem r join r.targetItem u join u.tmtDrug t where r.status = ?1")
    public Page<TMTDrug> findTMTDrugByStatus(RequestItem.Status status, Pageable pageable);

    @Query("select distinct t from RequestItem r join r.targetItem u join u.tmtDrug t where r.status = ?1 and t.tmtId like ?2%")
    public Page<TMTDrug> findTMTDrugByStatusAndTmtIdLike(RequestItem.Status status, String tmtid, Pageable pageable);

    @Query("select distinct r from RequestItem r join r.targetItem u join u.tmtDrug t where r.status = ?1 and t.tmtId = ?2")
    public Page<RequestItem> findByStatusAndTmtId(RequestItem.Status status, String tmtId, Pageable pageable);

    @Query("select distinct r from RequestItem r join r.targetItem u join u.tmtDrug t where r.status = ?1 and t.tmtId = ?2")
    public List<RequestItem> findByStatusAndTmtId(RequestItem.Status status, String tmtId);

    public RequestItem findByTargetItemHcodeAndTargetItemHospDrugCodeAndTargetItemTmtId(String hcode, String hospDrug, String tmt);

}
