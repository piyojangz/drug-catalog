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
import th.co.geniustree.nhso.drugcatalog.Constants;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.service.UpdateFlagControlService;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author thanthathon.b
 */
@Service("UpdateFlagControlForHospitalDrugExcelModelServiceImpl")
@Transactional(propagation = Propagation.REQUIRED)
public class UpdateFlagControlForHospitalDrugExcelModelServiceImpl implements UpdateFlagControlService<HospitalDrugExcelModel> {

    @Autowired
    private UploadHospitalDrugItemService uploadHospitalDrugItemService;

    @Override
    public boolean validateFlagA(HospitalDrugExcelModel item) {
        boolean everBeenAccepted = uploadHospitalDrugItemService.hasHospitalDrugNeverAccepted(item.getHcode(), item.getHospDrugCode());
        if (!everBeenAccepted) {
            item.addError("updateFlag", "ไม่สามารถเพิ่มรายการยานี้ได้ เนื่องจากมี HospDrugCode นี้อยู่ในระบบอยู่แล้ว");
        }
        return everBeenAccepted;
    }

    @Override
    public boolean validateFlagEU(HospitalDrugExcelModel item) {
        boolean flagAHasBefore = uploadHospitalDrugItemService.hasHospitalDrugWithFlagABefore(item.getHcode(), item.getHospDrugCode());
        if (!flagAHasBefore) {
            item.addError("updateFlag", "ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
        }
        boolean duplicate = uploadHospitalDrugItemService.isHospitalDrugWithTmtDuplicated(
                item.getHcode(),
                item.getHospDrugCode(),
                item.getTmtId(),
                DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()),
                item.getUpdateFlag());
        if (duplicate) {
            item.addError("dateEffective", "พบ hospDrugCode , TMTID , dateEffective , UpdateFlag ซ้ำในฐานข้อมูล");
        }
        return flagAHasBefore && duplicate;
    }

    @Override
    public boolean validateFlagD(HospitalDrugExcelModel item) {
        boolean flagAHasBefore = uploadHospitalDrugItemService.hasHospitalDrugWithFlagABefore(item.getHcode(), item.getHospDrugCode());
        if (!flagAHasBefore) {
            item.addError("updateFlag", "ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
        }
        boolean flagDBeforeA = uploadHospitalDrugItemService.isFlagDBeforeFlagA(item.getHcode(), item.getHospDrugCode(), DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()));
        if (flagDBeforeA) {
            item.addError("dateEffective", "ไม่สามารถดำเนินการ Flag D ก่อนที่จะมี Flag A ได้");
        }
        boolean duplicate = uploadHospitalDrugItemService.isHospitalDrugWithTmtDuplicated(
                item.getHcode(),
                item.getHospDrugCode(),
                item.getTmtId(),
                DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()),
                item.getUpdateFlag());
        if (duplicate) {
            item.addError("dateEffective", "พบ hospDrugCode , TMTID , dateEffective , UpdateFlag ซ้ำในฐานข้อมูล");
        }
        return flagAHasBefore && flagDBeforeA && duplicate;
    }

}
