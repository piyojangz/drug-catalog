/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

import th.co.geniustree.nhso.drugcatalog.model.TMT;
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
public class TradeDrugExcelModel implements Serializable, Typeable, TMT {

    @XlsColumn(columnNames = {"TMTID","TMTID(TPU)", "TMTID(TP)","TMTID (TPU)", "TMTID (TP)"})
    private String tmtId;
    @XlsColumn(columnNames = "changeDate")
    private String changeDateString;

    private Date changeDate;

    @XlsColumn
    private String fsn;
    @XlsColumn
    private String manufacturer;

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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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
        final TradeDrugExcelModel other = (TradeDrugExcelModel) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
