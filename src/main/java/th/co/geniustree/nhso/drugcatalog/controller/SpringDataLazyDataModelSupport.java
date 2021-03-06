/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller;

import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author moth
 */
public class SpringDataLazyDataModelSupport<T> extends LazyDataModel {
    private Sort sort;

    public SpringDataLazyDataModelSupport() {
    }

    public SpringDataLazyDataModelSupport(Sort sort) {
        this.sort = sort;
    }
    
    

    @Override
    public List load(int first, int pageSize, List multiSortMeta, Map filters) {
        PageRequest pageRequest = new PageRequest(first / pageSize, pageSize,sort);
        Page<T> page = load(pageRequest);
        setRowCount((int)page.getTotalElements());
        return page.getContent();
    }

    @Override
    public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        PageRequest pageRequest = new PageRequest(first / pageSize, pageSize,sort);
        Page<T> page = load(pageRequest);
        setRowCount((int)page.getTotalElements());
        return page.getContent();
    }

    public Page<T> load(Pageable pageable) {
        throw new IllegalStateException("Must override.");
    }

}
