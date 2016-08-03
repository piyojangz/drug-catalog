/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import th.co.geniustree.nhso.drugcatalog.input.DrugAndGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItem;

/**
 *
 * @author moth
 */
public interface TMTDrugGroupItemService {

    public void validate(DrugAndGroup drugAndGroup);

    public void save(List<DrugAndGroup> passModels);
    
    public void delete(TMTDrugGroupItem drugGroupItem);
}
