/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model.tmt;

import java.io.Serializable;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
@Entity
@Table(name="TMT_DRUG")
public class TMTDrug implements Serializable {

    @Id
    @Column(name = "TMT_ID", length = 10)
    private String tmtId;
    @Transient
    @XlsColumn
    private String tpuCode;
    @XlsColumn
    @Column(name = "ACTIVE_INGREDIENT", length = 300)
    private String activeIngredient;
    @XlsColumn
    @Column(name = "STRENGTH", length = 255)
    private String strength;
    @XlsColumn
    @Column(name = "DOSAGEFORM", length = 255)
    private String dosageform;
    @XlsColumn
    @Column(name = "CONTVALUE", length = 255)
    private String contvalue;
    @XlsColumn
    @Column(name = "CONTUNIT", length = 255)
    private String contunit;
    @XlsColumn
    @Column(name = "DISP_UNIT", length = 255)
    private String dispUnit;
    @XlsColumn
    @Column(name = "TRADENAME", length = 255)
    private String tradeName;
    @XlsColumn
    @Column(name = "MANUFACTURER", length = 255)
    private String manufacturer;
    @XlsColumn
    @Column(name = "FSN", length = 500)
    private String fsn;
    @XlsColumn
    @Column(name = "STATUS", length = 2)
    private String status;

    @PrePersist
    public void prePersist() {
        tmtId = tpuCode;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getTpuCode() {
        return tpuCode;
    }

    public void setTpuCode(String tpuCode) {
        this.tpuCode = tpuCode;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageform() {
        return dosageform;
    }

    public void setDosageform(String dosageform) {
        this.dosageform = dosageform;
    }

    public String getContvalue() {
        return contvalue;
    }

    public void setContvalue(String contvalue) {
        this.contvalue = contvalue;
    }

    public String getContunit() {
        return contunit;
    }

    public void setContunit(String contunit) {
        this.contunit = contunit;
    }

    public String getDispUnit() {
        return dispUnit;
    }

    public void setDispUnit(String dispUnit) {
        this.dispUnit = dispUnit;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.tmtId);
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
        final TMTDrug other = (TMTDrug) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }
    

}
