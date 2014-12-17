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
    private Integer uploadItemId;
    private String productCat;

    public ApproveData(String hcode, String hospDrug, String tmt, boolean approve, Set<String> errorColumns, String approveUserPid,Integer uploadItemId) {
        this.hcode = hcode;
        this.hospDrug = hospDrug;
        this.tmt = tmt;
        this.approve = approve;
        this.errorColumns = errorColumns;
        this.approveUserPid = approveUserPid;
        this.uploadItemId = uploadItemId;
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

    public Integer getUploadItemId() {
        return uploadItemId;
    }

    public void setUploadItemId(Integer uploadItemId) {
        this.uploadItemId = uploadItemId;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }
    

    @Override
    public String toString() {
        return "ApproveData{" + "hcode=" + hcode + ", hospDrug=" + hospDrug + ", tmt=" + tmt + ", approve=" + approve + ", errorColumns=" + errorColumns + ", approveUserPid=" + approveUserPid + ", uploadItemId=" + uploadItemId + '}';
    }
    
    
}
