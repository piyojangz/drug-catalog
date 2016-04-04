/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

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
    public boolean isExistsItem(String hcode, String hospDrugCode, String tmtid, String dateEffective, String updateFlag) {
        long count = repo.countByHospDrugCodeAndUploadDrugHcodeAndTMTIDAndDateEffectiveAndRequestAndAccept(
                hospDrugCode,
                hcode,
                tmtid,
                dateEffective,
                updateFlag);
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
    public boolean isHospitalDrugHasFlagAWithAccept(String hcode, String hospDrugCode) {
        long count = repo.countByHospitalDrugThatFlagAAndAccept(
                hospDrugCode,
                hcode);
        return count > 0;
    }

    @Override
    public boolean hasHospitalDrugNeverBeenAccept(String hcode, String hospDrugCode) {
        long count = repo.countByHcodeAndHospDrugCodeThatNotDeleteAndNotReject(hcode, hospDrugCode);
        return count == 0;
    }

    @Override
    public boolean hasHospitalDrugFlagABefore(String hcode, String hospDrugCode) {
        long count = repo.countByHospitalDrugThatFlagAAndAccept(hospDrugCode, hcode);
        return count == 1;
    }

    @Override
    public boolean isHospitalDrugWithTmtNotDuplicate(String hcode, String hospDrugCode, String tmtid, Date dateEffective, String updateFlag) {
        long count = repo.countByHospDrugCodeAndUploadDrugHcodeAndTMTIDAndDateEffectiveAndRequestAndAccept(hospDrugCode, hcode, tmtid, dateEffective, updateFlag);
        return count == 0;
    }

    @Override
    public boolean isFlagDAfterFlagA(String hcode, String hospDrugCode, Date dateEffective) {
        long count = repo.countByHospitalDrugThatDateEffectiveBeforeFlagA(hcode, hospDrugCode, dateEffective);
        return count == 0;
    }

}
