package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select count(u) "
            + "from UploadHospitalDrugItem u "
            + "where u.hospDrugCode = ?1 "
            + "and u.uploadDrug.hcode = ?2 "
            + "and COALESCE(u.tmtId,'NULLID') = COALESCE(?3,'NULLID') "
            + "and u.dateEffective = ?4 "
            + "and u.requestItem.status in (th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT , th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REQUEST) "
            + "and u.updateFlag = ?5 "
            + "and u.requestItem.deleted = 0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndTMTIDAndDateEffectiveAndRequestAndAccept(String hospDrugCode, String hcode, String tmtid, String dateEffective, String updateFlag);

    @Query("select count(u) "
            + "from UploadHospitalDrugItem u "
            + "where u.hospDrugCode = ?1 "
            + "and u.uploadDrug.hcode = ?2 "
            + "and COALESCE(u.tmtId,'NULLID') = COALESCE(?3,'NULLID') "
            + "and function('trunc',u.dateEffectiveDate) = function('trunc',?4) "
            + "and u.requestItem.status in (th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT , th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REQUEST) "
            + "and u.updateFlag = ?5 "
            + "and u.requestItem.deleted = 0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndTMTIDAndDateEffectiveAndRequestAndAccept(String hospDrugCode, String hcode, String tmtid, Date dateEffective, String updateFlag);
    
    @Query("select u.requestItem "
            + "from UploadHospitalDrugItem u "
            + "where u.hospDrugCode = ?1 "
            + "and u.uploadDrug.hcode = ?2 "
            + "and u.dateEffective = ?3 "
            + "and u.requestItem.status = th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REJECT "
            + "and u.updateFlag = ?4 "
            + "and u.requestItem.deleted = 0")
    public RequestItem findRejectItem(String hospDrugCode, String hcode, String dateEffective, String updateFlag);

    public Page<UploadHospitalDrugItem> findByUploadDrugId(Integer uploadDrugId, Pageable pageable);

    public List<UploadHospitalDrugItem> findByHospDrugCodeAndUploadDrugHcode(String hospDrugcode, String hcode, Sort sort);

    @Query("select count(u) "
            + "from UploadHospitalDrugItem u "
            + "where u.hospDrugCode= ?1 "
            + "and u.uploadDrug.hcode = ?2 "
            + "and function('trunc',u.dateEffectiveDate) = function('trunc',?3) "
            + "and u.requestItem.status in (th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT,th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REQUEST) "
            + "and u.updateFlag in ?4 "
            + "and u.requestItem.deleted = 0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAndRequestAndAccept(String hospDrugCode, String hcode, Date dateEffective, Collection updateFlags);

    @Query("select count(u) "
            + "from UploadHospitalDrugItem u "
            + "where u.hospDrugCode = ?1 "
            + "and u.uploadDrug.hcode = ?2 "
            + "and u.requestItem.status <> th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.IGNORED "
            + "and u.requestItem.deleted = 0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndRequestAndAccept(String hospDrugCode, String hcode);
    
    @Query("select count(u) "
            + "from UploadHospitalDrugItem u "
            + "where u.hospDrugCode = ?1 "
            + "and u.uploadDrug.hcode = ?2 "
            + "and u.requestItem.status = th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT "
            + "and u.requestItem.deleted = 0 "
            + "and u.updateFlag = 'A'")
    public long countByHospitalDrugThatFlagAAndAccept(String hospDrugCode, String hcode);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Procedure(name = "UploadHospitalDrugItem.INITIAL_HOSPITAL_DRUG")
    public void copyDataProcedure();

    public List<UploadHospitalDrugItem> findByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAfter(String hospDrugcode, String hcode, Date dateEffective);

    @Query("select count(u) "
            + "from UploadHospitalDrugItem u "
            + "where u.hospDrugCode = ?1 "
            + "and u.uploadDrug.hcode = ?2 "
            + "and u.requestItem.status <> th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.IGNORED "
            + "and u.updateFlag = ?3 "
            + "and u.requestItem.deleted = 0")
    public long countByHospDrugCodeAndUploadDrugHcodeAndRequestAndAcceptAndUpdateFlag(String hospDrugCode, String hcode, String updateFlag);

    @Query("select u "
            + "from UploadHospitalDrugItem u "
            + "where u.uploadDrug.hcode = ?1 "
            + "and u.hospDrugCode = ?2 "
            + "and COALESCE(u.tmtId,'NULLID') = COALESCE(?3,'NULLID') "
            + "and u.requestItem.status in (th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT , th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REQUEST) "
            + "and u.requestItem.deleted = 0 "
            + "and (u.updateFlag = ?4 or u.updateFlag = 'A')")
    public List<UploadHospitalDrugItem> findByUploadDrugHcodeAndHospDrugCodeAndTmtIdAndUpdateFlag(String hcode, String hospDrugCode, String tmtId, String updateFlag, Sort sort);

    @Query("select u "
            + "from UploadHospitalDrugItem u "
            + "where u.uploadDrug.hcode = ?1 "
            + "and u.hospDrugCode = ?2 "
            + "and u.updateFlag = ?3 "
            + "and u.requestItem.status = th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT "
            + "and u.requestItem.deleted = 0 "
            + "and u.requestItem.approveDate = ("
            + "     select max(u2.requestItem.approveDate) "
            + "     from UploadHospitalDrugItem u2 "
            + "     where u2.uploadDrug.hcode = ?1 "
            + "     and u2.hospDrugCode = ?2 "
            + "     and u2.updateFlag = ?3 "
            + "     and u2.requestItem.status = th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT "
            + "     and u2.requestItem.deleted = 0 )")
    public UploadHospitalDrugItem findLatestItemThatAcceptAndNotDeleteByUpdateFlag(String hcode, String hospDrugCode, String updateFlag);

    @Query(value = "select count(u) from UploadHospitalDrugItem u "
            + "where u.uploadDrug.hcode = ?1 "
            + "and u.hospDrugCode = ?2 "
            + "and u.requestItem.deleted = 0 "
            + "and u.requestItem.status in (th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT , th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.REQUEST)")
    public long countByHcodeAndHospDrugCodeThatNotDeleteAndNotReject(String hcode, String hospDrugCode);
    
    @Query(value = "select count(u) from UploadHospitalDrugItem u "
            + "where u.uploadDrug.hcode = ?1 "
            + "and u.hospDrugCode = ?2 "
            + "and function('trunc',u.dateEffectiveDate) > function('trunc',?3) "
            + "and u.updateFlag = 'A'"
            + "and u.requestItem.deleted = 0 "
            + "and u.requestItem.status = th.co.geniustree.nhso.drugcatalog.model.RequestItem.Status.ACCEPT")
    public long countByHospitalDrugThatDateEffectiveBeforeFlagA(String hcode, String hospDrugCode, Date dateEffective);
}
