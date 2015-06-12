/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Array;
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
        returnParameter = @PLSQLParameter(name = "RESULT", databaseType = "HOSPITALDRUG"))
@Embeddable
@Struct(
        name = "HOSPITALDRUG",
        fields = {
            "tmtid",
            "tmt_type",
            "fsn",
            "manufacturer",
            "hosp_genericName",
            "hosp_tradeName",
            "unit_price",
            "unitprice",
            "SPECPREP",
            "is_ed",
            "ndc24",
            "deleted",
            "approved",
            "productcat",
            "TMT_DOSAGEFORM",
            "DOSAGEFORM_GROUP",
            "REIMB_UNIT_PRICE",
            "drggroup",
            "content",
            "ISED_STATUS"})
@PLSQLRecord(
        name = "HOSPITALDRUG",
        compatibleType = "HOSPITALDRUG",
        javaType = HospitalDrugWithTMT.class,
        fields = {
            //            @PLSQLParameter(name = "p_hospDrugCode"),
            //            @PLSQLParameter(name = "p_hcode"),
            //            @PLSQLParameter(name = "p_tmtid"),
            //            @PLSQLParameter(name = "p_date")})
            @PLSQLParameter(name = "tmtid"),
            @PLSQLParameter(name = "tmt_type"),
            @PLSQLParameter(name = "fsn"),
            @PLSQLParameter(name = "manufacturer"),
            @PLSQLParameter(name = "hosp_genericName"),
            @PLSQLParameter(name = "hosp_tradeName"),
            @PLSQLParameter(name = "unit_price", databaseType = "NUMERIC_TYPE"),
            @PLSQLParameter(name = "unitprice", databaseType = "NUMERIC_TYPE"),
            @PLSQLParameter(name = "SPECPREP"),
            @PLSQLParameter(name = "is_ed"),
            @PLSQLParameter(name = "ndc24"),
            @PLSQLParameter(name = "deleted"),
            @PLSQLParameter(name = "approved"),
            @PLSQLParameter(name = "productcat"),
            @PLSQLParameter(name = "TMT_DOSAGEFORM"),
            @PLSQLParameter(name = "DOSAGEFORM_GROUP"),
            @PLSQLParameter(name = "REIMB_UNIT_PRICE", databaseType = "NUMERIC_TYPE"),
            @PLSQLParameter(name = "drggroup"),
            @PLSQLParameter(name = "content"),
            @PLSQLParameter(name = "ISED_STATUS")})
public class HospitalDrugWithTMT implements Serializable {

    public static enum Status {

        REQUEST, REJECT, ACCEPT
    }
    @Column(name = "tmtid")
    private String tmtid;
    @Column(name = "tmt_type")
    private String tmt_type;
    @Column(name = "fsn")
    private String fsn;
    @Column(name = "manufacturer")
    private String manufacturer;
    @Column(name = "hosp_genericName")
    private String hosp_genericName;
    @Column(name = "hosp_tradeName")
    private String hosp_tradeName;
    @Column(name = "unit_price")
    private Double unit_price;
    @Column(name = "unitprice")
    private Double unitprice;
    @Column(name = "SPECPREP")
    private String SPECPREP;
    @Column(name = "is_ed")
    private String is_ed;
    @Column(name = "ndc24")
    private String ndc24;
    @Column(name = "deleted")
    private String deleted;
    @Column(name = "approved")
    private String approved;
    @Column(name = "productcat")
    private String productcat;
    @Column(name = "TMT_DOSAGEFORM")
    private String TMT_DOSAGEFORM;
    @Column(name = "DOSAGEFORM_GROUP")
    private String DOSAGEFORM_GROUP;
    @Column(name = "REIMB_UNIT_PRICE")
    private BigDecimal REIMB_UNIT_PRICE;
    @Column(name = "drggroup")
    private Array drggroup;
    @Column(name = "content")
    private String content;
    @Column(name = "ISED_STATUS")
    private String ISED_STATUS;

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public String getTmt_type() {
        return tmt_type;
    }

    public void setTmt_type(String tmt_type) {
        this.tmt_type = tmt_type;
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

    public String getHosp_genericName() {
        return hosp_genericName;
    }

    public void setHosp_genericName(String hosp_genericName) {
        this.hosp_genericName = hosp_genericName;
    }

    public String getHosp_tradeName() {
        return hosp_tradeName;
    }

    public void setHosp_tradeName(String hosp_tradeName) {
        this.hosp_tradeName = hosp_tradeName;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public String getSPECPREP() {
        return SPECPREP;
    }

    public void setSPECPREP(String SPECPREP) {
        this.SPECPREP = SPECPREP;
    }

    public String getIs_ed() {
        return is_ed;
    }

    public void setIs_ed(String is_ed) {
        this.is_ed = is_ed;
    }

    public String getNdc24() {
        return ndc24;
    }

    public void setNdc24(String ndc24) {
        this.ndc24 = ndc24;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getProductcat() {
        return productcat;
    }

    public void setProductcat(String productcat) {
        this.productcat = productcat;
    }

    public String getTMT_DOSAGEFORM() {
        return TMT_DOSAGEFORM;
    }

    public void setTMT_DOSAGEFORM(String TMT_DOSAGEFORM) {
        this.TMT_DOSAGEFORM = TMT_DOSAGEFORM;
    }

    public String getDOSAGEFORM_GROUP() {
        return DOSAGEFORM_GROUP;
    }

    public void setDOSAGEFORM_GROUP(String DOSAGEFORM_GROUP) {
        this.DOSAGEFORM_GROUP = DOSAGEFORM_GROUP;
    }

    public BigDecimal getREIMB_UNIT_PRICE() {
        return REIMB_UNIT_PRICE;
    }

    public void setREIMB_UNIT_PRICE(BigDecimal REIMB_UNIT_PRICE) {
        this.REIMB_UNIT_PRICE = REIMB_UNIT_PRICE;
    }

    public Array getDrggroup() {
        return drggroup;
    }

    public void setDrggroup(Array drggroup) {
        this.drggroup = drggroup;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.tmtid);
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
        if (!Objects.equals(this.tmtid, other.tmtid)) {
            return false;
        }
        return true;
    }

}
