/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author moth
 */
@Entity
@Table(name="TMT_UPLOADHOSP_DRUG")
public class UploadHospitalDrug implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String hcode;
    @OneToMany(mappedBy = "uploadDrug", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UploadHospitalDrugItem> passItems;
    private String originalFilename;
    private String savedFileName;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;
    private Integer itemCount;
    private Integer passItemCount;
    private String shaHex;

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
    

    public List<UploadHospitalDrugItem> getPassItems() {
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
