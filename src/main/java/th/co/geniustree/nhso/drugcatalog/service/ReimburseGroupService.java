/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;

/**
 *
 * @author Thanthathon
 */
public interface ReimburseGroupService {

    public ReimburseGroup save(String id, String name, boolean isSpecialProject);

    public ReimburseGroup findByCode(String id);
    
    public List<ReimburseGroup> findOnlySpecialProjectOrGroup(boolean isSpecialProject);
    
    public Page<ReimburseGroup> findAllPaging(boolean specialProject,Pageable pageable);
    
    public Page<ReimburseGroup> search(String keyword,boolean specialProject,Pageable pageable);
    
    public ReimburseGroup edit(ReimburseGroup reimburseGroup);
    
    public void delete(ReimburseGroup reimburseGroup);
}
