/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
}
