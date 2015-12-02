/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import com.google.common.base.Strings;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalEdNed;
import th.co.geniustree.nhso.drugcatalog.model.HospitalEdNedPK;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalEdNedRepo;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EdNEdServiceImpl implements EdNEdService {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(EdNEdServiceImpl.class);
    @Autowired
    private HospitalEdNedRepo hospitalEdNedRepo;

    @Override
    public boolean isDuplicateEdNed(String hcode, String hospDrugCode, Date dateIn) {
        HospitalEdNed findOne = hospitalEdNedRepo.findOne(new HospitalEdNedPK(hcode, hospDrugCode, dateIn));
        if (findOne != null) {
            return true;
        }
        return false;
    }

    @Override
    public void addNewEdNed(HospitalDrug drug, String ised) {
        HospitalEdNed findOne = hospitalEdNedRepo.findOne(new HospitalEdNedPK(drug.getHcode(), drug.getHospDrugCode(), drug.getDateChange()));
        if (findOne == null) {
            createFirstEdNed(drug, ised);
        } else {
            findOne.setStatusEd(ised);
        }
    }

    /**
     * by pass check duplicated
     *
     * @param drug
     */
    @Override
    public void createFirstEdNed(HospitalDrug drug, String ised) {
        HospitalEdNed edNed = new HospitalEdNed();
        edNed.setHcode(drug.getHcode());
        edNed.setHospDrugCode(drug.getHospDrugCode());
        edNed.setDateIn(drug.getDateEffective());
        edNed.setStatusEd(ised);
        edNed.setCreateDate(new Date());
        drug.getEdNeds().add(hospitalEdNedRepo.save(edNed));
        drug.setIsed(ised);
    }

}
