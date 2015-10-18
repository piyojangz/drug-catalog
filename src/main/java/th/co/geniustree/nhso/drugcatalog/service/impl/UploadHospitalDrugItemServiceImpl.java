/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author thanthathon.b
 */
public class UploadHospitalDrugItemServiceImpl implements UploadHospitalDrugItemService{

    @Autowired
    private UploadHospitalDrugItemRepo repo;
    
    @Override
    public long countTotalHospitalDrugWithFlag(String hcode, String hospDrugCode, String tmtid, String updateFlag) {
        return repo.countByHospitalDrugWithFlag(hcode, hospDrugCode, tmtid, updateFlag);
    }
    
}
