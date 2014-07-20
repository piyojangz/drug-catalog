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
import th.co.geniustree.nhso.drugcatalog.model.Price;
import th.co.geniustree.nhso.drugcatalog.model.PricePK;
import th.co.geniustree.nhso.drugcatalog.repo.PriceRepo;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;

/**
 *
 * @author moth
 */
@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceRepo priceRepo;

    @Override
    public boolean isPriceDuplicate(String hcode, String hospDrugCode, Date dateEffective) {
        Price findOne = priceRepo.findOne(new PricePK(hcode, hospDrugCode, dateEffective));
        return findOne != null;
    }

    @Override
    public void addNewPrice(HospitalDrug hospitalDrug, BigDecimal unitprice) {
        boolean exist = isPriceDuplicate(hospitalDrug.getHcode(), hospitalDrug.getHospDrugCode(), hospitalDrug.getDateEffective());
        if (exist) {
            throw new IllegalStateException("Price at " + hospitalDrug.getDateEffective() + " is already exist.");
        }
        Price price = new Price();
        price.setHcode(hospitalDrug.getHcode());
        price.setHospDrugCode(hospitalDrug.getHospDrugCode());
        price.setDateEffectInclusive(hospitalDrug.getDateEffective());
        price.setPrice(unitprice);
        hospitalDrug.getPrices().add(priceRepo.save(price));
    }

    @Override
    public void createFirstPrice(HospitalDrug hospitalDrug) {
        Price price = new Price();
        price.setHcode(hospitalDrug.getHcode());
        price.setHospDrugCode(hospitalDrug.getHospDrugCode());
        price.setDateEffectInclusive(hospitalDrug.getDateEffective());
        price.setPrice(hospitalDrug.getUnitPrice());
        hospitalDrug.getPrices().add(priceRepo.save(price));
    }

}
