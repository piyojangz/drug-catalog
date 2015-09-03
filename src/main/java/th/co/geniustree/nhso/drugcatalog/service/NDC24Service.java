/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.NDC24;

/**
 *
 * @author Thanthathon
 */
public interface NDC24Service {
    
    public List<NDC24> findAll();
    
    public List<NDC24> search(String code);
    
}
