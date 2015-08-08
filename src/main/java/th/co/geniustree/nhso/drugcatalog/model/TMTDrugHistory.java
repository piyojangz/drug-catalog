/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Version;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_DRUG_HISTORY")
public class TMTDrugHistory implements Serializable, TMT {

    @Id
    @TableGenerator(name = "TMT_DRUG_HISTORY_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            allocationSize = 100,
            pkColumnValue = "TMT_DRUG_HISTORY")
    @GeneratedValue(generator = "TMT_DRUG_HISTORY_GEN", strategy = GenerationType.TABLE)
    private BigInteger id;
    
    @XlsColumn(columnNames = {"TPUCODE"})
    @Column(name = "TMTID", length = 6, nullable = false)
    private String tmtId;
    @XlsColumn
    @Column(name = "ACTIVEINGREDIENT", length = 300)
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
    @Column(name = "DISPUNIT", length = 255)
    private String dispUnit;
    @XlsColumn
    @Column(name = "TRADENAME", length = 255, columnDefinition = "NVARCHAR2(255)")
    private String tradeName;
    @XlsColumn
    @Column(name = "MANUFACTURER", length = 255, columnDefinition = "NVARCHAR2(255)")
    private String manufacturer;
    @XlsColumn
    @Column(name = "FSN", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String fsn;
    @XlsColumn
    @Column(name = "STATUS", length = 2)
    private String status;

    @Column(name = "CHANGEDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date changeDate;

    @Column(name = "CREATEDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "LASTMODIFIEDDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column(name = "TYPE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "RELEASEDATE")
    private TMTReleaseFileUpload releaseFileUpload;

    @Version
    private Integer version;

    public TMTDrugHistory() {
    }

    public TMTDrugHistory(String tmtId) {
        this.tmtId = tmtId;
    }

    @PrePersist
    public void prePersist() {
        createDate = new Date();
        lastModifiedDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        lastModifiedDate = new Date();
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String getTmtId() {
        return tmtId;
    }

    @Override
    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getChageDate() {
        return changeDate;
    }

    public void setChageDate(Date chageDate) {
        this.changeDate = chageDate;
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

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public TMTReleaseFileUpload getReleaseFileUpload() {
        return releaseFileUpload;
    }

    public void setReleaseFileUpload(TMTReleaseFileUpload releaseFileUpload) {
        this.releaseFileUpload = releaseFileUpload;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final TMTDrugHistory other = (TMTDrugHistory) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
