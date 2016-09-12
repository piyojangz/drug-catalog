/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author thanthathon.b
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UploadHospitalDrugItemServiceImpl implements UploadHospitalDrugItemService {

    @Autowired
    private UploadHospitalDrugItemRepo repo;

    @Override
    public boolean isExistsItem(String hcode, String hospDrugCode, Date dateEffective, String updateFlag) {
        List<String> s = Arrays.asList(new String[]{"A", updateFlag});
        long count = repo.countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAndRequestAndAccept(
                hospDrugCode, hcode, dateEffective, s);
        return count > 0;
    }

    @Override
    public boolean isExistsItem(String hcode, String hospDrugCode, Date dateEffective) {
        List<String> s = Arrays.asList(new String[]{"A", "E", "U", "D"});
        long count = repo.countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAndRequestAndAccept(
                hospDrugCode, hcode, dateEffective, s);
        return count > 0;
    }

    @Override
    public List<UploadHospitalDrugItem> findEditHistory(String hcode, String hospDrugCode, String tmtId, String updateFlag) {
        return repo.findByUploadDrugHcodeAndHospDrugCodeAndTmtIdAndUpdateFlag(hcode, hospDrugCode, tmtId, updateFlag, new Sort(Sort.Direction.ASC, "id"));
    }

    @Override
    public UploadHospitalDrugItem findLatestItemByFlag(String hcode, String hospDrugCode, String updateFlag) {
        return repo.findLatestItemThatAcceptAndNotDeleteByUpdateFlag(hcode, hospDrugCode, updateFlag);
    }

    @Override
    public boolean isUnitPriceNotMoreThanDoubleLatestItem(UploadHospitalDrugItem newItem) {
        UploadHospitalDrugItem latestItem = findLatestItemByFlag(
                newItem.getUploadDrug().getHcode(),
                newItem.getHospDrugCode(),
                newItem.getUpdateFlag());

        if (latestItem == null) {
            latestItem = findLatestItemByFlag(
                    newItem.getUploadDrug().getHcode(),
                    newItem.getHospDrugCode(),
                    "A");
        }
        if (latestItem == null) {
            throw new RuntimeException("Not found uploadHospitalDrugItem");
        }
        BigDecimal oldPr = new BigDecimal(latestItem.getUnitPrice());
        BigDecimal newPr = new BigDecimal(newItem.getUnitPrice());
        return newPr.doubleValue() <= oldPr.multiply(new BigDecimal(3)).doubleValue();
    }
}
