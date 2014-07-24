/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.input.validator.DateAndOptionalTime;
import th.co.geniustree.nhso.drugcatalog.input.validator.DoubleValue;
import th.co.geniustree.nhso.drugcatalog.input.validator.NDC24;
import th.co.geniustree.nhso.drugcatalog.input.validator.StartWith;
import th.co.geniustree.nhso.drugcatalog.input.validator.ValueSet;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
public class HospitalDrugExcelModel implements Serializable {

    @XlsColumn
    @NotEmpty(message = "hospDrugCode may not be empty")
    private String hospDrugCode;
    @XlsColumn
    @NotEmpty(message = "productCat may not be empty")
    @ValueSet(values = {"1", "2", "3", "4", "5", "6", "7"})
    private String productCat;
    @XlsColumn
    private String tmtId;
    @XlsColumn
    @StartWith(values = {"F", "M", "R"})
    private String specPrep;
    @XlsColumn
    @NotEmpty(message = "genericName may not be empty")
    private String genericName;
    @XlsColumn
    @NotEmpty(message = "tradeName may not be empty")
    private String tradeName;
    @XlsColumn
    private String dfsCode;
    @XlsColumn
    @NotEmpty(message = "dosageForm may not be empty")
    private String dosageForm;
    @XlsColumn
    @NotEmpty(message = "strength may not be empty")
    private String strength;
    @XlsColumn
    @NotEmpty(message = "content may not be empty")
    private String content;
    @XlsColumn
    @NotEmpty(message = "unitPrice may not be empty")
    @DoubleValue(message = "unitPrice is not decimal number.")
    private String unitPrice;
    
    @XlsColumn
    private String distributor;
    @XlsColumn
    @NotEmpty(message = "manufacturer may not be empty")
    private String manufacturer;
    @XlsColumn
    @NotEmpty(message = "ised may not be empty")
    @ValueSet(values = {"E", "N", "E*"}, message = "Must be \"E,N,E*\" only.")
    private String ised;
    @XlsColumn
    @NDC24(message = "NDC24 length must be 24 and digit only.")
    private String ndc24;
    @XlsColumn
    private String packSize;
    @XlsColumn
    @DoubleValue(message = "packPrice is not decimal number.")
    private String packPrice;
    @XlsColumn
    @NotEmpty(message = "updateFlag may not be empty")
    @ValueSet(values = {"A", "D", "E", "U"}, message = "Must be \"A,D,E,U\" only.")
    private String updateFlag;
    @XlsColumn
    @NotEmpty(message = "dateChange may not be empty")
    @DateAndOptionalTime(message = "dateChange ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateChange;
    @XlsColumn
    @NotEmpty(message = "dateUpdate may not be empty")
    @DateAndOptionalTime(message = "dateUpdate ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateUpdate;
    @XlsColumn
    @NotEmpty(message = "dateEffective may not be empty")
    @DateAndOptionalTime(message = "dateEffective ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateEffective;
    private int rowNum;
    private String hcode;
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

    public String getErrors() {
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

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public boolean isEqual(HospitalDrugExcelModel other) {
        boolean equal = this.hospDrugCode.equals(other.hospDrugCode) && this.updateFlag.equalsIgnoreCase(other.updateFlag);
        if (this.updateFlag.equalsIgnoreCase("U")) {
            //Check only date part
            equal = equal && this.dateEffective.substring(0, 10).equals(other.dateEffective.substring(0, 10));
        } else {
            equal = equal && this.dateChange.substring(0, 10).equals(other.dateChange.substring(0, 10));
        }
        return equal;
    }

}
