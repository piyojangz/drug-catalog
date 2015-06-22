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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_HOSPDRUG_TRANS")
public class HospitalDrugTran implements Serializable {

    @Id
    @Column(name = "UPLOADHOSPDRUG_ITEM_ID")
    private Integer id;

    @Column(name = "ISED_APPROVED", insertable = false, updatable = false)
    private String isedApprove;

    @OneToOne
    @JoinColumn(name = "UPLOADHOSPDRUG_ITEM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UploadHospitalDrugItem uploadHospDrug_Item;

    public UploadHospitalDrugItem getUploadHospDrug_Item() {
        return uploadHospDrug_Item;
    }

    public void setUploadHospDrug_Item(UploadHospitalDrugItem uploadHospDrug_Item) {
        this.uploadHospDrug_Item = uploadHospDrug_Item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsedApprove() {
        return isedApprove;
    }

    public void setIsedApprove(String isedApprove) {
        this.isedApprove = isedApprove;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final HospitalDrugTran other = (HospitalDrugTran) obj;
        return Objects.equals(this.id, other.id);
    }

}
