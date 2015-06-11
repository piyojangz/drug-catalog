/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.annotations.Struct;
import org.eclipse.persistence.platform.database.oracle.annotations.NamedPLSQLStoredFunctionQuery;
import org.eclipse.persistence.platform.database.oracle.annotations.PLSQLParameter;
import org.eclipse.persistence.platform.database.oracle.annotations.PLSQLRecord;

/**
 *
 * @author Thanthathon
 */
@NamedPLSQLStoredFunctionQuery(
        name = "getHospitalDrugWithTMT",
        functionName = "HOSPITALDRUG_PACK.find_hospdrug_withtmt",
        returnParameter = @PLSQLParameter(name = "RESULT", databaseType = "HOSPITALDRUG_PACK.hospitaldrug"))
@Embeddable
@Struct(
        name = "DRUGCODEOWNER.HOSPITALDRUG",
        fields = {
            "hcode",
            "hospDrugCode",
            "dateEffective",
            "tmtid",
            "tmt_type",
            "fsn",
            "manufacturer",
            "hosp_genericName",
            "hosp_tradeName",
            "unit_price",
            "SPECPREP",
            "is_ed",
            "ndc24",
            "deleted",
            "approved",
            "productcat",
            "TMT_DOSAGEFORM",
            "DOSAGEFORM_GROUP",
            "REIMB_UNIT_PRICE",
            "druggroup"})
@PLSQLRecord(
        name = "HOSPITALDRUG_PACK.hospitaldrug",
        compatibleType = "DRUGCODEOWNER.HOSPITALDRUG",
        javaType = HospitalDrugWithTMT.class,
        fields = {
            @PLSQLParameter(name = "p_hospDrugCode"),
            @PLSQLParameter(name = "p_hcode"),
            @PLSQLParameter(name = "p_tmtid"),
            @PLSQLParameter(name = "p_date")})
//            @PLSQLParameter(name = "tmtid"),
//            @PLSQLParameter(name = "tmt_type"),
//            @PLSQLParameter(name = "fsn"),
//            @PLSQLParameter(name = "manufacturer"),
//            @PLSQLParameter(name = "hosp_genericName"),
//            @PLSQLParameter(name = "hosp_tradeName"),
//            @PLSQLParameter(name = "unit_price"),
//            @PLSQLParameter(name = "SPECPREP"),
//            @PLSQLParameter(name = "is_ed"),
//            @PLSQLParameter(name = "ndc24"),
//            @PLSQLParameter(name = "deleted"),
//            @PLSQLParameter(name = "approved"),
//            @PLSQLParameter(name = "productcat"),
//            @PLSQLParameter(name = "TMT_DOSAGEFORM"),
//            @PLSQLParameter(name = "DOSAGEFORM_GROUP"),
//            @PLSQLParameter(name = "REIMB_UNIT_PRICE"),
//            @PLSQLParameter(name = "druggroup")})
public class HospitalDrugWithTMT implements Serializable {

    public static enum Status {
        REQUEST, REJECT, ACCEPT
    }
    
    @Column(name = "HCODE")
    private String hcode;
    
    @Column(name = "HOSPDRUGCODE")
    private String hospDrugCode;
    
    @Column(name = "DATE_EFFECTIVE")
    @Temporal(TemporalType.DATE)
    private Date dateEffective;
    
    @Column(name = "TMT_ID", nullable = true, length = 6)
    private String tmtId;

    @Column(name = "TMT_TYPE")
    private String tmtType;

    @Column(name = "FSN")
    private String fsn;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Column(name = "GENERICNAME")
    private String genericName;

    @Column(name = "TRADENAME")
    private String tradeName;

    @Column(name = "UNITPRICE", nullable = false)
    private String unitPrice;

    @Column(name = "SPECPREP", length = 3)
    private String specPrep;

    @Column(name = "ISED_STATUS")
    private String isedStatus;

    @Column(name = "NDC24", length = 24)
    private String ndc24;

    @Column(name = "DELETED")
    private Boolean deleted;

    @Column(name = "APPROVED")
    private String approve;

    @Column(name = "PRODUCTCAT", length = 3)
    private String productCat;

    @Column(name = "DOSAGEFORM")
    private String dosageForm;

    @Column(name = "DOSAGEFORMGROUP")
    private String dosageFormGroup;

    @Column(name = "REIMBURSE_UNIT_PRICE")
    private BigDecimal reimburseUnitPrice;

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getTmtType() {
        return tmtType;
    }

    public void setTmtType(String tmtType) {
        this.tmtType = tmtType;
    }

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSpecPrep() {
        return specPrep;
    }

    public void setSpecPrep(String specPrep) {
        this.specPrep = specPrep;
    }

    public String getIsedStatus() {
        return isedStatus;
    }

    public void setIsedStatus(String isedStatus) {
        this.isedStatus = isedStatus;
    }

    public String getNdc24() {
        return ndc24;
    }

    public void setNdc24(String ndc24) {
        this.ndc24 = ndc24;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getDosageFormGroup() {
        return dosageFormGroup;
    }

    public void setDosageFormGroup(String dosageFormGroup) {
        this.dosageFormGroup = dosageFormGroup;
    }

    public BigDecimal getReimburseUnitPrice() {
        return reimburseUnitPrice;
    }

    public void setReimburseUnitPrice(BigDecimal reimburseUnitPrice) {
        this.reimburseUnitPrice = reimburseUnitPrice;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.tmtId);
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
        final HospitalDrugWithTMT other = (HospitalDrugWithTMT) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
