/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItemTemp;

/**
 *
 * @author moth
 */
public interface UploadHospitalDrugItemTempRepo extends JpaRepository<UploadHospitalDrugItemTemp, Integer>,JpaSpecificationExecutor{
//    @Query("select count(u) from UploadHospitalDrugItemTemp u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 and u.dateEffective = ?3 "
//            + "and u.requestItem.status in  (th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REQUEST,th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT)"
//            + " and u.updateFlag = ?4")
//    public long countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAndRequestAndAccept(String hospDrugCode,String hcode,String dateEffective,String updateFlag);
//    @Query("select u.requestItem from UploadHospitalDrugItemTemp u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 and u.dateEffective = ?3 "
//            + "and u.requestItem.status = th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REJECT  and u.updateFlag = ?4")
//    public RequestItem findRejectItem(String hospDrugCode,String hcode,String dateEffective,String updateFlag);
    public Page<UploadHospitalDrugItemTemp> findByUploadDrugId(Integer uploadDrugId,Pageable pageable);
    public List<UploadHospitalDrugItemTemp> findByHospDrugCodeAndUploadDrugHcode(String hospDrugcode, String hcode, Sort sort);
    //@Query("select count")
    //public List<UploadHospitalDrugItem> findByRequestItemStatusAndRequestItemEditCountGreaterThan(RequestItem.Status status,Integer editCount);
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query(value = "delete from TMT_HOSPDRUG_TRANS where hcode = ?1",nativeQuery = true)
    public int deleteByHcode(String hcode);
    
}
