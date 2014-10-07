/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
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
    private PriceService priceService;
    @Autowired
    private EdNEdService edNedService;
    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    @Autowired
    private TmtDrugTxService tmtDrugTxService;

    @Override
    public void saveUploadHospitalDrugAndRequest(UploadHospitalDrug uploadHospitalDrug) {
        uploadHospitalDrug = uploadHospitalDrugRepo.save(uploadHospitalDrug);
        List<UploadHospitalDrugItem> passItems = uploadHospitalDrug.getPassItems();
        for (UploadHospitalDrugItem uploadItem : passItems) {
            HospitalDrugPK key = new HospitalDrugPK(uploadItem.getHospDrugCode(), uploadItem.getUploadDrug().getHcode());
            HospitalDrug hospitalDrug = hospitalDrugRepo.findOne(key);
            LOG.debug("{}", ToStringBuilder.reflectionToString(hospitalDrug, ToStringStyle.MULTI_LINE_STYLE));
            if (hospitalDrug == null) {
                addNewHospitalDrug(uploadHospitalDrug, uploadItem);
            } else {
                processUpdate(hospitalDrug, uploadItem);
            }
        }
    }

    private void addNewHospitalDrug(UploadHospitalDrug uploadHospitalDrug, UploadHospitalDrugItem uploadItem) throws BeansException {
        HospitalDrug hospitalDrug = new HospitalDrug();
        hospitalDrug.setHcode(uploadHospitalDrug.getHcode());
        BeanUtils.copyProperties(uploadItem, hospitalDrug);
        copyAndConvertAttribute(uploadItem, hospitalDrug);
        hospitalDrug = hospitalDrugRepo.save(hospitalDrug);
        createFirstPrice(hospitalDrug);
        createFirstEdNed(hospitalDrug);
        if (!Strings.isNullOrEmpty(uploadItem.getTmtId())) {
            createRequestItem(uploadItem, hospitalDrug);
            tmtDrugTxService.addNewTmtDrugTx(hospitalDrug, hospitalDrug.getTmtDrug());
        }
    }

    private void createRequestItem(UploadHospitalDrugItem uploadItem, HospitalDrug hospitalDrug) {
        RequestItem requestItem = requestItemRepo.findOne(uploadItem.getId());
        if (requestItem == null) {
            requestItem = new RequestItem();
            requestItem.setTargetItem(hospitalDrug);
        }
        if (requestItem.getStatus() != RequestItem.Status.ACCEPT) {
            requestItem.setStatus(RequestItem.Status.REQUEST);
            requestItem.setRequestUser(SecurityUtil.getUserDetails().getPid());
        }
        requestItem.setUploadDrugItem(uploadItem);
        requestItem = requestItemRepo.save(requestItem);
        uploadItem.setRequestItem(requestItem);
    }

    private void processUpdate(HospitalDrug alreadyDrug, UploadHospitalDrugItem uploadItem) {
        boolean isTmtIdChange = tmtIdChange(alreadyDrug.getTmtId(), uploadItem.getTmtId());
        if (!Strings.isNullOrEmpty(uploadItem.getTmtId())) {
            createRequestItem(uploadItem, alreadyDrug);
        }
        if ("U".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            BigDecimal newPrice = new BigDecimal(uploadItem.getUnitPrice());
            priceService.addNewPrice(alreadyDrug, newPrice);
        } else if ("E".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            BeanUtils.copyProperties(uploadItem, alreadyDrug);
            copyAndConvertAttribute(uploadItem, alreadyDrug);
            edNedService.addNewEdNed(alreadyDrug, uploadItem.getIsed());
            if (isTmtIdChange) {
                tmtDrugTxService.addNewTmtDrugTx(alreadyDrug, alreadyDrug.getTmtDrug());
            }
        } else if ("D".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            alreadyDrug.setDeleted(Boolean.TRUE);
        }
    }

    private void createFirstPrice(HospitalDrug drug) {
        priceService.createFirstPrice(drug, drug.getUnitPrice());
    }

    private void createFirstEdNed(HospitalDrug drug) {
        edNedService.createFirstEdNed(drug, drug.getIsed());
    }

    private void copyAndConvertAttribute(UploadHospitalDrugItem uploadItem, HospitalDrug drug) {
        drug.setDateChange(uploadItem.getDateChangeDate());
        drug.setDateUpdate(uploadItem.getDateUpdateDate());
        drug.setDateEffective(uploadItem.getDateEffectiveDate());
        drug.setUnitPrice(new BigDecimal(uploadItem.getUnitPrice()));
        if (uploadItem.getPackPrice() != null && !uploadItem.getPackPrice().isEmpty()) {
            drug.setPackPrice(new BigDecimal(uploadItem.getPackPrice()));
        }
    }

    @Override
    public void addNewDrugByHand(String hcode, UploadHospitalDrugItem item) {
        item = uploadHospitalDrugItemRepo.save(item);
        UploadHospitalDrug uploadHospitalDrug = uploadHospitalDrugRepo.findByHcodeAndShaHex(hcode, UploadHospitalDrugService.SPECIAL_SHAHEX_VALUE);
        uploadHospitalDrug = makeSpecialUploadDrug(uploadHospitalDrug, hcode, item);
        addNewHospitalDrug(uploadHospitalDrug, item);
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
            uploadHospitalDrug.setPassItemCount(uploadHospitalDrug.getPassItemCount() + 1);
            uploadHospitalDrug.getPassItems().add(item);
            item.setUploadDrug(uploadHospitalDrug);
            uploadHospitalDrug = uploadHospitalDrugRepo.save(uploadHospitalDrug);
        }
        return uploadHospitalDrug;
    }

    @Override
    public void editDrugByHand(String hcode, UploadHospitalDrugItem uploadItem) {
        uploadItem = uploadHospitalDrugItemRepo.save(uploadItem);
        UploadHospitalDrug uploadHospitalDrug = uploadHospitalDrugRepo.findByHcodeAndShaHex(hcode, UploadHospitalDrugService.SPECIAL_SHAHEX_VALUE);
        makeSpecialUploadDrug(uploadHospitalDrug, hcode, uploadItem);
        HospitalDrug hospitalDrug = hospitalDrugRepo.findOne(new HospitalDrugPK(uploadItem.getHospDrugCode(), hcode));
        if (hospitalDrug == null) {
            throw new IllegalStateException("Can't edit HospitalDrug that not already exist.");
        }
        processUpdate(hospitalDrug, uploadItem);
    }

    private boolean tmtIdChange(String alreadyTmtId, String newTmtId) {
        return !Strings.nullToEmpty(alreadyTmtId).equals(newTmtId);
    }

}
