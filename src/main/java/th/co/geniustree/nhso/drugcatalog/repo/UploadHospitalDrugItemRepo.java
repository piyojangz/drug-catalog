/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.Date;
import java.util.List;
import javax.persistence.TemporalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;

/**
 *
 * @author moth
 */
public interface UploadHospitalDrugItemRepo extends JpaRepository<UploadHospitalDrugItem, Integer>, JpaSpecificationExecutor {

    @Query("select count(u) from UploadHospitalDrugItem u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 and u.dateEffective = ?3 "
            + " and u.requestItem.status <>  th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.IGNORED "
            + " and u.updateFlag = ?4 and u.requestItem.deleted=0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAndRequestAndAccept(String hospDrugCode, String hcode, String dateEffective, String updateFlag);

    @Query("select u.requestItem from UploadHospitalDrugItem u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 and u.dateEffective = ?3 "
            + "and u.requestItem.status = th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REJECT  and u.updateFlag = ?4 and u.requestItem.deleted=0")
    public RequestItem findRejectItem(String hospDrugCode, String hcode, String dateEffective, String updateFlag);

    public Page<UploadHospitalDrugItem> findByUploadDrugId(Integer uploadDrugId, Pageable pageable);

    public List<UploadHospitalDrugItem> findByHospDrugCodeAndUploadDrugHcode(String hospDrugcode, String hcode, Sort sort);

    @Query("select count(u) from UploadHospitalDrugItem u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 and u.dateEffectiveDate = ?3 "
            + " and u.requestItem.status <>  th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.IGNORED "
            + " and u.updateFlag = ?4 and u.requestItem.deleted=0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAndRequestAndAccept(String hospDrugCode, String hcode, Date dateEffective, String updateFlag);

    @Query("select count(u) from UploadHospitalDrugItem u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 and u.dateEffectiveDate >= ?3 "
            + " and u.requestItem.status <>  th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.IGNORED "
            + " and u.updateFlag in ?4 and u.requestItem.deleted=0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveMoreThanAndRequestAndAccept(String hospDrugCode, String hcode, @Temporal(TemporalType.DATE) Date dateEffective, String... updateFlag);

    @Query("select count(u) from UploadHospitalDrugItem u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 "
            + " and u.requestItem.status <>  th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.IGNORED "
            + " and u.requestItem.deleted=0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndRequestAndAccept(String hospDrugCode, String hcode);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Procedure(name = "UploadHospitalDrugItem.INITIAL_HOSPITAL_DRUG")
    public void copyDataProcedure();

    public List<UploadHospitalDrugItem> findByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAfter(String hospDrugcode, String hcode, Date dateEffective);
    
        @Query("select count(u) from UploadHospitalDrugItem u where u.hospDrugCode=?1 and u.uploadDrug.hcode = ?2 "
            + " and u.requestItem.status <>  th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.IGNORED "
            + " and u.updateFlag = ?3 and u.requestItem.deleted=0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndRequestAndAcceptAndUpdateFlag(String hospDrugCode, String hcode,String updateFlag);
}
