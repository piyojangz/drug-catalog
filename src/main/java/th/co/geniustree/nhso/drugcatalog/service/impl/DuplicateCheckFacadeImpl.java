/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.service.DuplicateCheckFacade;
import th.co.geniustree.nhso.drugcatalog.service.UpdateFlagControlService;

/**
 *
 * @author moth
 * @author thanthathon.b
 */
@Service
public class DuplicateCheckFacadeImpl implements DuplicateCheckFacade {

    @Autowired
    @Qualifier("UpdateFlagControlForHospitalDrugExcelModelServiceImpl")
    private UpdateFlagControlService<HospitalDrugExcelModel> updateFlagControlService;

    @Override
    public void checkDuplicateInDatabase(HospitalDrugExcelModel uploadDrugModel) {
        if(uploadDrugModel.getUpdateFlag().equalsIgnoreCase("A")){
            updateFlagControlService.validateFlagA(uploadDrugModel);
        } else if(uploadDrugModel.getUpdateFlag().equalsIgnoreCase("E") || uploadDrugModel.getUpdateFlag().equalsIgnoreCase("U")){
            updateFlagControlService.validateFlagEU(uploadDrugModel);
        } else if(uploadDrugModel.getUpdateFlag().equalsIgnoreCase("D") ){
            updateFlagControlService.validateFlagD(uploadDrugModel);
        }
    }
}
