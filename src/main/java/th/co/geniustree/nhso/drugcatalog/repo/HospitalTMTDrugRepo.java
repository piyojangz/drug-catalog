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
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalDrugPK;

/**
 *
 * @author moth
 */
public interface HospitalTMTDrugRepo extends JpaRepository<HospitalDrug, HospitalDrugPK> {

    @Query(countQuery = "select count(d.hospDrugCode) from HospitalTMTDrug  d where d.HospitalTMTDrug =?1 and d.hcode = ?2")
    public Integer countByHospDrugCodeAndHcode(String hospDrugCode, String hcode);

    public Page<HospitalDrug> findByHcodeAndApproved(String hcode, boolean b, Pageable page);

    public Page<HospitalDrug> findByHcodeAndTmtIdIsNull(String hcode, Pageable page);

    public Page<HospitalDrug> findByHcodeAndApprovedAndTmtIdIsNotNull(String hcode, boolean b, Pageable page);
}
