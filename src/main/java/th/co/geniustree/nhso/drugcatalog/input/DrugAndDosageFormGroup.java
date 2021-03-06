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
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
public class DrugAndDosageFormGroup implements Serializable{
    
    @XlsColumn(columnNames = "TMTID")
    @NotEmpty(message = "ต้องระบุ TMTID มาด้วย")
    private String tmtid;
    
    @XlsColumn(columnNames = "DOSAGE_FORM_GROUP")
    @NotEmpty(message = "ต้องระบุ Dosage Form Group ID มาด้วย")
    private String dosageFormGroup;
    
    private Map<String, List<String>> errorMap = new HashMap<>();
    private int rowNum;

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public String getDosageFormGroup() {
        return dosageFormGroup;
    }

    public void setDosageFormGroup(String dosageFormGroup) {
        this.dosageFormGroup = dosageFormGroup;
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
