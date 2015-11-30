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
        long count = repo.countByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAndRequestAndAccept(hospDrugCode, hcode, dateEffective, updateFlag);
        return count > 0;
    }

    @Override
    public List<UploadHospitalDrugItem> findEditHistory(String hcode, String hospDrugCode, String updateFlag) {
        List<UploadHospitalDrugItem> items = repo.findByUploadDrugHcodeAndHospDrugCodeAndUpdateFlag(hcode, hospDrugCode, updateFlag, new Sort(Sort.Direction.ASC, "id"));
        items.addAll(repo.findByUploadDrugHcodeAndHospDrugCodeAndUpdateFlag(hcode, hospDrugCode, "A", new Sort(Sort.Direction.ASC, "id")));
        return items;
    }

    @Override
    public UploadHospitalDrugItem findLatestItemByFlag(String hcode, String hospDrugCode, String updateFlag) {
        return repo.findLatestItemThatAcceptAndNotDeleteByUpdateFlag(hcode, hospDrugCode, updateFlag);
    }

    
}
