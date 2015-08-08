/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Thanthathon
 */
@Embeddable
public class EdNedPK implements Serializable {

    @Column(name = "TMTID")
    private String tmtId;

    @Column(name = "FUND_CODE")
    private String fundCode;

    @Column(name = "DATE_IN")
    @Temporal(TemporalType.DATE)
    private Date dateIn;

    public EdNedPK() {
    }

    public EdNedPK(String tmtDrug, String fund, Date dateIn) {
        this.tmtId = tmtDrug;
        this.fundCode = fund;
        this.dateIn = dateIn;
    }
    
    public EdNedPK(Drug drug, Fund fund, Date dateIn) {
        this.tmtId = drug.getTmtId();
        this.fundCode = fund.getFundCode();
        this.dateIn = dateIn;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.tmtId);
        hash = 71 * hash + Objects.hashCode(this.fundCode);
        hash = 71 * hash + Objects.hashCode(this.dateIn);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EdNedPK other = (EdNedPK) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        if (!Objects.equals(this.fundCode, other.fundCode)) {
            return false;
        }
        if (!Objects.equals(this.dateIn, other.dateIn)) {
            return false;
        }
        return true;
    }

}
