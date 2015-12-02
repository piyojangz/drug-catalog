/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.math.BigDecimal;
import java.util.Date;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;

/**
 *
 * @author moth
 */
public interface PriceService {

    public boolean isPriceDuplicate(String hcode, String hospDrugCode, Date dateEffective);

    public void addNewPrice(HospitalDrug hospitalDrug, BigDecimal price);

    public void createFirstPrice(HospitalDrug hospitalDrug, BigDecimal unitprice);
}
