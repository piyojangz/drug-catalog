/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.Set;
import org.apache.poi.ss.usermodel.Row;

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
    private String tradeName;
    private String genericName;
    private String strength;
    private String dosageForm;
    private String content;
    private String manufacturer;

    public ApproveData(String hcode, String hospDrug, String tmt, boolean approve, Set<String> errorColumns, String approveUserPid, Integer uploadItemId) {
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

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    

    @Override
    public String toString() {
        return "ApproveData{" + "hcode=" + hcode + ", hospDrug=" + hospDrug + ", tmt=" + tmt + ", approve=" + approve + ", errorColumns=" + errorColumns + ", approveUserPid=" + approveUserPid + ", uploadItemId=" + uploadItemId + '}';
    }

}
