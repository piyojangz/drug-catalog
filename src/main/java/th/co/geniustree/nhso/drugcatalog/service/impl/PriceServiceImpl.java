/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalPrice;
import th.co.geniustree.nhso.drugcatalog.model.HospitalPricePK;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalPriceRepo;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;

/**
 *
 * @author moth
 */
@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    private HospitalPriceRepo priceRepo;

    @Override
    public boolean isPriceDuplicate(String hcode, String hospDrugCode, Date dateEffective) {
        HospitalPrice findOne = priceRepo.findOne(new HospitalPricePK(hcode, hospDrugCode, dateEffective));
        return findOne != null;
    }

    /**
     * like createFirstPrice but check duplicated against database.
     *
     * @param hospitalDrug
     * @param unitprice
     */
    @Override
    public void addNewPrice(HospitalDrug hospitalDrug, BigDecimal unitprice) {
        HospitalPrice findOne = priceRepo.findOne(new HospitalPricePK(hospitalDrug.getHcode(), hospitalDrug.getHospDrugCode(), hospitalDrug.getDateEffective()));
        if (findOne == null) {
            createFirstPrice(hospitalDrug);
        } else {
            findOne.setPrice(unitprice);
        }

    }

    /**
     * Bypass check duplicate
     *
     * @param hospitalDrug
     */
    @Override
    public void createFirstPrice(HospitalDrug hospitalDrug) {
        HospitalPrice price = new HospitalPrice();
        price.setHcode(hospitalDrug.getHcode());
        price.setHospDrugCode(hospitalDrug.getHospDrugCode());
        price.setDateEffectInclusive(hospitalDrug.getDateEffective());
        price.setPrice(hospitalDrug.getUnitPrice());
        price.setCreateDate(new Date());
        hospitalDrug.getPrices().add(priceRepo.save(price));
    }

}
