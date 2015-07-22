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

    @Column(name = "FUND_ID")
    private String fundId;

    @Column(name = "DATE_IN")
    @Temporal(TemporalType.DATE)
    private Date dateIn;

    public EdNedPK() {
    }

    public EdNedPK(String tmtDrug, String fund, Date dateIn) {
        this.tmtId = tmtDrug;
        this.fundId = fund;
        this.dateIn = dateIn;
    }
    
    public EdNedPK(Drug drug, Fund fund, Date dateIn) {
        this.tmtId = drug.getTmtId();
        this.fundId = fund.getFundCode();
        this.dateIn = dateIn;
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
        hash = 71 * hash + Objects.hashCode(this.fundId);
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
        if (!Objects.equals(this.fundId, other.fundId)) {
            return false;
        }
        if (!Objects.equals(this.dateIn, other.dateIn)) {
            return false;
        }
        return true;
    }

}
