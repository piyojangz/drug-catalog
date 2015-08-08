/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_DRUG")
public class TMTDrugDosageFormGroup implements Serializable {
    
    @XlsColumn(columnNames = {"TPUCODE"})
    @Id
    @Column(name = "TMTID", length = 6, nullable = false)
    private String tmtId;
    
    @XlsColumn
    @ManyToOne
    @JoinColumn(name = "DOSAGEFORM_GROUP",insertable = false,updatable = false)
    private DosageFormGroup dosageformGroup;

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public DosageFormGroup getDosageformGroup() {
        return dosageformGroup;
    }

    public void setDosageformGroup(DosageFormGroup dosageformGroup) {
        this.dosageformGroup = dosageformGroup;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.tmtId);
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
        final TMTDrugDosageFormGroup other = (TMTDrugDosageFormGroup) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }
    
    
}
