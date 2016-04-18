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
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author Thanthathon
 */
public interface ReimbursePriceService {

    public ReimbursePrice save(TMTDrug tmtDrug, BigDecimal price, Date dateEffective);
    public ReimbursePriceTP save(String hcode, String hospDrugCode, TMTDrug tmtDrug, String content, String specprep, BigDecimal price, Date dateEffective);
    public ReimbursePriceTP save(HospitalDrug hospitalDrug, BigDecimal price, Date dateEffective);
    
    public ReimbursePrice edit(ReimbursePrice tmt);
    public ReimbursePriceTP edit(ReimbursePriceTP tmt);

    public void delete(ReimbursePrice tmt);
    public void delete(ReimbursePriceTP tmt);

    public Page<ReimbursePrice> findAll(Pageable pageable);
    public Page<ReimbursePriceTP> findAllTP(Pageable pageable);

    public Page<ReimbursePrice> search(String keyword, Pageable pageable);
    public Page<ReimbursePriceTP> searchTP(String keyword, Pageable pageable);

    public void save(List<ReimbursePrice> reimbursePrices);
    public void saveTP(List<ReimbursePriceTP> reimbursePrices);

    public boolean isExists(String tmtId, Date dateEffective);
    public boolean isExists(String hcode, String hospDrugCode, String tmtId, Date dateEffective);
}
