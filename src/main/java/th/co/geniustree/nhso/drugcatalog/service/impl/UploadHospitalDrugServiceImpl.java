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
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;
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

    @Override
    public void saveUploadHospitalDrugAndRequest(UploadHospitalDrug uploadHospitalDrugs) {
        uploadHospitalDrugs = uploadHospitalDrugRepo.save(uploadHospitalDrugs);
        List<UploadHospitalDrugItem> passItems = uploadHospitalDrugs.getPassItems();
        for (UploadHospitalDrugItem uploadItem : passItems) {
            HospitalDrugPK key = new HospitalDrugPK(uploadItem.getHospDrugCode(), uploadItem.getUploadDrug().getHcode());
            HospitalDrug hospitalDrug = hospitalDrugRepo.findOne(key);
            LOG.debug("{}", ToStringBuilder.reflectionToString(hospitalDrug, ToStringStyle.MULTI_LINE_STYLE));
            if (hospitalDrug == null) {
                hospitalDrug = new HospitalDrug();
                hospitalDrug.setHcode(uploadHospitalDrugs.getHcode());
                BeanUtils.copyProperties(uploadItem, hospitalDrug);
                copyAndConvertAttribute(uploadItem, hospitalDrug);
                hospitalDrug = hospitalDrugRepo.save(hospitalDrug);
                createFirstPrice(hospitalDrug);
                createFirstEdNed(hospitalDrug);
                if (!Strings.isNullOrEmpty(uploadItem.getTmtId())) {
                    createRequestItem(uploadHospitalDrugs, uploadItem, hospitalDrug);
                }
            } else {
                if (Strings.isNullOrEmpty(hospitalDrug.getTmtId()) && !Strings.isNullOrEmpty(uploadItem.getTmtId())) {//not have tmt id before.
                    createRequestItem(uploadHospitalDrugs, uploadItem, hospitalDrug);
                }
                processUpdate(hospitalDrug, uploadItem);
            }
        }
    }

    private void createRequestItem(UploadHospitalDrug uploadHospitalDrugs, UploadHospitalDrugItem uploadItem, HospitalDrug hospitalDrug) {
        //create Request is new
        RequestItem requestItem = new RequestItem();
        requestItem.setHcode(uploadHospitalDrugs.getHcode());
        requestItem.setRequestItem(uploadItem);
        requestItem.setTargetItem(hospitalDrug);
        requestItem.setStatus(RequestItem.Status.REQUEST);
        requestItem.setRequestUser(SecurityUtil.getUserDetails().getStaffName());
        requestItem = requestItemRepo.save(requestItem);
    }

    private void processUpdate(HospitalDrug alreadyDrug, UploadHospitalDrugItem uploadItem) {
        copyAndConvertAttribute(uploadItem, alreadyDrug);
        if ("U".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            priceService.addNewPrice(alreadyDrug, new BigDecimal(uploadItem.getUnitPrice()));
            BeanUtils.copyProperties(uploadItem, alreadyDrug);
        } else if ("E".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            edNedService.addNewEdNed(alreadyDrug, uploadItem.getIsed());
            BeanUtils.copyProperties(uploadItem, alreadyDrug);
        } else if ("D".equalsIgnoreCase(uploadItem.getUpdateFlag())) {
            alreadyDrug.setDeleted(Boolean.TRUE);
            BeanUtils.copyProperties(uploadItem, alreadyDrug);
        }
    }

    private void createFirstPrice(HospitalDrug drug) {
        priceService.createFirstPrice(drug);
    }

    private void createFirstEdNed(HospitalDrug drug) {
        edNedService.createFirstEdNed(drug);
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

}
