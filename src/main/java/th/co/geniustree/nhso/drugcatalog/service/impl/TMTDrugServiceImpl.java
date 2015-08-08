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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author moth
 */
@Service
public class TMTDrugServiceImpl implements TMTDrugService {

    @Autowired
    private TMTDrugRepo tMTDrugRepo;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    @Override
    public TMTDrug findOneWithoutTx(String tmtid) {
        return tMTDrugRepo.findOne(tmtid);
    }

    @Override
    public Page<TMTDrug> findAllAndEagerGroup(Specification<TMTDrug> spec, Pageable pgbl) {
        Page<TMTDrug> findAll = tMTDrugRepo.findAll(spec, pgbl);
//        for (TMTDrug tmtDrug : findAll) {
//            tmtDrug.getDrugGroupItems().size();
//        }
        return findAll;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public Page<TMTDrug> search(String searchTmtid, Pageable pageable) {
        List<String> keyList = Arrays.asList(searchTmtid.split("\\s+"));
        Specification<TMTDrug> spec = Specifications.where(TMTDrugSpecs.tmtIdContains(keyList)).or(TMTDrugSpecs.fsnContains(keyList));
        return tMTDrugRepo.findAll(spec,pageable);
    }

}
