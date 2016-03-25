/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.math.BigDecimal;

/**
 *
 * @author thanthathon.b
 */
public class BigDecimalUtils {
    
    public static boolean checkPrice(String oldPrice, String newPrice) {
        BigDecimal oldPr = new BigDecimal(oldPrice);
        BigDecimal newPr = new BigDecimal(newPrice);
        return newPr.doubleValue() <= oldPr.multiply(new BigDecimal(2)).doubleValue();
    }
    
}
