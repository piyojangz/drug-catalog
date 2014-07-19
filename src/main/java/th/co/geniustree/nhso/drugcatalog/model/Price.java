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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_PRICE")
@IdClass(PricePK.class)
public class Price implements Serializable {

    @Id
    private String hcode;

    @Id
    private String hospDrugCode;

    @Id
    @Temporal(TemporalType.DATE)
    private Date dateEffectInclusive;

    @Temporal(TemporalType.DATE)
    private Date dateEndInclusive;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date createDate;

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

    public Date getDateEndInclusive() {
        return dateEndInclusive;
    }

    public void setDateEndInclusive(Date dateEndInclusive) {
        this.dateEndInclusive = dateEndInclusive;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.hcode);
        hash = 11 * hash + Objects.hashCode(this.hospDrugCode);
        hash = 11 * hash + Objects.hashCode(this.dateEffectInclusive);
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
        final Price other = (Price) obj;
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        if (!Objects.equals(this.dateEffectInclusive, other.dateEffectInclusive)) {
            return false;
        }
        return true;
    }

}
