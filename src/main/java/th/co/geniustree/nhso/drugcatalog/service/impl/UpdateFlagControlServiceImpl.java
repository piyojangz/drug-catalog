/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.Constants;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.service.UpdateFlagControlService;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author thanthathon.b
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UpdateFlagControlServiceImpl implements UpdateFlagControlService<UploadHospitalDrugItem> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateFlagControlServiceImpl.class);

    @Autowired
    private UploadHospitalDrugItemService uploadHospitalDrugItemService;

    @Override
    public boolean validateFlagA(UploadHospitalDrugItem item, boolean addError) {
        boolean everBeenAccepted = uploadHospitalDrugItemService.hasHospitalDrugNeverAccepted(item.getUploadDrug().getHcode(), item.getHospDrugCode());
        if (everBeenAccepted && addError) {
            LOG.debug("ไม่สามารถเพิ่มรายการยา {} เนื่องจากมี HospDrugCode นี้อยู่ในระบบอยู่แล้ว", item.getHospDrugCode());
//            item.addError("updateFlag", "ไม่สามารถเพิ่มรายการยานี้ได้ เนื่องจากมี HospDrugCode นี้อยู่ในระบบอยู่แล้ว");
        }
        return everBeenAccepted;
    }

    @Override
    public boolean validateFlagEU(UploadHospitalDrugItem item, boolean addError) {
        boolean flagAHasBefore = uploadHospitalDrugItemService.hasHospitalDrugWithFlagABefore(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode());
        if (!flagAHasBefore && addError) {
            LOG.debug("ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
//            item.addError("updateFlag", "ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
        }
        boolean duplicate = uploadHospitalDrugItemService.isHospitalDrugWithTmtDuplicated(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode(),
                item.getTmtId(),
                DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()),
                item.getUpdateFlag());
        if (duplicate && addError) {
            LOG.debug("พบข้อมูล {} , {} , {} , {} ซ้ำในฐานข้อมูล",
                    item.getHospDrugCode(),
                    item.getTmtId(),
                    DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()),
                    item.getUpdateFlag());
//            item.addError("dateEffective", "พบ hospDrugCode , TMTID , dateEffective , UpdateFlag ซ้ำในฐานข้อมูล");
        }
        return flagAHasBefore && duplicate;
    }

    @Override
    public boolean validateFlagD(UploadHospitalDrugItem item, boolean addError) {
        boolean flagAHasBefore = uploadHospitalDrugItemService.hasHospitalDrugWithFlagABefore(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode());
        if (!flagAHasBefore && addError) {
            LOG.debug("ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
//            item.addError("updateFlag", "ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
        }
        boolean flagDBeforeA = uploadHospitalDrugItemService.isFlagDBeforeFlagA(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode(),
                DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()));
        if (flagDBeforeA && addError) {
            LOG.debug("ไม่สามารถดำเนินการ Flag D ก่อนที่จะมี Flag A ได้");
//            item.addError("dateEffective", "ไม่สามารถดำเนินการ Flag D ก่อนที่จะมี Flag A ได้");
        }
        boolean duplicate = uploadHospitalDrugItemService.isHospitalDrugWithTmtDuplicated(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode(),
                item.getTmtId(),
                DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()),
                item.getUpdateFlag());
        if (duplicate && addError) {
            LOG.debug("พบข้อมูล {} , {} , {} , {} ซ้ำในฐานข้อมูล",
                    item.getHospDrugCode(),
                    item.getTmtId(),
                    DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()),
                    item.getUpdateFlag());
//            item.addError("dateEffective", "พบ hospDrugCode , TMTID , dateEffective , UpdateFlag ซ้ำในฐานข้อมูล");
        }
        return flagAHasBefore && flagDBeforeA && duplicate;
    }

}
