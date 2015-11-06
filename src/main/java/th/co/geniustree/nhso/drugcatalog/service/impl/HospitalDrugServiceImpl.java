/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import com.google.common.base.Strings;
import java.math.BigDecimal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.HospitalDrugService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;
import th.co.geniustree.nhso.drugcatalog.service.TmtDrugTxService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class HospitalDrugServiceImpl implements HospitalDrugService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HospitalDrugServiceImpl.class);
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    @Autowired
    private PriceService priceService;
    @Autowired
    private EdNEdService edNedService;

    @Autowired
    private TmtDrugTxService tmtDrugTxService;

    @Override
    public HospitalDrug addOrUpdateHospitalDrug(RequestItem requestItem) {
        if(Strings.isNullOrEmpty(requestItem.getUploadDrugItem().getTmtId())){
            requestItem.getUploadDrugItem().setTmtId("NULLID");
        }
        HospitalDrug findOne = hospitalDrugRepo.findOne(new HospitalDrugPK(
                requestItem.getUploadDrugItem().getHospDrugCode(),
                requestItem.getUploadDrugItem().getUploadDrug().getHcode(),
                requestItem.getUploadDrugItem().getTmtId()));
        if (findOne == null) {
            return addNewHospitalDrug(requestItem);
        } else {
            findOne.setApproved(Boolean.TRUE);
            return processUpdate(findOne, requestItem.getUploadDrugItem());
        }
    }

    private HospitalDrug addNewHospitalDrug(RequestItem requestItem) throws BeansException {
        HospitalDrug hospitalDrug = new HospitalDrug();
        hospitalDrug.setHcode(requestItem.getUploadDrugItem().getUploadDrug().getHcode());
        BeanUtils.copyProperties(requestItem.getUploadDrugItem(), hospitalDrug);
        copyAndConvertAttribute(requestItem.getUploadDrugItem(), hospitalDrug);
        hospitalDrug.setApproved(Boolean.TRUE);
        hospitalDrug = hospitalDrugRepo.save(hospitalDrug);
        createFirstPrice(hospitalDrug);
        createFirstEdNed(hospitalDrug);
        if (!hospitalDrug.getTmtDrug().getTmtId().equals("NULLID")) {
            tmtDrugTxService.addNewTmtDrugTx(hospitalDrug, hospitalDrug.getTmtDrug());
        }
        log.info("insertComplete");
        return hospitalDrug;
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
        drug.setUnitPrice(new BigDecimal(uploadItem.getUnitPrice().replaceAll(",", "")));
        if (uploadItem.getPackPrice() != null && !uploadItem.getPackPrice().isEmpty()) {
            drug.setPackPrice(new BigDecimal(uploadItem.getPackPrice().replaceAll(",", "")));
        }
    }

    private HospitalDrug processUpdate(HospitalDrug alreadyDrug, UploadHospitalDrugItem uploadItem) {
        if ("U".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            BigDecimal newPrice = new BigDecimal(uploadItem.getUnitPrice().replaceAll(",", ""));
            priceService.addNewPrice(alreadyDrug, newPrice);
        } else if ("E".equalsIgnoreCase(uploadItem.getUpdateFlag()) || "A".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            BeanUtils.copyProperties(uploadItem, alreadyDrug);
            copyAndConvertAttribute(uploadItem, alreadyDrug);
            edNedService.addNewEdNed(alreadyDrug, uploadItem.getIsed());
            if (!uploadItem.getTmtDrug().getTmtId().equals("NULLID")) {
                tmtDrugTxService.addNewTmtDrugTx(alreadyDrug, uploadItem.getTmtDrug());
            }
        } else if ("D".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            alreadyDrug.setDeleted(Boolean.TRUE);
        }
        return alreadyDrug;
    }

}
