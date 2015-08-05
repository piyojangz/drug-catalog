/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePricePK;
import th.co.geniustree.nhso.drugcatalog.repo.ReimbursePriceRepo;
import th.co.geniustree.nhso.drugcatalog.service.ReimbursePriceService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReimbursePriceServiceImpl implements ReimbursePriceService{

    @Autowired
    private ReimbursePriceRepo reimbursePriceRepo;
    
    @Override
    public ReimbursePrice save(String tmtid, BigDecimal price, Integer budgetYear) {
        Date dateEffective = new GregorianCalendar(budgetYear, 9, 1).getTime();
        ReimbursePrice reimbursePrice = new ReimbursePrice();
        ReimbursePricePK pk = new ReimbursePricePK();
        pk.setTmtId(tmtid);
        pk.setEffectiveDate(dateEffective);
        reimbursePrice.setId(pk);
        reimbursePrice.setPrice(price);
        return reimbursePriceRepo.save(reimbursePrice);
    }

    @Override
    public ReimbursePrice edit(ReimbursePrice dosageFormGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(ReimbursePrice dosageFormGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ReimbursePrice> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<ReimbursePrice> findAll(Pageable pageable) {
        return reimbursePriceRepo.findAll(pageable);
    }

    @Override
    public List<ReimbursePrice> search(String keyword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<ReimbursePrice> search(String keyword, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
