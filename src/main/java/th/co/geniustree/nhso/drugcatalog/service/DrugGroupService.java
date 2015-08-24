/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.DrugGroup;

/**
 *
 * @author moth
 */
public interface DrugGroupService {
    public DrugGroup findOne(String id);
    
    public List<DrugGroup> findAll();
}
