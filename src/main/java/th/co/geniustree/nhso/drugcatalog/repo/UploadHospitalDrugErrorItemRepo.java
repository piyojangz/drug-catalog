/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugErrorItem;

/**
 *
 * @author moth
 */
public interface UploadHospitalDrugErrorItemRepo extends JpaRepository<UploadHospitalDrugErrorItem, Integer>{

    public Page<UploadHospitalDrugErrorItem> findByUploadDrugId(Integer uploadId, Pageable pageAble);
    
}
