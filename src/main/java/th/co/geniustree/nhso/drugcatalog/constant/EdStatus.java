/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.constant;

/**
 *
 * @author thanthathon.b
 */
public enum EdStatus {
    E("E"),N("N"),E_WITH_CONDITION("E*");
   
    private final String value;
    
    private EdStatus(String value) {    
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
}
