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
    private PriceService priceService;
    @Autowired
    private EdNEdService edNedService;

    @Override
    public void checkDuplicateInDatabase(HospitalDrugExcelModel uploadDrugModel) {
        if ("U".equalsIgnoreCase(uploadDrugModel.getUpdateFlag())) {
            checkDuplicateForPrice(uploadDrugModel);
        }
        checkDuplicateForEdNed(uploadDrugModel);
    }

    private void checkDuplicateForPrice(HospitalDrugExcelModel uploadDrugModel) {
        boolean priceDuplicate = priceService.isPriceDuplicate(uploadDrugModel.getHcode(), uploadDrugModel.getHospDrugCode(), DateUtils.parseDateWithOptionalTimeAndNoneLeneint(uploadDrugModel.getDateEffective()));
        if (priceDuplicate) {
            uploadDrugModel.addError("unitPrice", "UnitPrice at dateEffective is already exist.");
        }
    }

    private void checkDuplicateForEdNed(HospitalDrugExcelModel uploadDrugModel) {
        boolean duplicateEdNed = edNedService.isDuplicateEdNed(uploadDrugModel.getHcode(), uploadDrugModel.getHospDrugCode(), DateUtils.parseDateWithOptionalTimeAndNoneLeneint(uploadDrugModel.getDateChange()));
        if (duplicateEdNed) {
            uploadDrugModel.addError("ised", "ED at dateIn(dateChange) is already exist.");
        }
    }

}
