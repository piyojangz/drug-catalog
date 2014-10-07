/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugTx;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugTxPK;
import th.co.geniustree.nhso.drugcatalog.repo.TmtDrugTxRepo;
import th.co.geniustree.nhso.drugcatalog.service.TmtDrugTxService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TmtDrugTxServiceImpl implements TmtDrugTxService {

    @Autowired
    private TmtDrugTxRepo tmtDrugTxRepo;

    @Override
    public void addNewTmtDrugTx(HospitalDrug hospitalDrug, TMTDrug tmtDrug) {
        TMTDrugTx findOne = tmtDrugTxRepo.findOne(new TMTDrugTxPK(hospitalDrug));
        if (findOne == null) {
            TMTDrugTx drgTx = tmtDrugTxRepo.save(new TMTDrugTx(tmtDrug, hospitalDrug));
            hospitalDrug.getTmtDrugTx().add(drgTx);
        }
    }

}
