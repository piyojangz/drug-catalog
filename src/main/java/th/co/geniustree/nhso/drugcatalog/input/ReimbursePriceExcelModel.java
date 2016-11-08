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
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.input.validator.DateAndOptionalTime;
import th.co.geniustree.nhso.drugcatalog.input.validator.DoubleValue;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
public class ReimbursePriceExcelModel implements Serializable{
    
    
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
    
    private Map<String, List<String>> errorMap = new HashMap<>();
    private int rowNum;

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
    
    public void addErrors(Set<ConstraintViolation<ReimbursePriceExcelModel>> violations) {
        for (ConstraintViolation<ReimbursePriceExcelModel> violation : violations) {
            addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    public void addError(String propertyPath, String message) {
        if (!getErrorMap().containsKey(propertyPath)) {
            getErrorMap().put(propertyPath, new ArrayList<String>());
        }
        getErrorMap().get(propertyPath).add(message);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.tmtid);
        hash = 31 * hash + Objects.hashCode(this.effectiveDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReimbursePriceExcelModel other = (ReimbursePriceExcelModel) obj;
        if (!Objects.equals(this.tmtid, other.tmtid)) {
            return false;
        }
        if (!Objects.equals(this.effectiveDate, other.effectiveDate)) {
            return false;
        }
        return true;
    }
    
    
}
