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
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.HospitalDrugService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;
import th.co.geniustree.nhso.drugcatalog.service.TmtDrugTxService;

/**
 *
 * @author moth
 */
@Service
public class HospitalDrugServiceImpl implements HospitalDrugService {

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
        HospitalDrug findOne = hospitalDrugRepo.findOne(new HospitalDrugPK(requestItem.getUploadDrugItem().getHospDrugCode(), requestItem.getUploadDrugItem().getUploadDrug().getHcode()));
        if (findOne == null) {
            return addNewHospitalDrug(requestItem);
        } else {
            return processUpdate(findOne, requestItem.getUploadDrugItem());
        }
    }

    private HospitalDrug addNewHospitalDrug(RequestItem requestItem) throws BeansException {
        HospitalDrug hospitalDrug = new HospitalDrug();
        hospitalDrug.setHcode(requestItem.getUploadDrugItem().getUploadDrug().getHcode());
        BeanUtils.copyProperties(requestItem.getUploadDrugItem(), hospitalDrug);
        copyAndConvertAttribute(requestItem.getUploadDrugItem(), hospitalDrug);
        hospitalDrug = hospitalDrugRepo.save(hospitalDrug);
        createFirstPrice(hospitalDrug);
        createFirstEdNed(hospitalDrug);
        if (!Strings.isNullOrEmpty(requestItem.getUploadDrugItem().getTmtId())) {
            tmtDrugTxService.addNewTmtDrugTx(hospitalDrug, hospitalDrug.getTmtDrug());
        }
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
        drug.setUnitPrice(new BigDecimal(uploadItem.getUnitPrice()));
        if (uploadItem.getPackPrice() != null && !uploadItem.getPackPrice().isEmpty()) {
            drug.setPackPrice(new BigDecimal(uploadItem.getPackPrice()));
        }
    }

    private HospitalDrug processUpdate(HospitalDrug alreadyDrug, UploadHospitalDrugItem uploadItem) {
        boolean isTmtIdChange = tmtIdChange(alreadyDrug.getTmtId(), uploadItem.getTmtId());
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
        return alreadyDrug;
    }

    private boolean tmtIdChange(String alreadyTmtId, String newTmtId) {
        return !Strings.nullToEmpty(alreadyTmtId).equals(newTmtId);
    }

}