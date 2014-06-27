/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalTMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalTMTDrugPK;

/**
 *
 * @author moth
 */
public interface HospitalTMTDrugRepo extends JpaRepository<HospitalTMTDrug, HospitalTMTDrugPK> {

    @Query(countQuery = "select count(d.hospDrugCode) from HospitalTMTDrug  d where d.HospitalTMTDrug =?1 and d.hcode = ?2")
    public Integer countByHospDrugCodeAndHcode(String hospDrugCode, String hcode);

    public List<HospitalTMTDrug> findByApproved(boolean b);

    public List<HospitalTMTDrug> findByTmtIdIsNull();

    public List<HospitalTMTDrug> findByApprovedAndTmtIdIsNotNull(boolean b);
}
