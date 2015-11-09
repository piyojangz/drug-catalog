/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_HOSPPRICE",indexes = {
    @Index(name = "TMT_HOSPPRICE_DATE_F",columnList = "DATE_EFFECTIVE")
})
@IdClass(HospitalPricePK.class)
public class HospitalPrice implements Serializable {

    @Id
    @Column(name = "HCODE", nullable = false, length = 5)
    private String hcode;

    @Id
    @Column(name = "HOSPDRUGCODE", nullable = false, length = 30)
    private String hospDrugCode;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_EFFECTIVE", nullable = false)
    private Date dateEffectInclusive;
    
    @Id
    @Column(name = "TMTID" , nullable = false , length = 6)
    private String tmtId;

    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public Date getDateEffectInclusive() {
        return dateEffectInclusive;
    }

    public void setDateEffectInclusive(Date dateEffectInclusive) {
        this.dateEffectInclusive = dateEffectInclusive;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.hcode);
        hash = 53 * hash + Objects.hashCode(this.hospDrugCode);
        hash = 53 * hash + Objects.hashCode(this.dateEffectInclusive);
        hash = 53 * hash + Objects.hashCode(this.tmtId);
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
        final HospitalPrice other = (HospitalPrice) obj;
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        if (!Objects.equals(this.dateEffectInclusive, other.dateEffectInclusive)) {
            return false;
        }
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }


}
