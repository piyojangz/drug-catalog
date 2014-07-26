/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.GenericDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTReleaseFileUpload;
import th.co.geniustree.nhso.drugcatalog.model.TradeDrug;
import th.co.geniustree.nhso.drugcatalog.repo.GenericDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReleaseFileUploadRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TradeDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.TMTRFService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TMTRFServiceImpl implements TMTRFService {

    @Autowired
    private TMTDrugRepo tmtDrugrepo;
    @Autowired
    private TradeDrugRepo tradeDrugRepo;
    @Autowired
    private GenericDrugRepo genericDrugRepo;
    @Autowired
    private TMTReleaseFileUploadRepo tmtReleaseFileUploadRepo;

    @Override
    public void save(List<TMTDrug> tmtDrug, List<TradeDrug> tp, List<GenericDrug> gpu, List<GenericDrug> gp, List<GenericDrug> vtm, List<GenericDrug> subs,Date releaseDate) {
        tmtDrugrepo.save(tmtDrug);
        tradeDrugRepo.save(tp);
        genericDrugRepo.save(gpu);
        genericDrugRepo.save(gp);
        genericDrugRepo.save(vtm);
        genericDrugRepo.save(subs);
        tmtReleaseFileUploadRepo.save(new TMTReleaseFileUpload(releaseDate));
    }

}
