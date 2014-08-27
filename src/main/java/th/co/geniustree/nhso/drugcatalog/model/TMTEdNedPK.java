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
 * @author pramoth
 */
public class TMTEdNedPK implements Serializable {

    public static final String SUPPORT_CASSIFIER = "UC";
    private String tmtId;
    private Date dateIn;
    private String classifier = SUPPORT_CASSIFIER;//support only UC in current version.

    public TMTEdNedPK() {
    }

    public TMTEdNedPK(String tmtId, Date dateIn) {
        this.tmtId = tmtId;
        this.dateIn = dateIn;
        this.classifier = SUPPORT_CASSIFIER;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public String getClassifier() {
        return classifier;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.tmtId);
        hash = 67 * hash + Objects.hashCode(this.dateIn);
        hash = 67 * hash + Objects.hashCode(this.classifier);
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
        final TMTEdNedPK other = (TMTEdNedPK) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        if (!Objects.equals(this.dateIn, other.dateIn)) {
            return false;
        }
        if (!Objects.equals(this.classifier, other.classifier)) {
            return false;
        }
        return true;
    }

    
}
