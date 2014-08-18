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
}
