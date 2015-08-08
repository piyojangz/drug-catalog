/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.utils.BudgetYearConverter;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePricePK;
import th.co.geniustree.nhso.drugcatalog.repo.ReimbursePriceRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimbursePriceSpecs;
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
        Date dateEffective = BudgetYearConverter.budgetYearToDate(BudgetYearConverter.convertToCEYear(budgetYear));
        ReimbursePrice reimbursePrice = new ReimbursePrice();
        ReimbursePricePK pk = new ReimbursePricePK();
        pk.setTmtId(tmtid);
        pk.setEffectiveDate(dateEffective);
        reimbursePrice.setId(pk);
        reimbursePrice.setPrice(price);
        return reimbursePriceRepo.save(reimbursePrice);
    }

    @Override
    public ReimbursePrice edit(ReimbursePrice reimbursePrice) {
        return reimbursePriceRepo.save(reimbursePrice);
    }

    @Override
    public void delete(ReimbursePrice reimbursePrice) {
        reimbursePriceRepo.delete(reimbursePrice);
    }

    @Override
    public Page<ReimbursePrice> findAllPaging(Pageable pageable) {
        return reimbursePriceRepo.findAll(pageable);
    }

    @Override
    public Page<ReimbursePrice> search(String keyword, Pageable pageable) {
        List<String> keyList = Arrays.asList(keyword.split("\\s+"));
        Specification<ReimbursePrice> spec = Specifications.where(ReimbursePriceSpecs.tmtLike(keyList))
                .or(ReimbursePriceSpecs.fsnLike(keyList));
        return reimbursePriceRepo.findAll(spec, pageable);
    }
    
}
