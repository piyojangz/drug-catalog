/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.Date;

/**
 *
 * @author Thanthathon
 */
public interface EclaimDAO {

    public HospitalDrugWithTMT loadDrugInfo(String hcode, String hospDrugCode, String tmtid, Date dateEffective);
    
}
