/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.input.validator.DateAndOptionalTime;
import th.co.geniustree.nhso.drugcatalog.input.validator.ValueSet;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
public class ExcelTMTEdNed implements Serializable {

    @XlsColumn
    @Size(min = 6, max = 6, message = "TMTID ต้องไม่เกิน {max} ตัวอักษร")
    @NotEmpty(message = "ต้องระบุ TMTID มาด้วยทุกครั้ง")
    private String tmtId;

    @XlsColumn(columnNames = "DATEIN")
    @NotEmpty(message = "ต้องระบุ DATEIN มาด้วย")
    @DateAndOptionalTime(message = "รูปแบบวันที่ของ DATEIN ไม่ถูกต้อง (dd/mm/yyyy)")
    private String dateinString;

    @XlsColumn(columnNames = "ISED")
    @Size(max = 2, message = "ISED ต้องไม่เกิน 2 ตัวอักษร")
    @NotEmpty(message = "ต้องกำหนด ISED มาด้วยทุกครั้ง")
    @ValueSet(values = {"E", "N", "E*"}, message = "ISED ต้องประกอบด้วย N หรือ E หรือ E* เท่านั้น")
    private String statusEd;
    
    private Map<String, List<String>> errorMap;
    private Date dateIn;
    private int rowNum;

    public Map<String, List<String>> getErrorMap() {
        if (errorMap == null) {
            errorMap = new HashMap<>();
        }
        return errorMap;
    }

    public void setErrorMap(Map<String, List<String>> errorMap) {
        this.errorMap = errorMap;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getDateinString() {
        return dateinString;
    }

    public void setDateinString(String dateinString) {
        this.dateinString = dateinString;
    }

    public String getStatusEd() {
        return statusEd;
    }

    public void setStatusEd(String statusEd) {
        this.statusEd = statusEd;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
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

    public void addErrors(Set<ConstraintViolation<ExcelTMTEdNed>> violations) {
        for (ConstraintViolation<ExcelTMTEdNed> violation : violations) {
            addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    public void addError(String propertyPath, String message) {
        if (!getErrorMap().containsKey(propertyPath)) {
            getErrorMap().put(propertyPath, new ArrayList<String>());
        }
        getErrorMap().get(propertyPath).add(message);
    }

    //if xml bean call setter then move it to setter method instead.
    public void postConstruct() {
        if (dateinString != null) {
            dateIn = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(dateinString);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final ExcelTMTEdNed other = (ExcelTMTEdNed) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        if (!Objects.equals(this.dateinString, other.dateinString)) {
            return false;
        }
        return true;
    }

}
