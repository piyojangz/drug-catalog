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
    private Date effectiveDate;
    
    @XlsColumn(columnNames = "PRICE")
    private BigDecimal price;

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
