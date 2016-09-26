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
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.input.validator.DateAndOptionalTime;
import th.co.geniustree.nhso.drugcatalog.input.validator.DoubleValue;
import th.co.geniustree.nhso.drugcatalog.input.validator.StartWith;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
public class ReimbursePriceTPExcelModel implements Serializable{
    
    @XlsColumn(columnNames = "HCODE")
    @NotEmpty(message = "ต้องระบุรหัสหน่วยบริการ")
    private String hcode;
    
    @XlsColumn(columnNames = "HOSPDRUGCODE")
    @NotEmpty(message = "ต้องระบุรหัสยา")
    private String hospDrugCode;
    
    @XlsColumn(columnNames = "TMTID")
    @NotEmpty(message = "ต้องระบุ TMTID มาด้วย")
    private String tmtid;
    
    @XlsColumn(columnNames = "EFFECTIVE_DATE")
    @NotEmpty(message = "ต้องระบุ EFFECTIVE_DATE มาด้วย")
    @DateAndOptionalTime(message = "รูปแบบวันที่ของ EFFECTIVE_DATE ไม่ถูกต้อง (dd/mm/yyyy)")
    private String effectiveDate;
    
    @XlsColumn(columnNames = "PRICE")
    @NotEmpty(message = "ต้องระบุ PRICE มาด้วย")
    @DoubleValue(message = "Reimburse Price ต้องเป็นตัวเลขหรือจุดทศนิยม เท่านั้น", removeSeperator = true)
    private String price;
    
    @XlsColumn(columnNames = "CONTENT")
    @Size(max = 50, message = "Content ต้องไม่เกิน {max} ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด Content มาด้วยทุกครั้ง")
    private String content;
    
    @XlsColumn(columnNames = "SPECPREP")
    @Size(max = 3, message = "Specprep ต้องไม่เกิน {max} ตัวอักษร")
    @StartWith(values = {"F", "M", "R"}, message = "Specprep ตัวอักษรตัวแรก ต้องประกอบด้วย F หรือ M หรือ R เท่านั้น")
    private String specprep;
    
    private Map<String, List<String>> errorMap = new HashMap<>();
    private int rowNum;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpecprep() {
        return specprep;
    }

    public void setSpecprep(String specprep) {
        this.specprep = specprep;
    }

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Map<String, List<String>> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, List<String>> errorMap) {
        this.errorMap = errorMap;
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
    
    public void addErrors(Set<ConstraintViolation<DrugAndDosageFormGroup>> violations) {
        for (ConstraintViolation<DrugAndDosageFormGroup> violation : violations) {
            addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    public void addError(String propertyPath, String message) {
        if (!getErrorMap().containsKey(propertyPath)) {
            getErrorMap().put(propertyPath, new ArrayList<String>());
        }
        getErrorMap().get(propertyPath).add(message);
    }
}
