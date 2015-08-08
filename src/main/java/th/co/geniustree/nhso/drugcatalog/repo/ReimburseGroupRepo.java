/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;

/**
 *
 * @author Thanthathon
 */
public interface ReimburseGroupRepo extends JpaRepository<ReimburseGroup, String>,JpaSpecificationExecutor<ReimburseGroup>{
    public List<ReimburseGroup> findByIdContains(String searchId);
    
    public List<ReimburseGroup> findBySpecialProject(boolean isSpecialProject);
}
