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
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_DRUG")
//TODO Duplicat table maybe cause probremm on create schema
public class TradeDrug implements Serializable, Typeable {

    @Id
    @XlsColumn(columnNames = {"TMTID(TPU)", "TMTID(TP)"})
    @Column(name = "TMTID", length = 6, nullable = false)
    private String tmtId;
    @Transient
    @XlsColumn(columnNames = "changeDate")
    private String changeDateString;

    @Column(name = "CHANGEDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date changeDate;

    @XlsColumn
    @Column(name = "FSN", length = 1000)
    private String fsn;
    @XlsColumn
    @Column(name = "MANUFACTURER", length = 255)
    private String manufacturer;
    @Column(name = "TYPE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private TMTDrug.Type type;

    @Column(name = "CREATEDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "LASTMODIFIEDDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @PrePersist
    public void prepersist() {
        createDate = new Date();
        lastModifiedDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        lastModifiedDate = new Date();
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getChangeDateString() {
        return changeDateString;
    }

    public void setChangeDateString(String changeDateString) {
        this.changeDateString = changeDateString;
        this.changeDate = DateUtils.parseUSDate("yyyyMMdd", changeDateString);
    }

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public TMTDrug.Type getType() {
        return type;
    }

    public void setType(TMTDrug.Type type) {
        this.type = type;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final TradeDrug other = (TradeDrug) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
