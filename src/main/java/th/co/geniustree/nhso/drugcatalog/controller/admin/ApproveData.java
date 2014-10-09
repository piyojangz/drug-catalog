/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.Set;

/**
 *
 * @author moth
 */
public class ApproveData {

    private String hcode;
    private String hospDrug;
    private String tmt;
    private boolean approve;
    private Set<String> errorColumns;
    private String approveUserPid;

    public ApproveData(String hcode, String hospDrug, String tmt, boolean approve, Set<String> errorColumns, String approveUserPid) {
        this.hcode = hcode;
        this.hospDrug = hospDrug;
        this.tmt = tmt;
        this.approve = approve;
        this.errorColumns = errorColumns;
        this.approveUserPid = approveUserPid;
    }
    

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHospDrug() {
        return hospDrug;
    }

    public void setHospDrug(String hospDrug) {
        this.hospDrug = hospDrug;
    }

    public String getTmt() {
        return tmt;
    }

    public void setTmt(String tmt) {
        this.tmt = tmt;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public Set<String> getErrorColumns() {
        return errorColumns;
    }

    public void setErrorColumns(Set<String> errorColumns) {
        this.errorColumns = errorColumns;
    }

    public String getApproveUserPid() {
        return approveUserPid;
    }

    public void setApproveUserPid(String approveUserPid) {
        this.approveUserPid = approveUserPid;
    }
    
}
