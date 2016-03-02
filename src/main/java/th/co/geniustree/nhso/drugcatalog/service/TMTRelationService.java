/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;

/**
 *
 * @author Thanthathon
 */
public interface TMTRelationService {
    
    public Page<TMTRelation> findAll(Pageable pageable);
    
    public Page<TMTRelation> search(String keyword,Pageable pageable);
    
    public void save(TMTRelation tmt);
    
    public void saveAll(Collection<TMTRelation> tmtList);
    
    public void delete(TMTRelation tmt);
    
    public void deleteAllChildren(TMTDrug tmtDrug);
    
    public Page<TMTRelation> findBySpec(Specification<TMTRelation> spec,Pageable pageable);
    
    public void deleteAllRelationByParent(TMTRelation relation);
    
    public boolean isRelationExist(String parentTmtId,String childTmtId);
    
}
