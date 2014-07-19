/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalDrugPK;

/**
 *
 * @author moth
 */
@Entity
@Table(name="TMT_HOSP_TMTDRUG")
@IdClass(HospitalDrugPK.class)
public class HospitalDrug implements Serializable {

    @Id
    private String hospDrugCode;
    @Id
    private String hcode;

    @NotEmpty
    private String productCat;

    private String tmtId;

    private String specPrep;

    @NotEmpty
    private String genericName;

    @NotEmpty
    private String tradeName;

    private String dsfCode;

    @NotEmpty
    private String dosageForm;

    @NotEmpty
    private String strength;

    @NotEmpty
    private String content;

    @NotEmpty
    private String unitPrice;

    @NotEmpty
    private String distributor;

    @NotEmpty
    private String manufacturer;

    @NotEmpty
    private String ised;

    private String ndc24;

    private String packSize;

    private String packPrice;

    @NotEmpty
    private String updateFlag;

    @NotEmpty
    private String dateChange;

    @NotEmpty
    private String dateUpdate;

    @NotEmpty
    private String dateEffective;
    private Boolean approved = Boolean.FALSE;
    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "HCODE",referencedColumnName = "HCODE",nullable = false),
        @JoinColumn(name = "HOSPDRUGCODE",referencedColumnName = "HOSPDRUGCODE",nullable = false)
    })
    private List<Price> prices;
    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "HCODE",referencedColumnName = "HCODE",nullable = false),
        @JoinColumn(name = "HOSPDRUGCODE",referencedColumnName = "HOSPDRUGCODE",nullable = false)
    })
    private List<HospitalEdNed> edNeds;


    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }
    

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }


    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getSpecPrep() {
        return specPrep;
    }

    public void setSpecPrep(String specPrep) {
        this.specPrep = specPrep;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getDsfCode() {
        return dsfCode;
    }

    public void setDsfCode(String dsfCode) {
        this.dsfCode = dsfCode;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getIsed() {
        return ised;
    }

    public void setIsed(String ised) {
        this.ised = ised;
    }

    public String getNdc24() {
        return ndc24;
    }

    public void setNdc24(String ndc24) {
        this.ndc24 = ndc24;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(String packPrice) {
        this.packPrice = packPrice;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getDateChange() {
        return dateChange;
    }

    public void setDateChange(String dateChange) {
        this.dateChange = dateChange;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public List<HospitalEdNed> getEdNeds() {
        return edNeds;
    }

    public void setEdNeds(List<HospitalEdNed> edNeds) {
        this.edNeds = edNeds;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.hospDrugCode);
        hash = 29 * hash + Objects.hashCode(this.hcode);
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
        final HospitalDrug other = (HospitalDrug) obj;
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        return true;
    }


}
