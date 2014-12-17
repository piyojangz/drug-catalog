/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.admin.ApproveData;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;
import th.co.geniustree.nhso.drugcatalog.service.HospitalDrugService;

/**
 *
 * @author moth
 */
@Service
public class ApproveServiceImpl implements ApproveService {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ApproveServiceImpl.class);
    @Autowired
    private RequestItemRepo requestItemRepo;

    @Autowired
    private HospitalDrugService hospitalDrugService;

    @Override
    public void approve(RequestItem requestItem) {
        approve(requestItem, SecurityUtil.getUserDetails().getPid());
    }

    @Override
    public void reject(RequestItem requestItem) {
        reject(requestItem, SecurityUtil.getUserDetails().getPid());
    }

    private void approve(RequestItem requestItem, String pid) {
        HospitalDrug hospitalDrug = hospitalDrugService.addOrUpdateHospitalDrug(requestItem);
        requestItem.setStatus(RequestItem.Status.ACCEPT);
        requestItem.setApproveDate(new Date());
        requestItem.setApproveUser(pid);
        requestItem.setTargetItem(hospitalDrug);
        hospitalDrug.getRequestItems().add(requestItem);
        requestItemRepo.save(requestItem);

    }

    private void reject(RequestItem requestItem, String pid) {
        requestItem.setStatus(RequestItem.Status.REJECT);
        requestItem.setApproveDate(new Date());
        requestItem.setApproveUser(pid);
        requestItemRepo.save(requestItem);
    }

    @Override
    public void approveOrReject(List<RequestItem> items) {
        for (RequestItem item : items) {
            if (RequestItem.Status.ACCEPT == item.getStatus()) {
                approve(item);
            } else {
                reject(item);
            }
        }
    }

    @Override
    public void approveOrReject(String hcode, String hospDrug, String tmt, boolean approve, Set<String> errorColumns, String userPid) {
        List<RequestItem> requestItems = requestItemRepo.findByStatusAndHospDrugCodeAndTmtId(hcode, hospDrug, tmt, DateUtils.parseUSDate("dd/MM/yyyy", "17/09/2014"));
        LOG.info("Approve or reject request {}", requestItems);
        for (RequestItem requestItem : requestItems) {
            if (requestItem != null) {
                if (approve) {
                    approve(requestItem, userPid);
                } else {
                    requestItem.setErrorColumns(errorColumns);
                    reject(requestItem, userPid);
                }
            } else {
                //TODO log to somewhere else.
            }
        }
    }

    @Override
    public void approveOrRejects(List<ApproveData> datas) {
        for (ApproveData data : datas) {
            RequestItem requestItem = requestItemRepo.findOne(data.getUploadItemId());
            if (requestItem != null) {
                requestItem.getUploadDrugItem().setProductCat(data.getProductCat());
                requestItem.getUploadDrugItem().setTradeName(data.getTradeName());
                requestItem.getUploadDrugItem().setGenericName(data.getGenericName());
                requestItem.getUploadDrugItem().setStrength(data.getStrength());
                requestItem.getUploadDrugItem().setDosageForm(data.getDosageForm());
                requestItem.getUploadDrugItem().setContent(data.getContent());
                requestItem.getUploadDrugItem().setManufacturer(data.getManufacturer());
                if (data.isApprove()) {
                    approve(requestItem);
                } else {
                    requestItem.setErrorColumns(data.getErrorColumns());
                    reject(requestItem);
                }
            }
        }
    }

}
