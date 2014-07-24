/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.DuplicateCheckFacade;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;

/**
 *
 * @author moth
 */
@Service
public class DuplicateCheckFacadeImpl implements DuplicateCheckFacade {

    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;

    @Override
    public void checkDuplicateInDatabase(HospitalDrugExcelModel uploadDrugModel) {
        if ("U".equalsIgnoreCase(uploadDrugModel.getUpdateFlag())) {
            checkDuplicateForUpdateFlageU(uploadDrugModel);
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
            uploadDrugModel.addError("dateChange", "Flag A,E,D at dateChange is already exist.");
        }
    }

}
