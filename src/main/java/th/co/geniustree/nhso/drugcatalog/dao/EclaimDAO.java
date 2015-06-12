/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.dao;

import java.util.Date;
import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugWithTMT;

/**
 *
 * @author Thanthathon
 */
public interface EclaimDAO {

    public List<HospitalDrugWithTMT> loadDrugInfo(String hcode, String hospDrugCode, String tmtid, Date dateEffective);
    
}
