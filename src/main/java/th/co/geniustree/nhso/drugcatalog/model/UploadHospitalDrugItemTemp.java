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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import th.co.geniustree.nhso.drugcatalog.input.validator.DoubleValue;
import th.co.geniustree.nhso.drugcatalog.input.validator.NDC24;
import th.co.geniustree.nhso.drugcatalog.input.validator.StartWith;
import th.co.geniustree.nhso.drugcatalog.input.validator.ValueSet;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_HOSPDRUG_TRANS")
public class UploadHospitalDrugItemTemp implements Serializable {

    public static enum Status {

        REQUEST, REJECT, ACCEPT
    }
    @Id
    @Column(name="UPLOADHOSPDRUG_ITEM_ID")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "UPLOADHOSPDRUG_ID")
    private UploadHospitalDrug uploadDrug;

    @NotBlank(message = "ต้องกำหนด HospDrugCode มาด้วยทุกครั้ง")
    @Size(max = 30, message = "HospDrugCode ต้องไม่เกิน 30 ตัวอักษร")
    @Column(name = "HOSPDRUGCODE", nullable = false, length = 30)
    private String hospDrugCode;

    @NotBlank(message = "ต้องกำหนด ProductCat มาด้วยทุกครั้ง")
    @Size(max = 1, message = "ProductCat ต้องไม่เกิน 1 ตัวอักษร")
    @ValueSet(values = {"1", "2", "3", "4", "5"}, message = "ProductCat ต้องประกอบด้วย 1 หรือ 2 หรือ 3 หรือ 4 หรือ 5 เท่านั้น")
    @Column(name = "PRODUCTCAT", nullable = false, length = 3)
    private String productCat;

    @Size(min = 6, max = 6, message = "tmtId size must be {max}.")
    @Column(name = "TMTID", nullable = true, length = 6)
    private String tmtId;

    @Size(max = 3, message = "SpecRep ต้องไม่เกิน {max} ตัวอักษร")
    @StartWith(values = {"F", "M", "R"}, message = "SpecRep ตัวอักษรตัวแรก ต้องประกอบด้วย F หรือ M หรือ R เท่านั้น")
    @Column(name = "SPECPREP", nullable = true, length = 3)
    private String specPrep;

    @Size(max = 255, message = "GenericName ต้องไม่เกิน 255 ตัวอักษร")
    @NotBlank(message = "ต้องกำหนด GenericName มาด้วยทุกครั้ง")
    @Column(name = "GENERICNAME", nullable = false, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String genericName;

    @Size(max = 255, message = "TradeName ต้องไม่เกิน 255 ตัวอักษร")
    @NotBlank(message = "ต้องกำหนด TradeName มาด้วยทุกครั้ง")
    @Column(name = "TRADENAME", nullable = false, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String tradeName;

    @Size(max = 100, message = "DFSCode ต้องไม่เกิน 100 ตัวอักษร")
    @Column(name = "DFSCODE", nullable = true, length = 100, columnDefinition = "NVARCHAR2(100)")
    private String dfsCode;

    @Size(max = 255, message = "DosageForm ต้องไม่เกิน 255 ตัวอักษร")
    @NotBlank(message = "ต้องกำหนด DosageForm มาด้วยทุกครั้ง")
    @Column(name = "DOSAGEFORM", nullable = false, length = 255, columnDefinition = "NVARCHAR2(100)")
    private String dosageForm;

    @Size(max = 255, message = "Strength ต้องไม่เกิน 255 ตัวอักษร")
    @Column(name = "STRENGTH", nullable = true, length = 255)
    private String strength;

    @Size(max = 100, message = "Content ต้องไม่เกิน 255 ตัวอักษร")
    @NotBlank(message = "ต้องกำหนด Content มาด้วยทุกครั้ง")
    @Column(name = "CONTENT", nullable = false, length = 100)
    private String content;

    @Size(max = 11, message = "UnitPrice ต้องประกอบด้วยตัวเลขหรือจุดทศนิยม ไม่เกิน 11 ตัวอักษร (99999999.99)")
    @NotBlank(message = "ต้องกำหนด UnitPrice มาด้วยทุกครั้ง")
    @DoubleValue(message = "UnitPrice ต้องเป็นตัวเลขหรือจุดทศนิยม เท่านั้น")
    @Column(name = "UNITPRICE", nullable = false)
    private String unitPrice;

    @Size(max = 255, message = "Distributer ต้องไม่เกิน 255 ตัวอักษร")
    @Column(name = "DISTRIBUTOR", nullable = true, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String distributor;

    @Size(max = 255, message = "Manufacturer ต้องไม่เกิน 255 ตัวอักษร")
    @NotBlank(message = "ต้องกำหนด Manufacturer มาด้วยทุกครั้ง")
    @Column(name = "MANUFACTURER", nullable = false, length = 255, columnDefinition = "NVARCHAR2(255)")
    private String manufacturer;

    @Size(max = 2, message = "ISED ต้องไม่เกิน 2 ตัวอักษร")
    @NotBlank(message = "ต้องกำหนด ISED มาด้วยทุกครั้ง")
    @ValueSet(values = {"E", "N", "E*"}, message = "ISED ต้องประกอบด้วย N หรือ E หรือ E* เท่านั้น")
    @Column(name = "ISED", nullable = false, length = 2)
    private String ised;

    @NDC24(message = "NDC24 ต้องประกอบด้วยตัวเลข 24 หลัก เท่านั้น")
    @Column(name = "NDC24", nullable = true, length = 24)
    private String ndc24;

    @Size(max = 100, message = "PackSize ต้องไม่เกิน 100 ตัวอักษร")
    @Column(name = "PACKSIZE", nullable = true, length = 100)
    private String packSize;

    @Size(max = 11, message = "PackPrice ต้องประกอบด้วยตัวเลขหรือจุดทศนิยม ไม่เกิน 11 ตัวอักษร (99999999.99)")
    @DoubleValue(message = "PackPrice ต้องเป็นตัวเลขหรือจุดทศนิยม เท่านั้น")
    @Column(name = "PACKPRICE", nullable = true)
    private String packPrice;

    @Size(max = 1, message = "UpdateFlag ต้องไม่เกิน 1 ตัวอักษร")
    @NotBlank(message = "ต้องกำหนด UpdateFlag มาด้วยทุกครั้ง")
    @ValueSet(values = {"A", "D", "E", "U"}, message = "UpdateFlag ต้องประกอบด้วย A หรือ หรือ D หรือ E หรือ U เท่านั้น")
    @Column(name = "UPDATEFLAG", nullable = false, length = 1)
    private String updateFlag;


    @Column(name = "DATECHANGE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateChangeDate;

    @Column(name = "DATEUPDATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdateDate;

    @NotNull(message = "ต้องกำหนด DateEffective มาด้วยทุกครั้ง")
    @Column(name = "DATEEFFECTIVE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEffectiveDate;

    @ManyToOne
    @JoinColumn(name = "TMTID", insertable = false, updatable = false)
    private TMTDrug tmtDrug;

    @OneToOne
    @JoinColumn(name = "UPLOADHOSPDRUG_ITEM_ID", insertable = false, updatable = false)
    private RequestItem requestItem;
    @Column(name = "ISED_APPROVED")
    private String isedApprove;
    @Column(name = "NDC24_APPROVED")
    private String ndc24Approve;
    @Column(name = "ISED_STATUS")
    private String isedStatus;

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

   

    public Date getDateChangeDate() {
        return dateChangeDate;
    }

    public Date getDateUpdateDate() {
        return dateUpdateDate;
    }

    public Date getDateEffectiveDate() {
        return dateEffectiveDate;
    }

    public void setDateChangeDate(Date dateChangeDate) {
        this.dateChangeDate = dateChangeDate;
    }

    public void setDateUpdateDate(Date dateUpdateDate) {
        this.dateUpdateDate = dateUpdateDate;
    }

    public void setDateEffectiveDate(Date dateEffectiveDate) {
        this.dateEffectiveDate = dateEffectiveDate;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public RequestItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
    }

    public String getIsedApprove() {
        return isedApprove;
    }

    public void setIsedApprove(String isedApprove) {
        this.isedApprove = isedApprove;
    }

    public String getNdc24Approve() {
        return ndc24Approve;
    }

    public void setNdc24Approve(String ndc24Approve) {
        this.ndc24Approve = ndc24Approve;
    }

    public String getIsedStatus() {
        return isedStatus;
    }

    public void setIsedStatus(String isedStatus) {
        this.isedStatus = isedStatus;
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
        final UploadHospitalDrugItemTemp other = (UploadHospitalDrugItemTemp) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public void timString() {
        if (this.unitPrice != null) {
            this.unitPrice = this.unitPrice.trim();
        }
        if (this.packPrice != null) {
            this.packPrice = this.packPrice.trim();
        }
    }

}
