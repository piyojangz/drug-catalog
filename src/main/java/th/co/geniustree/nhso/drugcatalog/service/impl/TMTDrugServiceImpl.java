/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

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

    @Override
    public List<TMTDrug> findTMTDrugWithFsn(String fsn) {
        return tMTDrugRepo.findByFsnIgnoreCaseContaining(fsn);
    }

    @Override
    public List<TMTDrug> findTMTDrugWithFsn(String fsn, Specifications specs) {
        return tMTDrugRepo.findByFsn(fsn, specs);
    }

}
