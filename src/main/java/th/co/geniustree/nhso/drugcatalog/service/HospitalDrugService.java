/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;

/**
 *
 * @author moth
 */
public interface HospitalDrugService {

    public HospitalDrug addOrUpdateHospitalDrug(RequestItem requestItem);
    
    public HospitalDrug findById(String hcode , String hospDrugCode);
}
