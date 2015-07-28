/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.ICD10Group;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;

/**
 *
 * @author Thanthathon
 */
public interface Icd10GroupService {

    public ICD10Group save(ICD10 icd10, ReimburseGroup reimburseGroup);

    public ICD10Group save(ICD10Group icd10Group);
    
    public ICD10Group findOne(String icd10Id , String reimburseGroupId);
    
    public ICD10Group findOne(ICD10Group icd10Group);
    
    public List<ICD10Group> findByIcd10Code(String icd10Id);
    
    public List<ICD10Group> findByReimburseGroupId(String reimburseGroupId);
}
