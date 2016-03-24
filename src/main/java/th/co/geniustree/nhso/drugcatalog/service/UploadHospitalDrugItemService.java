/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Date;
import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;

/**
 *
 * @author thanthathon.b
 */
public interface UploadHospitalDrugItemService {

    public boolean isExistsItem(String hcode, String hospDrugCode, Date dateEffective);

    public boolean isExistsItem(String hcode, String hospDrugCode, Date dateEffective, String updateFlag);
    
    public boolean isExistsItem(String hospDrugCode, String hcode, String tmtid, String dateEffective, String updateFlag);
    
    public List<UploadHospitalDrugItem> findEditHistory(String hcode, String hospDrugCode, String tmtId, String updateFlag);

    public UploadHospitalDrugItem findLatestItemByFlag(String hcode, String hospDrugCode, String updateFlag);
    
    public boolean isHospitalDrugHasFlagAWithAccept(String hcode, String hospDrugCode);
}
