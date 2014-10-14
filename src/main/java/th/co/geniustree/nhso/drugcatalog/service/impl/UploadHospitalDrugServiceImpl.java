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
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;
import th.co.geniustree.nhso.drugcatalog.service.TmtDrugTxService;
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
    private RequestItemRepo requestItemRepo;

    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;

    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;

    @Override
    public void saveUploadHospitalDrugAndRequest(UploadHospitalDrug uploadHospitalDrug) {
        uploadHospitalDrug = uploadHospitalDrugRepo.save(uploadHospitalDrug);
        List<UploadHospitalDrugItem> passItems = uploadHospitalDrug.getPassItems();
        for (UploadHospitalDrugItem uploadItem : passItems) {
            if (!Strings.isNullOrEmpty(uploadItem.getTmtId())) {
                createRequestItem(uploadItem);
            }
        }
    }

    private void createRequestItem(UploadHospitalDrugItem uploadItem) {
        RequestItem requestItem = new RequestItem();
        requestItem.setStatus(RequestItem.Status.REQUEST);
        requestItem.setRequestUser(SecurityUtil.getUserDetails().getPid());
        requestItem.setUploadDrugItem(uploadItem);
        requestItem = requestItemRepo.save(requestItem);
        uploadItem.setRequestItem(requestItem);
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
        HospitalDrug hospitalDrug = hospitalDrugRepo.findOne(new HospitalDrugPK(uploadItem.getHospDrugCode(), hcode));
        if (hospitalDrug == null) {
            throw new IllegalStateException("Can't edit HospitalDrug that not already exist.");
        }
        uploadItem = uploadHospitalDrugItemRepo.save(uploadItem);
        UploadHospitalDrug uploadHospitalDrug = uploadHospitalDrugRepo.findByHcodeAndShaHex(hcode, UploadHospitalDrugService.SPECIAL_SHAHEX_VALUE);
        makeSpecialUploadDrug(uploadHospitalDrug, hcode, uploadItem);
        createRequestItem(uploadItem);
    }

}
