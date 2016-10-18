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
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.input.validator.DateAndOptionalTime;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
public class DrugAndGroup implements Serializable {

    @XlsColumn
    @NotEmpty(message = "ต้องระบุ TMTID มาด้วย")
    private String tmtId;
    @XlsColumn
    @NotEmpty(message = "ต้องระบุ DRUGGROUP มาด้วย")
    private String drugGroup;
    @XlsColumn
    @NotEmpty(message = "ต้องระบุ DATEIN มาด้วย")
    @DateAndOptionalTime(message = "รูปแบบวันที่ของ DATEIN ไม่ถูกต้อง (dd/mm/yyyy)")
    private String datein;
    @XlsColumn
    @DateAndOptionalTime(message = "รูปแบบวันที่ของ DATEOUT ไม่ถูกต้อง (dd/mm/yyyy)")
    private String dateout;
    private Map<String, List<String>> errorMap;
    private int rowNum;
    private Date dateInDate;
    private Date dateOutDate;

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getDrugGroup() {
        return drugGroup;
    }

    public void setDrugGroup(String drugGroup) {
        this.drugGroup = drugGroup;
    }

    public String getDatein() {
        return datein;
    }

    public void setDatein(String datein) {
        this.datein = datein;
    }

    public String getDateout() {
        return dateout;
    }

    public void setDateout(String dateout) {
        this.dateout = dateout;
    }

    public Map<String, List<String>> getErrorMap() {
        if (errorMap == null) {
            errorMap = new HashMap<>();
        }
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

    public Date getDateInDate() {
        return dateInDate;
    }

    public void setDateInDate(Date dateInDate) {
        this.dateInDate = dateInDate;
    }

    public Date getDateOutDate() {
        return dateOutDate;
    }

    public void setDateOutDate(Date dateOutDate) {
        this.dateOutDate = dateOutDate;
    }

    public void addErrors(Set<ConstraintViolation<DrugAndGroup>> violations) {
        for (ConstraintViolation<DrugAndGroup> violation : violations) {
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
        if (datein != null) {
            dateInDate = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(datein);
        }
        if (dateout != null) {
            dateOutDate = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(dateout);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final DrugAndGroup other = (DrugAndGroup) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        if (!Objects.equals(this.drugGroup, other.drugGroup)) {
            return false;
        }
        if (!Objects.equals(this.datein, other.datein)) {
            return false;
        }
        return true;
    }

}
