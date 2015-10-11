/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import com.google.common.base.Strings;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.RequestItemService;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UploadHospitalDrugServiceImpl implements UploadHospitalDrugService {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadHospitalDrugServiceImpl.class);

    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;

    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;

    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    @Autowired
    private RequestItemService requestItemService;

    @Override
    public void saveUploadHospitalDrugAndRequest(UploadHospitalDrug uploadHospitalDrug) {
        uploadHospitalDrug = uploadHospitalDrugRepo.save(uploadHospitalDrug);
        List<UploadHospitalDrugItem> passItems = uploadHospitalDrug.getPassItems();
        for (UploadHospitalDrugItem uploadItem : passItems) {
            createRequestItem(uploadItem);
        }
    }

    private void createRequestItem(UploadHospitalDrugItem uploadItem) {
        requestItemService.generateRequest(uploadItem);
    }

    @Override
    public void addNewDrugByHand(String hcode, UploadHospitalDrugItem item) {
        item = uploadHospitalDrugItemRepo.save(item);
        UploadHospitalDrug uploadHospitalDrug = uploadHospitalDrugRepo.findByHcodeAndShaHex(hcode, UploadHospitalDrugService.SPECIAL_SHAHEX_VALUE);
        uploadHospitalDrug = makeSpecialUploadDrug(uploadHospitalDrug, hcode, item);
        createRequestItem(item);
    }

    private UploadHospitalDrug makeSpecialUploadDrug(UploadHospitalDrug uploadHospitalDrug, String hcode, UploadHospitalDrugItem item) {
        if (uploadHospitalDrug == null) {
            uploadHospitalDrug = new UploadHospitalDrug();
            uploadHospitalDrug.setHcode(hcode);
            uploadHospitalDrug.setItemCount(1);
            uploadHospitalDrug.setPassItemCount(1);
            uploadHospitalDrug.setSavedFileName("กรอกข้อมูลผ่านโปรแกรมออนไลน์");
            uploadHospitalDrug.setOriginalFilename(uploadHospitalDrug.getSavedFileName());
            uploadHospitalDrug.setShaHex(UploadHospitalDrugService.SPECIAL_SHAHEX_VALUE);
            uploadHospitalDrug.getPassItems().add(item);
            item.setUploadDrug(uploadHospitalDrug);
            uploadHospitalDrug = uploadHospitalDrugRepo.save(uploadHospitalDrug);
        } else {
            uploadHospitalDrug.setItemCount(uploadHospitalDrug.getPassItems().size() + 1);
            uploadHospitalDrug.setPassItemCount(uploadHospitalDrug.getPassItems().size() + 1);
            uploadHospitalDrug.getPassItems().add(item);
            item.setUploadDrug(uploadHospitalDrug);
            uploadHospitalDrug = uploadHospitalDrugRepo.save(uploadHospitalDrug);
        }
        return uploadHospitalDrug;
    }

    @Override
    public void editDrugByHand(String hcode, UploadHospitalDrugItem uploadItem) {
        boolean exist = hospitalDrugRepo.exists(new HospitalDrugPK(uploadItem.getHospDrugCode(), hcode,uploadItem.getTmtId()));
        if (!exist) {
            throw new IllegalStateException("Can't edit HospitalDrug that not already exist.");
        }
        uploadItem = uploadHospitalDrugItemRepo.save(uploadItem);
        UploadHospitalDrug uploadHospitalDrug = uploadHospitalDrugRepo.findByHcodeAndShaHex(hcode, UploadHospitalDrugService.SPECIAL_SHAHEX_VALUE);
        makeSpecialUploadDrug(uploadHospitalDrug, hcode, uploadItem);
        createRequestItem(uploadItem);
    }

}
