/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;

/**
 *
 * @author moth
 */
public interface UploadHospitalDrugItemRepo extends JpaRepository<UploadHospitalDrugItem, Integer>{
    public long countByHospDrugCodeAndUploadDrugHcodeAndDateChange(String hospDrugCode,String hcode,String dateChange);
    public long countByHospDrugCodeAndUploadDrugHcodeAndDateUpdate(String hospDrugCode,String hcode,String dateChange);
    public Page<UploadHospitalDrugItem> findByUploadDrugId(Integer uploadDrugId,Pageable pageable);
}
