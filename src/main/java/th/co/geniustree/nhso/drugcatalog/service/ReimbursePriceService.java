/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;

/**
 *
 * @author Thanthathon
 */
public interface ReimbursePriceService {
    
    public ReimbursePrice save(String tmtid,BigDecimal price,Integer budgetYear);
    
    public ReimbursePrice edit(ReimbursePrice dosageFormGroup);
    
    public boolean delete(ReimbursePrice dosageFormGroup);
    
    public boolean delete(String id);
    
    public List<ReimbursePrice> findAll();
    
    public Page<ReimbursePrice> findAll(Pageable pageable);
    
    public List<ReimbursePrice> search(String keyword);

    public Page<ReimbursePrice> search(String keyword, Pageable pageable);
}
