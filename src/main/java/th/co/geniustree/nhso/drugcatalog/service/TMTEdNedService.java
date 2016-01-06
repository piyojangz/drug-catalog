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
import th.co.geniustree.nhso.drugcatalog.input.ExcelTMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;

/**
 *
 * @author moth
 */
public interface TMTEdNedService {

    public void save(List<ExcelTMTEdNed> excelTMTEdNeds);

    public boolean exist(String tmtId, Date dateIn);

    public Page<TMTEdNed> findAll(Pageable pageable);

    public TMTEdNed save(TMTDrug tmtDrug, Date dateIn, String statusEd, Date createDate);

    public Page<TMTEdNed> findBySpec(Specification<TMTEdNed> spec, Pageable pageable);

    public void delete(TMTEdNed edNed);

    public TMTEdNed edit(TMTEdNed edNed);

}
