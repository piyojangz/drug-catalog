/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_HOSPDRUG")
@IdClass(HospitalDrugPK.class)
public class HospitalDrug implements Serializable {

    @Id
    @Column(name = "HCODE", nullable = false, length = 5)
    private String hcode;
    @Id
    @Column(name = "HOSPDRUGCODE", nullable = false, length = 30)
    private String hospDrugCode;

    @Column(name = "PRODUCTCAT", nullable = true, length = 3)
    private String productCat;

    @Column(name = "TMTID", nullable = true, length = 6)
    private String tmtId;

    @Column(name = "SPECPREP", nullable = true, length = 2)
    private String specPrep;

    @NotEmpty
    @Column(name = "GENERICNAME", nullable = false, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String genericName;

    @NotEmpty
    @Column(name = "TRADENAME", nullable = false, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String tradeName;

    @Column(name = "DFSCODE", nullable = true, length = 100, columnDefinition = "NVARCHAR2(100)")
    private String dfsCode;

    @NotEmpty
    @Column(name = "DOSAGEFORM", nullable = false, length = 255, columnDefinition = "NVARCHAR2(100)")
    private String dosageForm;

    @Column(name = "STRENGTH", nullable = true, length = 255)
    private String strength;

    @NotEmpty
    @Column(name = "CONTENT", nullable = false, length = 100)
    private String content;

    @NotEmpty
    @Column(name = "UNITPRICE", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "DISTRIBUTOR", nullable = true, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String distributor;

    @NotEmpty
    @Column(name = "MANUFACTURER", nullable = false, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String manufacturer;

    @NotEmpty
    @Column(name = "ISED", nullable = false, length = 2)
    private String ised;

    @Column(name = "NDC24", nullable = true, length = 24)
    private String ndc24;

    @Column(name = "PACKSIZE", nullable = true, length = 100)
    private String packSize;

    @Column(name = "PACKPRICE", precision = 10, scale = 2, nullable = true)
    private BigDecimal packPrice;

    @NotEmpty
    @Column(name = "UPDATEFLAG", nullable = false, length = 1)
    private String updateFlag;

    @NotEmpty
    @Column(name = "DATECHANGE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateChange;

    @NotEmpty
    @Column(name = "DATEUPDATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @NotEmpty
    @Column(name = "DATEEFFECTIVE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEffective;

    @Column(name = "APPROVED", nullable = false)
    private Boolean approved = Boolean.FALSE;

    @Column(name = "DELETED", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "HCODE", referencedColumnName = "HCODE", nullable = false),
        @JoinColumn(name = "HOSPDRUGCODE", referencedColumnName = "HOSPDRUGCODE", nullable = false)
    })
    private List<HospitalPrice> prices;
    
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "HCODE", referencedColumnName = "HCODE", nullable = false),
        @JoinColumn(name = "HOSPDRUGCODE", referencedColumnName = "HOSPDRUGCODE", nullable = false)
    })
    private List<HospitalEdNed> edNeds;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", nullable = false, insertable = false, updatable = false)
    private TMTDrug tmtDrug;
    
    @ManyToOne
    @JoinColumn(name = "HCODE", insertable = false, updatable = false)
    private Hospital hospital;
    
    @OneToOne(mappedBy = "targetItem")
    private RequestItem requestItem;

    @Version
    private Integer version;

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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
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

    public BigDecimal getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(BigDecimal packPrice) {
        this.packPrice = packPrice;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public List<HospitalPrice> getPrices() {
        if (prices == null) {
            prices = new ArrayList<>();
        }
        return prices;
    }

    public void setPrices(List<HospitalPrice> prices) {
        this.prices = prices;
    }

    public List<HospitalEdNed> getEdNeds() {
        if (edNeds == null) {
            edNeds = new ArrayList<>();
        }
        return edNeds;
    }

    public void setEdNeds(List<HospitalEdNed> edNeds) {
        this.edNeds = edNeds;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public RequestItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
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
