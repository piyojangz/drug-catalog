/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.AutoApproveService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class AutoApprove implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(AutoApprove.class);

    @Autowired
    private AutoApproveService autoApproveService;
    private String hcode;
    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    private String flagApprove;

    public String getFlagApprove() {
        return flagApprove;
    }

    public void setFlagApprove(String flagApprove) {
        this.flagApprove = flagApprove;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public void approveByHcode() {
        autoApproveService.approveByHcode(hcode);
    }

    public void approveRejectAndEditCountGreaterThanZero() {
        autoApproveService.approveRejectAndEditCountGreaterThanZero();
    }

    public void approveRequestWhichTMTisNull() {
        autoApproveService.approveRequestWhichTMTisNull();
    }

    public void approveRequestWhichCreateOneline() {
        autoApproveService.approveRequestWhichCreateOneline();
    }

    public void reapproveAll() {
        int page = 0;
        while (autoApproveService.approvePartial(page, 10000)) {
            page++;
        }
    }

    public void copyDataToHospDrugtran() {
        uploadHospitalDrugItemRepo.copyDataProcedure();
    }

    public void approveAllRequestWithFlag() {
        try {
            autoApproveService.approveByRequestFlag(flagApprove, SecurityUtil.getUserDetails().getPid());
            FacesMessageUtils.info("อนุมัติทุกรายการยา Flag " + flagApprove + " เรียบร้อย");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถอนุมัติรายการยาได้");
            LOG.error("Cannot approve Flag " + flagApprove + " : ", e);
        }
    }
}
