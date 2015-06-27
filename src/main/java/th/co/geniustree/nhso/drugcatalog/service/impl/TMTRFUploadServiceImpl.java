/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.input.GenericDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.input.TradeDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.model.TMT;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugUpload;
import th.co.geniustree.nhso.drugcatalog.model.TMTReleaseFileUpload;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugUploadRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReleaseFileUploadRepo;
import th.co.geniustree.nhso.drugcatalog.service.TMTRFService;
import th.co.geniustree.nhso.drugcatalog.service.TMTRFUploadService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TMTRFUploadServiceImpl implements TMTRFUploadService{

    @Autowired
    private TMTDrugUploadRepo tmtDrugUploadrepo;
//    @Autowired
//    private TMTDrugRepo tmtDrugrepo;
    @Autowired
    private TMTReleaseFileUploadRepo tmtReleaseFileUploadRepo;

    @Override
    public void save(List<TMTDrugUpload> tmtDrug, List<TradeDrugExcelModel> tp, List<GenericDrugExcelModel> gpu, List<GenericDrugExcelModel> gp, List<GenericDrugExcelModel> vtm, List<GenericDrugExcelModel> subs, Date releaseDate) {
        saveEachEntity(subs);
        saveEachEntity(vtm);
        saveEachEntity(gp);
        saveEachEntity(gpu);
        saveEachEntity(tp);
        saveEachEntity(tmtDrug, "createDate","lastModifiedDate","version");
        tmtReleaseFileUploadRepo.save(new TMTReleaseFileUpload(releaseDate));
    }

    private void saveEachEntity(List<? extends TMT> tp, String... ignoedProperties) throws BeansException {
        for (TMT tmt : tp) {
            TMTDrugUpload findOne = tmtDrugUploadrepo.findOne(tmt.getTmtId());
            if (findOne == null) {
                findOne = tmtDrugUploadrepo.save(new TMTDrugUpload(tmt.getTmtId()));
            }
            BeanUtils.copyProperties(tmt, findOne, ignoedProperties);
        }
    }
    
}
