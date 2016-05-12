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
import th.co.geniustree.nhso.drugcatalog.service.DuplicateCheckFacade;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author moth
 * @author thanthathon.b
 */
@Service
public class DuplicateCheckFacadeImpl implements DuplicateCheckFacade {

    @Autowired
    private UploadHospitalDrugItemService uploadDrugItemService;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;

    @Override
    public void checkDuplicateInDatabase(HospitalDrugExcelModel uploadDrugModel) {
        checkDuplicateForUpdateFlageUED(uploadDrugModel);
        if ("A".equalsIgnoreCase(uploadDrugModel.getUpdateFlag())) {
            checkDuplicateForUpdateFlagA(uploadDrugModel);
        } else {
            isHospitalDrugHasFlagABefore(uploadDrugModel);
        }
    }

    private void checkDuplicateForUpdateFlageUED(HospitalDrugExcelModel uploadDrugModel) {
        boolean exist = uploadDrugItemService.isExistsItem(
                uploadDrugModel.getHcode(),
                uploadDrugModel.getHospDrugCode(),
                uploadDrugModel.getTmtId(),
                uploadDrugModel.getDateEffective(),
                uploadDrugModel.getUpdateFlag());
        if (exist) {
            uploadDrugModel.addError("dateEffective", "พบ hospDrugCode , TMTID , dateEffective , UpdateFlag ซ้ำในฐานข้อมูล");
        }
    }
    
    private void isHospitalDrugHasFlagABefore(HospitalDrugExcelModel uploadDrugModel){
        boolean exists = uploadDrugItemService.isHospitalDrugHasFlagAWithAccept(uploadDrugModel.getHcode(), uploadDrugModel.getHospDrugCode());
        if(!exists){
            uploadDrugModel.addError("rowNum", "รายการยานี้ไม่มีรายการยาที่เป็น Flag A มาก่อน หรือส่งมาแล้วแต่ยังไม่ได้รับการอนุมัติ หรืออนุมัติแล้วไม่ผ่านการตรวจสอบ");
        }
    }

    private void checkDuplicateForUpdateFlagA(HospitalDrugExcelModel uploadDrugModel) {
        boolean exists = hospitalDrugRepo.exists(new HospitalDrugPK(uploadDrugModel.getHospDrugCode(), uploadDrugModel.getHcode()));
        if (exists) {
            uploadDrugModel.addError("updateFlag", "UpdateFlag \"A\" ใช้เฉพาะเพิ่มรายการยาใหม่ เท่านั้น หากต้องการปรับปรุง/แก้ไขรายการยาเดิม ให้ระบุ UpdateFlag ให้ถูกต้อง");
        }
    }

}
