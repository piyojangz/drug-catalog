/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_UPLOADHOSPDRUG_ERRORITEM",
        indexes = {
            @Index(name = "UPLOAD_ERROR_HOSPDRUGCODE", columnList = "HOSPDRUGCODE")
        })
public class UploadHospitalDrugErrorItem implements Serializable {

    @Id
    @TableGenerator(name = "TMT_UPLOADHOSPDRUG_ERRORITEM_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_UPLOADHOSPDRUG_ERRORITEM")
    @GeneratedValue(generator = "TMT_UPLOADHOSPDRUG_ERRORITEM_GEN", strategy = GenerationType.TABLE)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "UPLOADHOSPDRUG_ID")
    private UploadHospitalDrug uploadDrug;

    @Column(name = "HOSPDRUGCODE", length = 255)
    private String hospDrugCode;

    @Column(name = "PRODUCTCAT", length = 255)
    private String productCat;

    @Column(name = "TMTID", length = 255)
    private String tmtId;

    @Column(name = "SPECPREP", length = 255)
    private String specPrep;

    @NotEmpty
    @Column(name = "GENERICNAME", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String genericName;

    @NotEmpty
    @Column(name = "TRADENAME", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String tradeName;

    @Column(name = "DFSCODE", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String dfsCode;

    @NotEmpty
    @Column(name = "DOSAGEFORM", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String dosageForm;

    @NotEmpty
    @Column(name = "STRENGTH", length = 1000)
    private String strength;

    @NotEmpty
    @Column(name = "CONTENT", length = 1000)
    private String content;

    @NotEmpty
    @Column(name = "UNITPRICE", length = 100)
    private String unitPrice;

    @Column(name = "DISTRIBUTOR", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String distributor;

    @NotEmpty
    @Column(name = "MANUFACTURER", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String manufacturer;

    @NotEmpty
    @Column(name = "ISED", length = 255)
    private String ised;

    @Column(name = "NDC24", length = 255)
    private String ndc24;

    @Column(name = "PACKSIZE", length = 255)
    private String packSize;

    @Column(name = "PACKPRICE", length = 255)
    private String packPrice;

    @NotEmpty
    @Column(name = "UPDATEFLAG", length = 255)
    private String updateFlag;

    @NotEmpty
    @Column(name = "DATECHANGE", length = 255)
    private String dateChange;

    @NotEmpty
    @Column(name = "DATEUPDATE", length = 255)
    private String dateUpdate;

    @NotEmpty
    @Column(name = "DATEEFFECTIVE", length = 255)
    private String dateEffective;

    @ElementCollection
    @CollectionTable(name = "TMT_ERRORMSG_ITEM")
    @Column(name = "ERRORMSG", length = 1500, columnDefinition = "NVARCHAR2(1500)")
    private Map<String, String> errorMap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public UploadHospitalDrug getUploadDrug() {
        return uploadDrug;
    }

    public void setUploadDrug(UploadHospitalDrug uploadDrug) {
        this.uploadDrug = uploadDrug;
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

    public String getDfsCode() {
        return dfsCode;
    }

    public void setDfsCode(String dfsCode) {
        this.dfsCode = dfsCode;
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

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public String getErrors() {
        return Joiner.on(",").join(errorMap.values());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final UploadHospitalDrugErrorItem other = (UploadHospitalDrugErrorItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public void addAllError(Map<String, List<String>> errorMap) {
        if (this.errorMap == null) {
            this.errorMap = new HashMap<>();
        }
        for (String key : errorMap.keySet()) {
            this.errorMap.put(key, Joiner.on(",").join(errorMap.get(key)));
        }
    }

}
