/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import java.io.Serializable;
import java.util.ArrayList;
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
public class DosageFormExcelModel implements Serializable {

    @XlsColumn(columnNames = "DOSAGEFORM_GROUP_ID")
    @NotEmpty(message = "ต้องระบุ Dosage Form Group มาด้วย")
    private String id;

    @XlsColumn(columnNames = "DOSAGEFORM_GROUP_DESCRIPTION")
    private String description;

    private Map<String, List<String>> errorMap;
    private int rowNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void addErrors(Set<ConstraintViolation<DosageFormExcelModel>> violations) {
        for (ConstraintViolation<DosageFormExcelModel> violation : violations) {
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
