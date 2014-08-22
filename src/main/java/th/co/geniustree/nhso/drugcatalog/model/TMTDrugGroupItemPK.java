/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author moth
 */
public class TMTDrugGroupItemPK implements Serializable{
    private String tmtDrug;
    private String drugGroup;
    private Date datein;

    public TMTDrugGroupItemPK() {
    }

    
    public TMTDrugGroupItemPK(String tmtDrug, String drugGroup, Date datein) {
        this.tmtDrug = tmtDrug;
        this.drugGroup = drugGroup;
        this.datein = datein;
    }
    

    public String getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(String tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public String getDrugGroup() {
        return drugGroup;
    }

    public void setDrugGroup(String drugGroup) {
        this.drugGroup = drugGroup;
    }

    public Date getDatein() {
        return datein;
    }

    public void setDatein(Date datein) {
        this.datein = datein;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.tmtDrug);
        hash = 29 * hash + Objects.hashCode(this.drugGroup);
        hash = 29 * hash + Objects.hashCode(this.datein);
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
        final TMTDrugGroupItemPK other = (TMTDrugGroupItemPK) obj;
        if (!Objects.equals(this.tmtDrug, other.tmtDrug)) {
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
