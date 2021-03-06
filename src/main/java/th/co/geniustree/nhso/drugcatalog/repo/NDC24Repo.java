/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.NDC24;

/**
 *
 * @author Thanthathon
 */
public interface NDC24Repo extends JpaRepository<NDC24, String>{
    
    public List<NDC24> findByNdc24Contains(String code);
    
}
