/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.DuplicateCheckFacade;

/**
 *
 * @author moth
 */
@Service
public class DuplicateCheckFacadeImpl implements DuplicateCheckFacade {

    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;

    @Override
    public void checkDuplicateInDatabase(HospitalDrugExcelModel uploadDrugModel) {
        if ("U".equalsIgnoreCase(uploadDrugModel.getUpdateFlag())) {
            checkDuplicateForUpdateFlageU(uploadDrugModel);
        } else if ("A".equalsIgnoreCase(uploadDrugModel.getUpdateFlag())) {
            checkDuplicateForUpdateFlagA(uploadDrugModel);
        } else {
            checkDuplicateForUpdateFlagAED(uploadDrugModel);
        }
    }

    private void checkDuplicateForUpdateFlageU(HospitalDrugExcelModel uploadDrugModel) {
        long countByHospDrugCodeAndUploadDrugHcodeAndDateUpdate = uploadHospitalDrugItemRepo.countByHospDrugCodeAndUploadDrugHcodeAndDateUpdate(uploadDrugModel.getHospDrugCode(), uploadDrugModel.getHcode(), uploadDrugModel.getDateUpdate());
        if (countByHospDrugCodeAndUploadDrugHcodeAndDateUpdate > 0) {
            uploadDrugModel.addError("dateUpdate", "Flag U at dateUpdate is already exist.");
        }
    }

    private void checkDuplicateForUpdateFlagAED(HospitalDrugExcelModel uploadDrugModel) {
        long countByHospDrugCodeAndUploadDrugHcodeAndDateChange = uploadHospitalDrugItemRepo.countByHospDrugCodeAndUploadDrugHcodeAndDateChange(uploadDrugModel.getHospDrugCode(), uploadDrugModel.getHcode(), uploadDrugModel.getDateChange());
        if (countByHospDrugCodeAndUploadDrugHcodeAndDateChange > 0) {
            uploadDrugModel.addError("dateChange", "Flag E,D at dateChange is already exist.");
        }
    }

    private void checkDuplicateForUpdateFlagA(HospitalDrugExcelModel uploadDrugModel) {
        boolean exists = hospitalDrugRepo.exists(new HospitalDrugPK(uploadDrugModel.getHospDrugCode(),uploadDrugModel.getHcode()));
        if(exists){
            uploadDrugModel.addError("updateFlag", "UpdateFlag 'A' (Add new one) but is already added.");
        }
    }

}
