/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;

/**
 *
 * @author moth
 */
public interface UploadHospitalDrugService {

    public static final String SPECIAL_SHAHEX_VALUE = "SPECIAL_SHAHEX_VALUE";

    public void saveUploadHospitalDrugAndRequest(UploadHospitalDrug uploadHospitalDrugs);

    public void addNewDrugByHand(String hcode, UploadHospitalDrugItem item);

    public void editDrugByHand(String hcode, UploadHospitalDrugItem item);
}
