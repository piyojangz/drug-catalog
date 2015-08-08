/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;

/**
 *
 * @author Thanthathon
 */
public interface DosageFormGroupService {
    
    public DosageFormGroup save(String id,String description);
    
    public DosageFormGroup edit(DosageFormGroup dosageFormGroup);
    
    public boolean delete(DosageFormGroup dosageFormGroup);
    
    public boolean delete(String id);
    
    public List<DosageFormGroup> findAll();
    
    public Page<DosageFormGroup> findAll(Pageable pageable);
    
    public List<DosageFormGroup> search(String keyword);

    public Page<DosageFormGroup> search(String keyword, Pageable pageable);
    
    public DosageFormGroup findById(String id);
}
