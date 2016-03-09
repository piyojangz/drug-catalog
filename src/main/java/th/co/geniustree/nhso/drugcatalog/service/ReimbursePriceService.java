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
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePricePK;

/**
 *
 * @author Thanthathon
 */
public interface ReimbursePriceService {
    
    public ReimbursePrice save(String tmtid,BigDecimal price,Date dateEffective);
    
    public ReimbursePrice edit(ReimbursePrice dosageFormGroup);
    
    public void delete(ReimbursePrice dosageFormGroup);
    
    public Page<ReimbursePrice> findAllPaging(Pageable pageable);
    
    public Page<ReimbursePrice> search(String keyword, Pageable pageable);
    
    public void saveAll(List<ReimbursePrice> reimbursePrices);
    
    public boolean isExists(ReimbursePricePK id);
    
    public boolean isExists(String tmtId, Date dateEffective);
}
