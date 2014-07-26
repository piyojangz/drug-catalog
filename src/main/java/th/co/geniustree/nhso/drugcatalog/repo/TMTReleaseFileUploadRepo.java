/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.nhso.drugcatalog.model.TMTReleaseFileUpload;

/**
 *
 * @author moth
 */
public interface TMTReleaseFileUploadRepo extends JpaRepository<TMTReleaseFileUpload, Integer>{
@Query("select u from TMTReleaseFileUpload u where u.releaseDate = (select max(x.releaseDate) from TMTReleaseFileUpload x)")
    public TMTReleaseFileUpload findLastestReleaseDate();
    
}
