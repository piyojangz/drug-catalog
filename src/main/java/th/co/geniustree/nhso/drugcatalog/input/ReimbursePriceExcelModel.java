/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
public class ReimbursePriceExcelModel implements Serializable{
    
    
    @XlsColumn(columnNames = "TMTID")
    private String tmtid;
    
    @XlsColumn(columnNames = "EFFECTIVE_DATE")
    private String effectiveDate;
    
    @XlsColumn(columnNames = "PRICE")
    private String price;

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
}
