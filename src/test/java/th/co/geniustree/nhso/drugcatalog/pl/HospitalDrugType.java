/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.pl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Array;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author pramoth
 */
@Embeddable


public class HospitalDrugType implements Serializable {

    /**
     * tmtid varchar2(6), tmt_type varchar2(6), fsn nvarchar2(1000),
     * manufacturer nvarchar2(255), hosp_genericName NVARCHAR2(255),
     * hosp_tradeName NVARCHAR2(255), unit_price number(10,2), unitprice
     * number(10,2), SPECPREP varchar(4), is_ed varchar2(2), ndc24 varchar2(24),
     * deleted varchar2(1),
     *
     * approved varchar2(1), productcat varchar(3), TMT_DOSAGEFORM varchar(255),
     * DOSAGEFORM_GROUP varchar(255), REIMB_UNIT_PRICE number(10,2), content
     * varchar(255), ISED_STATUS varchar(2), drggroup druggroup
     */
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
    @Column(name = "approve")
    private String approve;
    @Column(name = "product_cat")
    private String product_cat;
    @Column(name = "dosage_form")
    private String dosage_form;
    @Column(name = "dosage_form_group")
    private String dosage_form_group;
    @Column(name = "reimburse_unit_price")
    private BigDecimal reimburse_unit_price;
    @Column(name = "druggroup")
    private Array druggroup;

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

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getProduct_cat() {
        return product_cat;
    }

    public void setProduct_cat(String product_cat) {
        this.product_cat = product_cat;
    }

    public String getDosage_form() {
        return dosage_form;
    }

    public void setDosage_form(String dosage_form) {
        this.dosage_form = dosage_form;
    }

    public String getDosage_form_group() {
        return dosage_form_group;
    }

    public void setDosage_form_group(String dosage_form_group) {
        this.dosage_form_group = dosage_form_group;
    }

    public BigDecimal getReimburse_unit_price() {
        return reimburse_unit_price;
    }

    public void setReimburse_unit_price(BigDecimal reimburse_unit_price) {
        this.reimburse_unit_price = reimburse_unit_price;
    }

    public Array getDruggroup() {
        return druggroup;
    }

    public void setDruggroup(Array druggroup) {
        this.druggroup = druggroup;
    }

}
