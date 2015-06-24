/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.dao;

import java.util.Date;
import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType;
//import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType;

/**
 *
 * @author Thanthathon
 */
public interface EclaimDAO {

    public HospitalDrugType loadDrugInfo(String hcode, String hospDrugCode, String tmtid, Date dateEffective);
    
    public List<String> getDrugGroupsFrom(HospitalDrugType drug);
    
}
