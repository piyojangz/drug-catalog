/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author moth
 */
public interface TMTDrugService {
    public TMTDrug findOneWithoutTx(String tmtid);
    public Page<TMTDrug> findAllAndEagerGroup(Specification<TMTDrug> s, Pageable pgbl);
    public List<TMTDrug> findTMTDrugWithFsn(String fsn);
    
}
