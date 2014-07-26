/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
public class GenericDrugExcelModel implements Serializable, Typeable, TMT {

    @XlsColumn(columnNames = {"TMTID(GPU)", "TMTID(GP)", "TMTID(SUBS)", "TMTID(VTM)"})
    private String tmtId;
    @XlsColumn(columnNames = "changeDate")
    private String changeDateString;
    private Date changeDate;

    @XlsColumn
    private String fsn;

    private TMTDrug.Type type;

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getChangeDateString() {
        return changeDateString;
    }

    public void setChangeDateString(String changeDateString) {
        this.changeDateString = changeDateString;
        this.changeDate = DateUtils.parseUSDate("yyyyMMdd", changeDateString);
    }

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    public TMTDrug.Type getType() {
        return type;
    }

    public void setType(TMTDrug.Type type) {
        this.type = type;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.tmtId);
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
        final GenericDrugExcelModel other = (GenericDrugExcelModel) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
