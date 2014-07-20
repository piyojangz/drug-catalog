/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class EdNEdServiceImpl implements EdNEdService {

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
    public void addNewEdNed(HospitalDrug alreadyDrug, String ised) {
        boolean exist = isDuplicateEdNed(alreadyDrug.getHcode(), alreadyDrug.getHospDrugCode(), alreadyDrug.getDateChange());
        if (exist) {
            throw new IllegalStateException("Ed/Ned at "+alreadyDrug.getDateChange()+" is already exist.");
        }
        //TODO MUST check range of ED/NED
        HospitalEdNed edNed = new HospitalEdNed();
        edNed.setHcode(alreadyDrug.getHcode());
        edNed.setHospDrugCode(alreadyDrug.getHospDrugCode());
        edNed.setDateIn(alreadyDrug.getDateChange());
        alreadyDrug.getEdNeds().add(hospitalEdNedRepo.save(edNed));
    }

    @Override
    public void createFirstEdNed(HospitalDrug drug) {
        HospitalEdNed edNed = new HospitalEdNed();
        edNed.setHcode(drug.getHcode());
        edNed.setHospDrugCode(drug.getHospDrugCode());
        edNed.setDateIn(drug.getDateChange());
        drug.getEdNeds().add(hospitalEdNedRepo.save(edNed));
    }

}
