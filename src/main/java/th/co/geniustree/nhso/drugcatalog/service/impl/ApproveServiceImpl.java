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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;

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
    private HospitalDrugRepo hospitalTMTDrugRepo;

    @Override
    public void approve(RequestItem requestItem) {
        approve(requestItem, SecurityUtil.getUserDetails().getPid());
    }

    @Override
    public void reject(RequestItem requestItem) {
        reject(requestItem, SecurityUtil.getUserDetails().getPid());
    }

    private void approve(RequestItem requestItem, String pid) {
        requestItem.setStatus(RequestItem.Status.ACCEPT);
        requestItem.setApproveDate(new Date());
        requestItem.setApproveUser(pid);
        HospitalDrug targetItem = requestItem.getTargetItem();
        targetItem.setApproved(Boolean.TRUE);
        hospitalTMTDrugRepo.save(targetItem);
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
        RequestItem requestItem = requestItemRepo.findByTargetItemHcodeAndTargetItemHospDrugCodeAndTargetItemTmtId(hcode, hospDrug, tmt);
        LOG.info("Approve or reject request {}", requestItem);
        if (requestItem != null) {
            if (approve) {
                approve(requestItem, userPid);
            } else {
                reject(requestItem, userPid);
            }
        }else{
            //TODO log to somewhere else.
        }
    }

}
