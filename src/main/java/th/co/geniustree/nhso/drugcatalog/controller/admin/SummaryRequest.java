/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.Date;

/**
 *
 * @author Thanthathon
 */
public class SummaryRequest {
    
    public static final String ALL_ZONE = "SELECT_ALL_ZONE";
    public static final String ALL_PROVINCE = "SELECT_ALL_PROVINCE";
    
    private Date requestDate;
    private String hcode;
    private String hname;
    private Integer countTMTNotNull;
    private Integer countTMTNull;
    private Integer countFlagA;
    private Integer countFlagE;
    private Integer countFlagU;
    private Integer countFlagD;
    private Integer countAll;

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public Integer getCountTMTNotNull() {
        return countTMTNotNull;
    }

    public void setCountTMTNotNull(Integer countTMTNotNull) {
        this.countTMTNotNull = countTMTNotNull;
    }

    public Integer getCountTMTNull() {
        return countTMTNull;
    }

    public void setCountTMTNull(Integer countTMTNull) {
        this.countTMTNull = countTMTNull;
    }

    public Integer getCountFlagA() {
        return countFlagA;
    }

    public void setCountFlagA(Integer countFlagA) {
        this.countFlagA = countFlagA;
    }

    public Integer getCountFlagE() {
        return countFlagE;
    }

    public void setCountFlagE(Integer countFlagE) {
        this.countFlagE = countFlagE;
    }

    public Integer getCountFlagU() {
        return countFlagU;
    }

    public void setCountFlagU(Integer countFlagU) {
        this.countFlagU = countFlagU;
    }

    public Integer getCountFlagD() {
        return countFlagD;
    }

    public void setCountFlagD(Integer countFlagD) {
        this.countFlagD = countFlagD;
    }

    public Integer getCountAll() {
        return countAll;
    }

    public void setCountAll(Integer countAll) {
        this.countAll = countAll;
    }
    
}
