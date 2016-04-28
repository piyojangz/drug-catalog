/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model.log;

import java.io.Serializable;
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
 * @author thanthathon.b
 */
@Entity
@Table(name = "TMT_DRUGGROUPITEM_DELETED")
public class TMTDrugGroupItemDeleted implements Serializable {

    @Id
    @TableGenerator(name = "TMT_DRUGGROUPITEM_DELETED_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_DRUGGROUPITEM_DELETED")
    @GeneratedValue(generator = "TMT_DRUGGROUPITEM_DELETED_GEN", strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "TMTID", nullable = false)
    private String tmtId;

    @Column(name = "DRUGGROUP_NAME", nullable = false)
    private String drugGroup;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATEIN", nullable = false)
    private Date dateIn;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATEOUT")
    private Date dateOut;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDATE")
    private Date createDate;
    
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

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getDrugGroup() {
        return drugGroup;
    }

    public void setDrugGroup(String drugGroup) {
        this.drugGroup = drugGroup;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final TMTDrugGroupItemDeleted other = (TMTDrugGroupItemDeleted) obj;
        return Objects.equals(this.id, other.id);
    }

}
