/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalEdNedPK;
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugService;

/**
 *
 * @author moth
 */
@Service
public class UploadHospitalDrugServiceImpl implements UploadHospitalDrugService {

    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    @Autowired
    private PriceService priceService;
    @Autowired
    private EdNEdService edNedService;

    @Override
    public void saveUploadHospitalDrugAndRequest(UploadHospitalDrug uploadHospitalDrugs) {
        uploadHospitalDrugs = uploadHospitalDrugRepo.save(uploadHospitalDrugs);
        List<UploadHospitalDrugItem> passItems = uploadHospitalDrugs.getPassItems();
        for (UploadHospitalDrugItem uploadItem : passItems) {
            if (!alreadyExistAndNotYetApprove(uploadItem) && !Strings.isNullOrEmpty(uploadItem.getTmtId())) {
                //create HospitalDrug
                HospitalDrug drug = new HospitalDrug();
                BeanUtils.copyProperties(uploadItem, drug);
                copyAndConvertAttribute(uploadItem, drug);
                drug.setHcode(uploadHospitalDrugs.getHcode());
                drug = hospitalDrugRepo.save(drug);
                createPrice(drug);
                createEdNed(drug);
                //create Request is new
                RequestItem requestItem = new RequestItem();
                requestItem.setHcode(uploadHospitalDrugs.getHcode());
                requestItem.setRequestItem(uploadItem);
                requestItem.setTargetItem(drug);
                requestItem.setStatus(RequestItem.Status.REQUEST);
                requestItem.setRequestUser(SecurityUtil.getUserDetails().getStaffName());
                requestItem = requestItemRepo.save(requestItem);
            } else {
                processUpdate(uploadItem);
            }
        }
    }

    private boolean alreadyExistAndNotYetApprove(UploadHospitalDrugItem uploadItem) {
        Integer countByHospDrugCodeAndHcodeAndApproved = hospitalDrugRepo.countByHospDrugCodeAndHcodeAndApproved(uploadItem.getHospDrugCode(), uploadItem.getUploadDrug().getHcode(), false);
        return countByHospDrugCodeAndHcodeAndApproved > 0;
    }

    private void processUpdate(UploadHospitalDrugItem uploadItem) {
        HospitalDrugPK key = new HospitalDrugPK(uploadItem.getHospDrugCode(), uploadItem.getUploadDrug().getHcode());
        HospitalDrug alreadyDrug = hospitalDrugRepo.findOne(key);
        copyAndConvertAttribute(uploadItem, alreadyDrug);
        if ("U".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            addNewPrice(alreadyDrug, uploadItem);
        } else if ("E".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            updateAttribute(alreadyDrug, uploadItem);
        } else if ("D".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            alreadyDrug.setDeleted(Boolean.TRUE);
        }
    }

    private void addNewPrice(HospitalDrug alreadyDrug, UploadHospitalDrugItem uploadItem) {
        priceService.addNewPrice(alreadyDrug, new BigDecimal(uploadItem.getUnitPrice()));
    }

    private void updateAttribute(HospitalDrug alreadyDrug, UploadHospitalDrugItem uploadItem) {
        if (!alreadyDrug.getIsed().equals(uploadItem.getIsed())) {
            edNedService.addNewEdNed(alreadyDrug, uploadItem.getIsed());
        }
        BeanUtils.copyProperties(uploadItem, alreadyDrug);
    }

    private void createPrice(HospitalDrug drug) {
        priceService.createFirstPrice(drug);
    }

    private void createEdNed(HospitalDrug drug) {
        edNedService.createFirstEdNed(drug);
    }

    private void copyAndConvertAttribute(UploadHospitalDrugItem uploadItem, HospitalDrug drug) {
        drug.setDateChange(DateUtils.parseDateWithOptionalTimeAndNoneLeneint(uploadItem.getDateChange()));
        drug.setDateUpdate(DateUtils.parseDateWithOptionalTimeAndNoneLeneint(uploadItem.getDateUpdate()));
        drug.setDateEffective(DateUtils.parseDateWithOptionalTimeAndNoneLeneint(uploadItem.getDateEffective()));
        drug.setUnitPrice(new BigDecimal(uploadItem.getUnitPrice()));
        if (uploadItem.getPackPrice() != null && !uploadItem.getPackPrice().isEmpty()) {
            drug.setPackPrice(new BigDecimal(uploadItem.getPackPrice()));
        }
    }

}
