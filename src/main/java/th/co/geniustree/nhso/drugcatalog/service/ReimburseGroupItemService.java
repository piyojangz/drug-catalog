/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author Thanthathon
 */
public interface ReimburseGroupItemService {

    public ReimburseGroupItem save(TMTDrug tmtDrug, Fund fund, ICD10 icd10, ReimburseGroupItem.ED edStatus, ReimburseGroup reimburseGroup, Integer budgetYear);

    public ReimburseGroupItem findById(String tmtid, String fundCode, String icd10Id, String reimburseGroupId, Date searchDate);

    public Page<ReimburseGroupItem> findAllPaging(Pageable pageable);

    public Page<ReimburseGroupItem> findPagingBySpec(Specification spec, Pageable pageable);
    
    public List<ReimburseGroupItem> findReimburseGroupItem(String tmtid, String fundCode, String icd10Code,Date searchDate);
    
    public Page<ReimburseGroupItem> findReimburseGroupItem(String tmtid, String fundCode, String icd10Code,Integer budgetYear,Pageable pageable);
    
    public Page<ReimburseGroupItem> findReimburseGroupItem(TMTDrug tmtDrug, Fund fund, ICD10 icd10, Date searchDate, Pageable pageable);
}
