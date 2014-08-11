/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
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

    private final DecimalFormat formatter = new DecimalFormat("########.##");
    @XlsColumn
    @NotEmpty(message = "hospDrugCode may not be empty")
    @Size(max = 30, message = "hospDrugCode size must not more than {max}.")
    private String hospDrugCode;

    @XlsColumn
    @Size(max = 1, message = "productCat size must not more than {max}.")
    @ValueSet(values = {"1", "2", "3", "4", "5", "6", "7"}, message = "productCat จะต้องมีค่า 1,2,3,4,5,6,7")
    private String productCat;

    @XlsColumn
    @Size(min = 6, max = 6, message = "tmtId size must be {max}.")
    private String tmtId;

    @XlsColumn
    @Size(max = 2, message = "specPrep size must not more than {max}.")
    @StartWith(values = {"F", "M", "R"}, message = "specPrep Must start with F,M,R")
    private String specPrep;

    @XlsColumn
    @Size(max = 255, message = "genericName size must not more than {max}.")
    @NotEmpty(message = "genericName may not be empty")
    private String genericName;
    @XlsColumn
    @Size(max = 255, message = "tradeName size must not more than {max}.")
    @NotEmpty(message = "tradeName may not be empty")
    private String tradeName;

    @XlsColumn(columnNames = {"dfsCode", "dsfCode"})
    @Size(max = 100, message = "dfsCode size must not more than {max}.")
    private String dfsCode;

    @XlsColumn
    @Size(max = 255, message = "dosageForm size must not more than {max}.")
    @NotEmpty(message = "dosageForm may not be empty")
    private String dosageForm;

    @XlsColumn
    @Size(max = 255, message = "strength size must not more than {max}.")
    @NotEmpty(message = "strength may not be empty")
    private String strength;

    @XlsColumn
    @Size(max = 100, message = "content size must not more than {max}.")
    @NotEmpty(message = "content may not be empty")
    private String content;

    @XlsColumn
    @Size(max = 11, message = "unitPrice size must not more than {max} (########.##).")
    @NotEmpty(message = "unitPrice may not be empty")
    @DoubleValue(message = "unitPrice is not decimal number.")
    private String unitPrice;

    @XlsColumn
    @Size(max = 255, message = "distributor size must not more than {max}.")
    private String distributor;

    @XlsColumn(columnNames = {"manufacturer", "manufacture"})
    @Size(max = 255, message = "manufacturer size must not more than {max}.")
    @NotEmpty(message = "manufacturer may not be empty")
    private String manufacturer;

    @XlsColumn
    @Size(max = 2, message = "ised size must not more than {max}.")
    @NotEmpty(message = "ised may not be empty")
    @ValueSet(values = {"E", "N", "E*"}, message = "ised must be \"E,N,E*\" only.")
    private String ised;

    @XlsColumn
    @NDC24(message = "NDC24 length must be 24 and digit only.")
    private String ndc24;

    @XlsColumn
    @Size(max = 100, message = "packSize size must not more than {max}.")
    private String packSize;

    @XlsColumn
    @Size(max = 11, message = "packPrice size must not more than {max} (########.##).")
    @DoubleValue(message = "packPrice is not decimal number.")
    private String packPrice;

    @XlsColumn
    @Size(max = 1, message = "updateFlag size must not more than {max}.")
    @NotEmpty(message = "updateFlag may not be empty")
    @ValueSet(values = {"A", "D", "E", "U"}, message = "updateFlag must be \"A,D,E,U\" only.")
    private String updateFlag;

    @XlsColumn
    @NotEmpty(message = "dateChange may not be empty for update flag A,E,D")
    @DateAndOptionalTime(message = "dateChange ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)", groups = AEDGroup.class)
    private String dateChange;
    @XlsColumn
    @NotEmpty(message = "dateUpdate may not be empty for update flag U", groups = UGroup.class)
    @DateAndOptionalTime(message = "dateUpdate ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateUpdate;

    @XlsColumn
    @NotEmpty(message = "dateEffective may not be empty for update flag U", groups = UGroup.class)
    @DateAndOptionalTime(message = "dateEffective ไม่ถูกต้องตาม format dd/mm/yyyy hh:mm (hh:mm เป็น optional)")
    private String dateEffective;
    private int rowNum;
    private String hcode;
    private Map<String, List<String>> errorMap = new HashMap<>();
    private String originalDateChange;
    private String originaleDateUpdate;
    private String originalDateEffective;

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

    public String getOriginalDateChange() {
        return originalDateChange;
    }

    public void setOriginalDateChange(String originalDateChange) {
        this.originalDateChange = originalDateChange;
    }

    public String getOriginaleDateUpdate() {
        return originaleDateUpdate;
    }

    public void setOriginaleDateUpdate(String originaleDateUpdate) {
        this.originaleDateUpdate = originaleDateUpdate;
    }

    public String getOriginalDateEffective() {
        return originalDateEffective;
    }

    public void setOriginalDateEffective(String originalDateEffective) {
        this.originalDateEffective = originalDateEffective;
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

    public void addErrors(Set<ConstraintViolation<HospitalDrugExcelModel>> violations) {
        for (ConstraintViolation<HospitalDrugExcelModel> violation : violations) {
            addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
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
            equal = equal && this.dateUpdate.equals(other.dateUpdate);
        } else {
            equal = equal && this.dateChange.equals(other.dateChange);
        }
        return equal;
    }

    public void cutFractionMorethan2() {
        unitPrice = cutFraction(unitPrice);
        packPrice = cutFraction(packPrice);
    }

    private String cutFraction(String toCut) {
        try {
            if (toCut != null) {
                Number parsed = formatter.parse(toCut);
                return formatter.format(parsed);
            }
        } catch (Exception ex) {
            //Logger.getLogger(HospitalDrugExcelModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toCut;
    }

    public void subtractYearIsWrongYear() {
        originalDateChange = dateChange;
        originaleDateUpdate = dateUpdate;
        originalDateEffective = dateEffective;
        dateChange = subtractYearIsWrongYear(dateChange);
        dateUpdate = subtractYearIsWrongYear(dateUpdate);
        dateEffective = subtractYearIsWrongYear(dateEffective);
    }

    private String subtractYearIsWrongYear(String dateToParse) {
        if (!Strings.isNullOrEmpty(dateToParse)) {
            try {
                Date parsed = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(dateToParse);
                Calendar calendar = Calendar.getInstance(Locale.US);
                calendar.setTime(parsed);
                if (calendar.get(Calendar.YEAR) > 2500) {
                    calendar.roll(Calendar.YEAR, -543);
                    return DateUtils.format("dd/MM/yyyy HH:mm", calendar.getTime());
                }
            } catch (Exception illegalArgumentException) {
                //Ignored
            }
        }
        return dateToParse;
    }

}
