/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.avro.generic.GenericData;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.input.validator.DateAndOptionalTime;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
public class HospitalDrugExcelModel implements Serializable {

    @XlsColumn
    @NotEmpty
    private String hospDrugCode;
    @XlsColumn
    @NotEmpty
    private String productCat;
    @XlsColumn
    private String tmtId;
    @XlsColumn
    private String specPrep;
    @XlsColumn
    @NotEmpty
    private String genericName;
    @XlsColumn
    @NotEmpty
    private String tradeName;
    @XlsColumn
    private String dsfCode;
    @XlsColumn
    @NotEmpty
    private String dosageForm;
    @XlsColumn
    @NotEmpty
    private String strength;
    @XlsColumn
    @NotEmpty
    private String content;
    @XlsColumn
    @NotEmpty
    private String unitPrice;
    @XlsColumn
    @NotEmpty
    private String distributor;
    @XlsColumn
    @NotEmpty
    private String manufacturer;
    @XlsColumn
    @NotEmpty
    private String ised;
    @XlsColumn
    private String ndc24;
    @XlsColumn
    private String packSize;
    @XlsColumn
    private String packPrice;
    @XlsColumn
    @NotEmpty
    private String updateFlag;
    @XlsColumn
    @NotEmpty
    @DateAndOptionalTime(message = "dateChange ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateChange;
    @XlsColumn
    @NotEmpty
    @DateAndOptionalTime(message = "dateUpdate ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateUpdate;
    @XlsColumn
    @NotEmpty
    @DateAndOptionalTime(message = "dateEffective ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateEffective;
    private int rowNum;
    private Map<String, List<String>> errorMap = new HashMap<>();

    public String getHospDrugCode() {
        return hospDrugCode;
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

    public void setStrength(String atrength) {
        this.strength = atrength;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
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

    public Map<String, List<String>> getErrorMap() {
        return errorMap;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    public String getErrors(){
        return Joiner.on(",").join(errorMap.values());
    }

    public void setErrorMap(Map<String, List<String>> errorMap) {
        this.errorMap = errorMap;
    }

    public void addError(String propertyPath, String message) {
        if (!errorMap.containsKey(propertyPath)) {
            errorMap.put(propertyPath, new ArrayList<String>());
        }
        errorMap.get(propertyPath).add(message);
    }

}
