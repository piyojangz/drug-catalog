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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;

/**
 *
 * @author moth
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface UploadHospitalDrugRepo extends JpaRepository<UploadHospitalDrug, Integer>{

    public Page<UploadHospitalDrug> findByHcode(String hcode, Pageable pageReQuest);
    public Long countByShaHex(String shaHex);

    public UploadHospitalDrug findByHcodeAndShaHex(String hcode, String SPECIAL_SHAHEX_VALUE);
    
    @Query("select distinct h from UploadHospitalDrug u JOIN u.hospital h JOIN h.province p where p.id = ?1")
    public List<Hospital> findAllHospitalInProvince(String provinceId);
    @Transactional(readOnly = true,propagation = Propagation.NOT_SUPPORTED)
    @Query("select u.hospital.province.nhsoZone.nhsoZone,u.hospital.province.nhsoZone.zoneName,u.hospital.province.id,u.hospital.province.name,u.hospital.hcode,u.hospital.hname,sum(u.itemCount-u.passItemCount),sum(u.passItemCount),sum(u.itemCount) "
            + "from UploadHospitalDrug u "
            + " where "
            + "case when ?1 is null then '1' else u.hospital.province.nhsoZone.nhsoZone end = case when ?1 is null then '1' else  ?1 end "
            + " and "
            + "case when ?2 is null then '1' else u.hospital.province.id end = case when ?2 is null then '1' else  ?2 end "
            + " and "
            + "case when ?3 is null then '1' else u.hospital.hcode end = case when ?3 is null then '1' else  ?3 end "
            + "group by u.hospital.province.nhsoZone.nhsoZone,u.hospital.province.nhsoZone.zoneName,u.hospital.province.id,u.hospital.province.name,u.hospital.hcode,u.hospital.hname "
            + "order by u.hospital.province.nhsoZone.nhsoZone")
    public Object[] sumUploadItemGroupByHcode(String nhsoZone,String provinceId,String hcode);
    
}
