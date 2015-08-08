/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author Thanthathon
 */
public class DrugServiceImpl implements  TMTDrugService{
    
    @Autowired
    private TMTDrugRepo tmtDrugRepo;

    @Override
    public TMTDrug findOneWithoutTx(String tmtid) {
        return tmtDrugRepo.findOne(tmtid);
    }

    @Override
    public Page<TMTDrug> findAllAndEagerGroup(Specification<TMTDrug> s, Pageable pgbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<TMTDrug> search(String searchTmtid, Pageable pageable) {
        List<String> keyList = Arrays.asList(searchTmtid.split("\\s+"));
        Specification<TMTDrug> spec = Specifications.where(TMTDrugSpecs.tmtIdContains(keyList)).or(TMTDrugSpecs.fsnContains(keyList));
        return tmtDrugRepo.findAll(spec,pageable);
    }
    
}
