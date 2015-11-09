/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author thanthathon.b
 */
public class UploadHospitalDrugItemServiceImpl implements UploadHospitalDrugItemService{

    @Autowired
    private UploadHospitalDrugItemRepo repo;
    
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    
    @Override
    public long countTotalHospitalDrug(String hcode, String hospDrugCode, String tmtid) {
        return repo.countByHospitalDrug(hcode, hospDrugCode, tmtid);
    }

    @Override
    public boolean isChangeTmt(String hcode, String hospDrugCode, String tmtid) {
        HospitalDrug hospitalDrug = hospitalDrugRepo.findByHcodeAndHospDrugCodeAndTmtId(hcode, hospDrugCode, tmtid);
        return !hospitalDrug.getTmtId().equals(tmtid);
    }
    
    
}
