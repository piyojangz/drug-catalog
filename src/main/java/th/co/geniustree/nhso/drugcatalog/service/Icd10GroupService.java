/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.ICD10;
import th.co.geniustree.nhso.drugcatalog.model.Icd10Group;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;

/**
 *
 * @author Thanthathon
 */
public interface Icd10GroupService {

    public Icd10Group save(ICD10 icd10, ReimburseGroup reimburseGroup);

    public Icd10Group save(Icd10Group icd10Group);
    
    public Icd10Group findOne(String icd10Id , String reimburseGroupId);
    
    public Icd10Group findOne(Icd10Group icd10Group);
    
    public List<Icd10Group> findByIcd10Code(String icd10Id);
    
    public List<Icd10Group> findByReimburseGroupId(String reimburseGroupId);
}
