/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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
        boolean exist = isDuplicateEdNed(drug.getHcode(), drug.getHospDrugCode(), drug.getDateChange());
        if (exist) {
            throw new IllegalStateException("Ed/Ned at " + drug.getDateChange() + " is already exist.");
        }
        //TODO MUST check range of ED/NED
        createFirstEdNed(drug);
    }

    /**
     * by pass check duplicated
     *
     * @param drug
     */
    @Override
    public void createFirstEdNed(HospitalDrug drug) {
        HospitalEdNed edNed = new HospitalEdNed();
        edNed.setHcode(drug.getHcode());
        edNed.setHospDrugCode(drug.getHospDrugCode());
        edNed.setDateIn(drug.getDateChange());
        drug.getEdNeds().add(hospitalEdNedRepo.save(edNed));
    }

}
