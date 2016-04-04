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
        boolean hasHospitalDrug;
        hasHospitalDrug = uploadHospitalDrugItemService.hasHospitalDrugNeverBeenAccept(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode());
        if (!hasHospitalDrug && addError) {
            LOG.debug("ไม่สามารถเพิ่มรายการยา {} เนื่องจากมี HospDrugCode นี้อยู่ในระบบอยู่แล้ว", item.getHospDrugCode());
//            item.addError("updateFlag", "ไม่สามารถเพิ่มรายการยานี้ได้ เนื่องจากมี HospDrugCode นี้อยู่ในระบบอยู่แล้ว");
        }
        return hasHospitalDrug;
    }

    @Override
    public boolean validateFlagEU(UploadHospitalDrugItem item, boolean addError) {
        boolean hasFlagABefore;
        hasFlagABefore = uploadHospitalDrugItemService.hasHospitalDrugFlagABefore(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode());
        if (!hasFlagABefore && addError) {
            LOG.debug("ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
//            item.addError("updateFlag", "ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
        }
        boolean duplicate = uploadHospitalDrugItemService.isHospitalDrugWithTmtNotDuplicate(
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
        return hasFlagABefore && duplicate;
    }

    @Override
    public boolean validateFlagD(UploadHospitalDrugItem item, boolean addError) {
        boolean hasFlagABefore;
        hasFlagABefore = uploadHospitalDrugItemService.hasHospitalDrugFlagABefore(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode());
        if (!hasFlagABefore && addError) {
            LOG.debug("ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
//            item.addError("updateFlag", "ไม่พบรายการยาที่มี UpdateFlag A กรุณาตรวจสอบข้อมูลอีกครั้ง");
        }
        boolean flagDBeforeOrEqualA;
        flagDBeforeOrEqualA = uploadHospitalDrugItemService.isFlagDAfterFlagA(
                item.getUploadDrug().getHcode(),
                item.getHospDrugCode(),
                DateUtils.parseUSDate(Constants.TMT_DATETIME_FORMAT, item.getDateEffective()));
        if (flagDBeforeOrEqualA && addError) {
            LOG.debug("ไม่สามารถดำเนินการ Flag D ก่อนที่จะมี Flag A ได้");
//            item.addError("dateEffective", "ไม่สามารถดำเนินการ Flag D ก่อนที่จะมี Flag A ได้");
        }
        boolean duplicate = uploadHospitalDrugItemService.isHospitalDrugWithTmtNotDuplicate(
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
        return hasFlagABefore && flagDBeforeOrEqualA && duplicate;
    }

}
