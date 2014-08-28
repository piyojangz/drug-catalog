/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.Constants;
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
    @NotEmpty(message = "ต้องกำหนด HospDrugCode มาด้วยทุกครั้ง")
    @Size(max = 30, message = "HospDrugCode ต้องไม่เกิน 30 ตัวอักษร")
    private String hospDrugCode;

    @XlsColumn
    @Size(max = 1, message = "ProductCat ต้องไม่เกิน 1 ตัวอักษร")
    @ValueSet(values = {"1", "2", "3", "4", "5", "6", "7"}, message = "ProductCat ต้องประกอบด้วย 1 หรือ 2 หรือ 3 หรือ 4 หรือ 5 หรือ 6 หรือ 7 เท่านั้น")
    private String productCat;

    @XlsColumn
    @Size(min = 6, max = 6, message = "TMTID ต้องไม่เกิน {max} ตัวอักษร")
    private String tmtId;

    @XlsColumn
    @Size(max = 2, message = "SpecRep ต้องไม่เกิน 2 ตัวอักษร")
    @StartWith(values = {"F", "M", "R"}, message = "SpecRep ตัวอักษรตัวแรก ต้องประกอบด้วย F หรือ M หรือ R เท่านั้น")
    private String specPrep;

    @XlsColumn
    @Size(max = 255, message = "GenericName ต้องไม่เกิน 255 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด GenericName มาด้วยทุกครั้ง")
    private String genericName;
    @XlsColumn
    @Size(max = 255, message = "TradeName ต้องไม่เกิน 255 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด TradeName มาด้วยทุกครั้ง")
    private String tradeName;

    @XlsColumn(columnNames = {"dfsCode", "dsfCode"})
    @Size(max = 100, message = "DFSCode ต้องไม่เกิน 100 ตัวอักษร")
    private String dfsCode;

    @XlsColumn
    @Size(max = 255, message = "DosageForm ต้องไม่เกิน 255 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด DosageForm มาด้วยทุกครั้ง")
    private String dosageForm;

    @XlsColumn
    @Size(max = 255, message = "Strength ต้องไม่เกิน 255 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด Strength มาด้วยทุกครั้ง")
    private String strength;

    @XlsColumn
    @Size(max = 100, message = "Content ต้องไม่เกิน 255 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด Content มาด้วยทุกครั้ง")
    private String content;

    @XlsColumn
    @Size(max = 11, message = "UnitPrice ต้องประกอบด้วยตัวเลขหรือจุดทศนิยม ไม่เกิน 11 ตัวอักษร (99999999.99)")
    @NotEmpty(message = "ต้องกำหนด UnitPrice มาด้วยทุกครั้ง")
    @DoubleValue(message = "UnitPrice ต้องเป็นตัวเลขหรือจุดทศนิยม เท่านั้น")
    private String unitPrice;

    @XlsColumn
    @Size(max = 255, message = "Distributer ต้องไม่เกิน 255 ตัวอักษร")
    private String distributor;

    @XlsColumn(columnNames = {"manufacturer", "manufacture"})
    @Size(max = 255, message = "Manufacturer ต้องไม่เกิน 255 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด Manufacturer มาด้วยทุกครั้ง")
    private String manufacturer;

    @XlsColumn
    @Size(max = 2, message = "ISED ต้องไม่เกิน 2 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด ISED มาด้วยทุกครั้ง")
    @ValueSet(values = {"E", "N", "E*"}, message = "ISED ต้องประกอบด้วย N หรือ E หรือ E* เท่านั้น")
    private String ised;

    @XlsColumn
    @NDC24(message = "NDC24 ต้องประกอบด้วยตัวเลข 24 หลัก เท่านั้น")
    private String ndc24;

    @XlsColumn
    @Size(max = 100, message = "PackSize ต้องไม่เกิน 100 ตัวอักษร")
    private String packSize;

    @XlsColumn
    @Size(max = 11, message = "PackPrice ต้องประกอบด้วยตัวเลขหรือจุดทศนิยม ไม่เกิน 11 ตัวอักษร (99999999.99)")
    @DoubleValue(message = "PackPrice ต้องเป็นตัวเลขหรือจุดทศนิยม เท่านั้น")
    private String packPrice;

    @XlsColumn
    @Size(max = 1, message = "UpdateFlag ต้องไม่เกิน 1 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด UpdateFlag มาด้วยทุกครั้ง")
    @ValueSet(values = {"A", "D", "E", "U"}, message = "UpdateFlag ต้องประกอบด้วย A  หรือ E หรือ D หรือ U เท่านั้น")
    private String updateFlag;

    @XlsColumn
    @NotEmpty(message = "ต้องกำหนด DateChange มาด้วยทุกครั้ง ถ้า UpdateFlag เท่ากับ A หรือ E หรือ D ", groups = {AGroup.class, EDGroup.class})
    @DateAndOptionalTime(message = "รูปแบบวันที่ของ DataChange ไม่ถูกต้อง (dd/mm/yyyy hh:mm)")
    private String dateChange;

    @XlsColumn
    @NotEmpty(message = "ต้องกำหนด DateUpdate มาด้วยทุกครั้ง ถ้า UpdateFlag เท่ากับ A หรือ U", groups = {AGroup.class, UGroup.class})
    @DateAndOptionalTime(message = "รูปแบบวันที่ของ DataUpdate ไม่ถูกต้อง (dd/mm/yyyy hh:mm)")
    private String dateUpdate;

    @XlsColumn
    @NotEmpty(message = "ต้องกำหนด DateEffective มาด้วยทุกครั้ง")
    @DateAndOptionalTime(message = "รูปแบบวันที่ของ DataEffective ไม่ถูกต้อง (dd/mm/yyyy hh:mm)")
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
                    return DateUtils.format(Constants.TMT_DATETIME_FORMAT, calendar.getTime());
                }
            } catch (Exception illegalArgumentException) {
                //Ignored
            }
        }
        return dateToParse;
    }

}
