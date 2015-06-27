/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugUpload;

/**
 *
 * @author Thanthathon
 */
public interface TMTDrugUploadRepo extends JpaRepository<TMTDrugUpload, String>{
    
}
