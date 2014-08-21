/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;

/**
 *
 * @author moth
 */
public interface HospitalDrugRepo extends JpaRepository<HospitalDrug, HospitalDrugPK>, JpaSpecificationExecutor {

    @Query(countQuery = "select count(d.hospDrugCode) from HospitalDrug  d where d.hospDrugCode =?1 and d.hcode = ?2 and d.approved = ?3")
    public Integer countByHospDrugCodeAndHcodeAndApproved(String hospDrugCode, String hcode, boolean approved);

    public Integer countByHcodeAndTmtIdAndApprovedIsTrue(String hospDrugCode, String hcode);

    public Page<HospitalDrug> findByHcodeAndApproved(String hcode, boolean b, Pageable page);

    public Page<HospitalDrug> findByHcodeAndTmtIdIsNull(String hcode, Pageable page);

    public Page<HospitalDrug> findByHcodeAndApprovedAndTmtIdIsNotNull(String hcode, boolean b, Pageable page);

    public Long countByHcodeAndTmtId(String hcode,String tmtId);
    
    @Transactional(readOnly = true,propagation = Propagation.NOT_SUPPORTED)
    @Query("select u.hospital.province.nhsoZone.nhsoZone,u.hospital.province.nhsoZone.zoneName,u.hospital.province.id,u.hospital.province.name,u.hospital.hcode,u.hospital.hname,"
            + "count(case when u.tmtId is not null and u.approved=true then '1' else null end),"
            + "count(case when u.tmtId is not null and u.approved=false  then '1' else null end),"
            + "count(case when u.tmtId is null then '1' else null end),"
            + "count(u) "
            + "from HospitalDrug u "
            + " where "
            + "case when ?1 is null then '1' else u.hospital.province.nhsoZone.nhsoZone end = case when ?1 is null then '1' else  ?1 end "
            + " and "
            + "case when ?2 is null then '1' else u.hospital.province.id end = case when ?2 is null then '1' else  ?2 end "
            + " and "
            + "case when ?3 is null then '1' else u.hospital.hcode end = case when ?3 is null then '1' else  ?3 end "
            + "group by u.hospital.province.nhsoZone.nhsoZone,u.hospital.province.nhsoZone.zoneName,u.hospital.province.id,u.hospital.province.name,u.hospital.hcode,u.hospital.hname "
            + "order by u.hospital.province.nhsoZone.nhsoZone")
    public Object[] countTmtGroupByHcode(String nhsoZone,String provinceId,String hcode);
}
