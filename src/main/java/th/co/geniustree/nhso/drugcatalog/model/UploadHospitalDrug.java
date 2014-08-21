/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_UPLOADHOSPDRUG", indexes = {
    @Index(name = "UPLOADHOSPDRUG_HCODE_IDX", columnList = "HCODE"),
    @Index(name = "UPLOADHOSPDRUG_SHAHEX_IDX", columnList = "SHAHEX", unique = true)
})
public class UploadHospitalDrug implements Serializable {

    @Id
    @TableGenerator(name = "TMT_UPLOADHOSPDRUG_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_UPLOADHOSPDRUG", allocationSize = 1)
    @GeneratedValue(generator = "TMT_UPLOADHOSPDRUG_GEN", strategy = GenerationType.TABLE)

    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "HCODE", nullable = false, length = 5)
    private String hcode;

    @OneToMany(mappedBy = "uploadDrug", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UploadHospitalDrugItem> passItems;

    @OneToMany(mappedBy = "uploadDrug", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UploadHospitalDrugErrorItem> errorItems;

    @Column(name = "ORIGINALFILENAME", length = 255, nullable = false)
    private String originalFilename;

    @Column(name = "SAVEDFILENAME", length = 255, nullable = false)
    private String savedFileName;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "ITEMCOUNT", nullable = false)
    private Integer itemCount = 0;

    @Column(name = "PASSITEMCOUNT", nullable = false)
    private Integer passItemCount = 0;

    @Column(name = "SHAHEX", nullable = false, length = 100)
    private String shaHex;

    @ManyToOne
    @JoinColumn(name = "HCODE", insertable = false, updatable = false)
    private Hospital hospital;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
    

    public List<UploadHospitalDrugItem> getPassItems() {
        if (passItems == null) {
            passItems = new ArrayList<>();
        }
        return passItems;
    }

    public void setPassItems(List<UploadHospitalDrugItem> passItems) {
        this.passItems = passItems;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getSavedFileName() {
        return savedFileName;
    }

    public void setSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getPassItemCount() {
        return passItemCount;
    }

    public void setPassItemCount(Integer passItemCount) {
        this.passItemCount = passItemCount;
    }

    public String getShaHex() {
        return shaHex;
    }

    public void setShaHex(String shaHex) {
        this.shaHex = shaHex;
    }

    public List<UploadHospitalDrugErrorItem> getErrorItems() {
        if (errorItems == null) {
            errorItems = new ArrayList<>();
        }
        return errorItems;
    }

    public void setErrorItems(List<UploadHospitalDrugErrorItem> errorItems) {
        this.errorItems = errorItems;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final UploadHospitalDrug other = (UploadHospitalDrug) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
