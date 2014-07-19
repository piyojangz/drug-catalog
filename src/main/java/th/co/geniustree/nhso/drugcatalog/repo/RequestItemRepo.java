/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;

/**
 *
 * @author moth
 */
public interface RequestItemRepo extends JpaRepository<RequestItem, Integer>{

    public List<RequestItem> findByStatus(RequestItem.Status status);
    
}
