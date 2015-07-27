/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Date;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;

/**
 *
 * @author Thanthathon
 */
public interface ReimburseGroupItemService {
    
    public ReimburseGroupItem save(String tmtid , String fundCode, String edStatus, String icd10Id , String reimburseGroupId);
    
    public ReimburseGroupItem save(ReimburseGroupItem reimburseGroupItem);
    
    public ReimburseGroupItem findReimburseGroup(String tmtid , String fundCode , String icd10Id, Date dateIn);
}
