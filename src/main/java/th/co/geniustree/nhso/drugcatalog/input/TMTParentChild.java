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
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
public class TMTParentChild implements Serializable {

    @XlsColumn(columnNames = "TMTID_PARENT")
    @NotEmpty
    private String parentTmtId;

    @XlsColumn(columnNames = "TMTID_CHILD")
    @NotEmpty
    private String childTmtId;

    private Map<String, List<String>> errorMap;
    private int rowNum;

    public String getParentTmtId() {
        return parentTmtId;
    }

    public void setParentTmtId(String parentTmtId) {
        this.parentTmtId = parentTmtId;
    }

    public String getChildTmtId() {
        return childTmtId;
    }

    public void setChildTmtId(String childTmtId) {
        this.childTmtId = childTmtId;
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

    public void addErrors(Set<ConstraintViolation<TMTParentChild>> violations) {
        for (ConstraintViolation<TMTParentChild> violation : violations) {
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
        hash = 97 * hash + Objects.hashCode(this.parentTmtId);
        hash = 97 * hash + Objects.hashCode(this.childTmtId);
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
        final TMTParentChild other = (TMTParentChild) obj;
        if (!Objects.equals(this.parentTmtId, other.parentTmtId)) {
            return false;
        }
        if (!Objects.equals(this.childTmtId, other.childTmtId)) {
            return false;
        }
        return true;
    }
    
}
