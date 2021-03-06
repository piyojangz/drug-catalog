/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Array;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.eclipse.persistence.annotations.Struct;

/**
 *
 * @author pramoth
 */
@Embeddable

@Struct(name = "HOSPITALDRUG", fields = {
    "tmtid", "tmt_type", "fsn", "manufacturer", "hosp_genericName", "hosp_tradeName", "unit_price", "unitprice",
    "SPECPREP", "is_ed", "approved", "productcat", "TMT_DOSAGEFORM", "DOSAGEFORM_GROUP", "REIMB_UNIT_PRICE", "content", "ISED_STATUS", "drggroup", "ndc24", "deleted"
})
public class HospitalDrugType implements Serializable {

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
    private BigDecimal unit_price;
    @Column(name = "unitprice")
    private BigDecimal unitprice;
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

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    public BigDecimal getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(BigDecimal unitprice) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getISED_STATUS() {
        return ISED_STATUS;
    }

    public void setISED_STATUS(String ISED_STATUS) {
        this.ISED_STATUS = ISED_STATUS;
    }

}