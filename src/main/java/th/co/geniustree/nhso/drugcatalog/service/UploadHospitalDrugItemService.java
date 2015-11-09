/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

/**
 *
 * @author thanthathon.b
 */
public interface UploadHospitalDrugItemService {
    
    public boolean isChangeTmt(String hcode, String hospDrugCode,String tmtid);

    public long countTotalHospitalDrug(String hcode, String hospDrugCode, String tmtid);
    
}
