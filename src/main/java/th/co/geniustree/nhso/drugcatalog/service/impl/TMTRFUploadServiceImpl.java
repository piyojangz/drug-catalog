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
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugHistory;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugUpload;
import th.co.geniustree.nhso.drugcatalog.model.TMTReleaseFileUpload;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugHistoryRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugUploadRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReleaseFileUploadRepo;
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

    @Autowired
    private TMTReleaseFileUploadRepo tmtReleaseFileUploadRepo;
    
    @Autowired
    private TMTDrugHistoryRepo tmtDrugHistoryRepo;

    @Override
    public void save(List<TMTDrugUpload> tmtDrug, List<TradeDrugExcelModel> tp, List<GenericDrugExcelModel> gpu, List<GenericDrugExcelModel> gp, List<GenericDrugExcelModel> vtm, List<GenericDrugExcelModel> subs, Date releaseDate) {
        TMTReleaseFileUpload tmtReleaseFile = tmtReleaseFileUploadRepo.save(new TMTReleaseFileUpload(releaseDate));
        saveEachEntity(tmtReleaseFile,subs);
        saveEachEntity(tmtReleaseFile,vtm);
        saveEachEntity(tmtReleaseFile,gp);
        saveEachEntity(tmtReleaseFile,gpu);
        saveEachEntity(tmtReleaseFile,tp);
        saveEachEntity(tmtReleaseFile,tmtDrug, "createDate","lastModifiedDate","version");
    }

    private void saveEachEntity(TMTReleaseFileUpload tmtReleaseFile,List<? extends TMT> tp, String... ignoredProperties) throws BeansException {
        for (TMT tmt : tp) {
            TMTDrugUpload findOne = tmtDrugUploadrepo.findOne(tmt.getTmtId());
            if (findOne == null) {
                findOne = tmtDrugUploadrepo.save(new TMTDrugUpload(tmt.getTmtId()));
            }
            BeanUtils.copyProperties(tmt, findOne, ignoredProperties);
            TMTDrugHistory history = new TMTDrugHistory();
            history.setReleaseFileUpload(tmtReleaseFile);
            BeanUtils.copyProperties(tmt, history, ignoredProperties);
            tmtDrugHistoryRepo.save(history);
        }
    }
    
}
