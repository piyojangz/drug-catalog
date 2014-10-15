/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_DRUG")
public class TmtNDC24 implements Serializable {

    @NotEmpty(message = "ต้องระบุ TMTID")
    @XlsColumn(columnNames = {"TMTID"})
    @Id
    @Column(name = "TMTID", length = 6, nullable = false)
    private String tmtId;
    
    @NotEmpty(message = "ต้องระบุ NDC24")
    @Size(min = 24, max = 24, message = "NDC24 ต้องมีความยาว 24 อักขระ")
    @XlsColumn(columnNames = {"NDC24"})
    @Column(name = "NDC24", nullable = true, length = 24)
    private String ndc24;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NDC24_CREATEDATE")
    private Date createDate;
    @Transient
    private int rowNum;
    @Transient
    private Map<String, List<String>> errorMap;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        createDate = new Date();
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getNdc24() {
        return ndc24;
    }

    public void setNdc24(String ndc24) {
        this.ndc24 = ndc24;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public Map<String, List<String>> getErrorMap() {
        if (errorMap == null) {
            errorMap = new HashMap<>();
        }
        return errorMap;
    }

    public String getErrors() {
        return Joiner.on(",").join(errorMap.values());
    }

    public void addErrors(Set<ConstraintViolation<TmtNDC24>> violations) {
        for (ConstraintViolation<TmtNDC24> violation : violations) {
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
        hash = 43 * hash + Objects.hashCode(this.tmtId);
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
        final TmtNDC24 other = (TmtNDC24) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
