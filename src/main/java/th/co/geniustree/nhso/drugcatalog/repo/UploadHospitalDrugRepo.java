/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;

/**
 *
 * @author moth
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface UploadHospitalDrugRepo extends JpaRepository<UploadHospitalDrug, Integer>{

    public Page<UploadHospitalDrug> findByHcode(String hcode, Pageable pageReQuest);
    public Long countByShaHex(String shaHex);
    
}
