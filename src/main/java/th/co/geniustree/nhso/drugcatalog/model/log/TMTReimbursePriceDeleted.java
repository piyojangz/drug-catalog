/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model.log;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author moth
 */
@Table(name = "TMT_REIMBURSE_PRICE_DELETED")
@Entity
public class TMTReimbursePriceDeleted implements Serializable {

    @Id
    @TableGenerator(name = "TMT_REIMBURSE_PRICE_DELETED_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_REIMBURSE_PRICE_DELETED")
    @GeneratedValue(generator = "TMT_REIMBURSE_PRICE_DELETED_GEN", strategy = GenerationType.TABLE)
    private Integer id;
    
    @Column(name = "PRICE", precision = 10, scale = 2)
    private BigDecimal price;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;
    
    @Column(name = "TMTID")
    private String tmtId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETED_DATE", nullable = false)
    private Date deletedDate;
    
    @PrePersist
    public void prePersist(){
        this.deletedDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final TMTReimbursePriceDeleted other = (TMTReimbursePriceDeleted) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
