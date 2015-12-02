/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Date;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;

/**
 *
 * @author moth
 */
public interface EdNEdService {

    public boolean isDuplicateEdNed(String hcode, String hospDrugCode, Date dateIn);

    public void addNewEdNed(HospitalDrug alreadyDrug, String ised);

    public void createFirstEdNed(HospitalDrug drug, String ised);
}
