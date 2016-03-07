/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import java.io.Serializable;
import java.util.Objects;
import org.hibernate.validator.constraints.NotEmpty;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author Thanthathon
 */
public class TMTGpuToTpu implements Serializable, UploadRelationshipModel {

    @XlsColumn(columnNames = "GPUID")
    @NotEmpty
    private String parentTmtId;

    @XlsColumn(columnNames = "TPUID")
    @NotEmpty
    private String childTmtId;

    @Override
    public String getParentTmtId() {
        return parentTmtId;
    }

    @Override
    public void setParentTmtId(String parentTmtId) {
        this.parentTmtId = parentTmtId;
    }

    @Override
    public String getChildTmtId() {
        return childTmtId;
    }

    @Override
    public void setChildTmtId(String childTmtId) {
        this.childTmtId = childTmtId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.parentTmtId);
        hash = 47 * hash + Objects.hashCode(this.childTmtId);
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
        final TMTGpuToTpu other = (TMTGpuToTpu) obj;
        if (!Objects.equals(this.parentTmtId, other.parentTmtId)) {
            return false;
        }
        if (!Objects.equals(this.childTmtId, other.childTmtId)) {
            return false;
        }
        return true;
    }

}
