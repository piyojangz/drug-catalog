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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
//TODO Duplicat table maybe cause probremm on create schema
@Entity
@Table(name = "TMT_DRUG")
public class GenericDrug implements Serializable, Typeable {

    @Id
    @XlsColumn(columnNames = {"TMTID(GPU)", "TMTID(GP)", "TMTID(SUBS)", "TMTID(VTM)"})
    @Column(name = "TMTID", length = 6, nullable = false)
    private String tmtId;
    @Transient
    @XlsColumn(columnNames = "changeDate")
    private String changeDateString;
    
    @Column(name = "CHANGEDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date changeDate;
    
    @XlsColumn
    @Column(name = "FSN", length = 500)
    private String fsn;
    @Column(name = "TYPE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private TMTDrug.Type type;

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
        final GenericDrug other = (GenericDrug) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
