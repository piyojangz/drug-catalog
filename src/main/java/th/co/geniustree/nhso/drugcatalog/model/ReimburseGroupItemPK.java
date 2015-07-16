/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 *
 * @author Thanthathon
 */
@Embeddable
public class ReimburseGroupItemPK implements Serializable {

    private String tmtId;
    private String fundId;
    private String icd10;
    
    @Embedded
    private EdNedPK edNed;

    public ReimburseGroupItemPK() {
    }

    public ReimburseGroupItemPK(String drug, String fund, String icd10, String edStatus) {
        this.tmtId = drug;
        this.fundId = fund;
        this.icd10 = icd10;
//        this.edStatus = edStatus;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getIcd10() {
        return icd10;
    }

    public void setIcd10(String icd10) {
        this.icd10 = icd10;
    }


}
