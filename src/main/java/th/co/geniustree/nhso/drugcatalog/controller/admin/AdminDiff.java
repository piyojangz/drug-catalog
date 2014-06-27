/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.request.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.tmt.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class AdminDiff implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AdminDiff.class);
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private ApproveService approveService;
    private RequestItem requestItem;
    private TMTDrug masterDrug;

    private String tmtCode;
    private Integer requestId;
    private boolean back = false;

    public RequestItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
    }

    public TMTDrug getMasterDrug() {
        return masterDrug;
    }

    public void setMasterDrug(TMTDrug masterDrug) {
        this.masterDrug = masterDrug;
    }

    public String getTmtCode() {
        return tmtCode;
    }

    public void setTmtCode(String tmtCode) {
        this.tmtCode = tmtCode;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public String accept() {
        approveService.approve(requestItem);
        FacesMessageUtils.info("Approve completed.");
        back = true;
        return null;
    }

    public String reject() {
        approveService.reject(requestItem);
        FacesMessageUtils.info("Reject completed.");
        back = true;
        return null;
    }

    public void loadData() {
        LOG.debug("param tmtCode={},requestId={}", tmtCode, requestId);
        requestItem = requestItemRepo.findOne(requestId);
        masterDrug = tmtDrugRepo.findOne(tmtCode);
    }
}
