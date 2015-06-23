/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import th.co.geniustree.nhso.drugcatalog.Constants;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.input.Lastgroup;
import th.co.geniustree.nhso.drugcatalog.input.validator.DateRange;
import th.co.geniustree.nhso.drugcatalog.input.validator.DoubleValue;
import th.co.geniustree.nhso.drugcatalog.input.validator.NDC24;
import th.co.geniustree.nhso.drugcatalog.input.validator.StartWith;
import th.co.geniustree.nhso.drugcatalog.input.validator.ValueSet;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_UPLOADHOSPDRUG_ITEM",
        indexes = {
            @Index(name = "UPLOAD_ITEM_HOSPDRUGCODE", columnList = "HOSPDRUGCODE"),
            @Index(name = "UPLOAD_ITEM_DATECHANGE", columnList = "DATECHANGE"),
            @Index(name = "UPLOAD_ITEM_DATEUPDATE", columnList = "DATEUPDATE"),
            @Index(name = "UPLOAD_ITEM_UPDATEFLAG", columnList = "UPDATEFLAG"),
            @Index(name = "UPLOADITEM_STATUS_IDX", columnList = "REQUEST_STATUS"),
            @Index(name = "UPLOADITEM_EFFECTDATE_IDX", columnList = "DATEEFFECTIVEDATE")
        })
@NamedStoredProcedureQuery(name = "UploadHospitalDrugItem.INITIAL_HOSPITAL_DRUG", procedureName = "INITIAL_HOSPITAL_DRUG")
public class UploadHospitalDrugItem implements Serializable {

    public static enum Status {

        REQUEST, REJECT, ACCEPT
    }
    @Id
    @TableGenerator(name = "TMT_UPLOADHOSPDRUG_ITEM_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            allocationSize = 100,
            pkColumnValue = "TMT_UPLOADHOSPDRUG_ITEM")
    @GeneratedValue(generator = "TMT_UPLOADHOSPDRUG_ITEM_GEN", strategy = GenerationType.TABLE)
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
    private String dateChange;

    @Column(name = "DATEUPDATE", nullable = true)
    private String dateUpdate;

    @Column(name = "DATEEFFECTIVE", nullable = false)
    private String dateEffective;

    @Column(name = "DATECHANGEDATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateChangeDate;

    @Column(name = "DATEUPDATEDATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdateDate;

    @NotNull(message = "ต้องกำหนด DateEffective มาด้วยทุกครั้ง")
    @Column(name = "DATEEFFECTIVEDATE", nullable = false)
    @DateRange(message = "ปี จะต้องไม่น้อยกว่า {min} และไม่มากกว่าปีปัจจุบัน + {futureOffset}", futureOffset = 5)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEffectiveDate;

    @ManyToOne
    @JoinColumn(name = "TMTID", insertable = false, updatable = false)
    private TMTDrug tmtDrug;

    @OneToOne(mappedBy = "uploadDrugItem", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private RequestItem requestItem;

    @OneToOne(mappedBy = "uploadHospDrugItem",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private HospitalDrugTran hospitalDrug;

    public HospitalDrugTran getHospitalDrug() {
        return hospitalDrug;
    }

    public void setHospitalDrug(HospitalDrugTran hospitalDrug) {
        this.hospitalDrug = hospitalDrug;
    }

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
        if (dateChange != null && !Strings.isNullOrEmpty(dateChange)) {
            this.dateChangeDate = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(dateChange);
        }
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
        if (dateUpdate != null && !Strings.isNullOrEmpty(dateUpdate)) {
            this.dateUpdateDate = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(dateUpdate);
        }
    }

    public String getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(String dateEffective) {
        this.dateEffective = dateEffective;
        if (dateEffective != null) {
            this.dateEffectiveDate = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(dateEffective);
        }
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
        if (dateChangeDate != null) {
            dateChange = DateUtils.format(Constants.TMT_DATETIME_FORMAT, dateChangeDate);
        }
    }

    public void setDateUpdateDate(Date dateUpdateDate) {
        this.dateUpdateDate = dateUpdateDate;
        if (dateUpdateDate != null) {
            dateUpdate = DateUtils.format(Constants.TMT_DATETIME_FORMAT, dateUpdateDate);
        }
    }

    public void setDateEffectiveDate(Date dateEffectiveDate) {
        this.dateEffectiveDate = dateEffectiveDate;
        if (dateEffectiveDate != null) {
            dateEffective = DateUtils.format(Constants.TMT_DATETIME_FORMAT, dateEffectiveDate);
        }
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
        final UploadHospitalDrugItem other = (UploadHospitalDrugItem) obj;
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
        if (this.hospDrugCode != null) {
            this.hospDrugCode = this.hospDrugCode.trim();
        }
    }

}
