/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelationID;

/**
 *
 * @author Thanthathon
 */
public interface TMTRelationRepo extends JpaRepository<TMTRelation, TMTRelationID>, JpaSpecificationExecutor<TMTRelation>{
    
    public  List<TMTRelation> findByIdParentId(String parentId);
    
}
