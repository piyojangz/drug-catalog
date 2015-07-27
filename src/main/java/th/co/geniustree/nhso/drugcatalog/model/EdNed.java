/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_ED_STATUS")
public class EdNed implements Serializable {

    @EmbeddedId
    private EdNedPK pk;

    @Column(name = "ED_STATUS")
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", nullable = false, insertable = false, updatable = false)
    private Drug tmtDrug;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FUND_CODE", referencedColumnName = "FUND_CODE", nullable = false, insertable = false, updatable = false)
    private Fund fund;

    @Version
    private Integer version;

    public EdNed() {
    }

    public EdNed(Drug drug, Fund fund, Date dateIn) {
        this.pk = new EdNedPK(drug, fund, dateIn);
    }

    public EdNed(String drug, String fund, Date dateIn) {
        this.pk = new EdNedPK(drug, fund, dateIn);
    }

    public EdNedPK getPk() {
        return pk;
    }

    public void setPk(EdNedPK pk) {
        this.pk = pk;
    }

    public Drug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(Drug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.pk);
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
        final EdNed other = (EdNed) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

}
