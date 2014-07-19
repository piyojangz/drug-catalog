/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalTMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;

/**
 *
 * @author moth
 */
@Service
public class ApproveServiceImpl implements ApproveService{
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private HospitalTMTDrugRepo hospitalTMTDrugRepo;
    @Override
    public void approve(RequestItem requestItem) {
        requestItem.setStatus(RequestItem.Status.ACCEPT);
        requestItem.setApproveDate(new Date());
        requestItem.setApproveUser(SecurityUtil.getUserDetails().getStaffName());
        HospitalDrug targetItem = requestItem.getTargetItem();
        targetItem.setApproved(Boolean.TRUE);
        hospitalTMTDrugRepo.save(targetItem);
        requestItemRepo.save(requestItem);
    }

    @Override
    public void reject(RequestItem requestItem) {
        requestItem.setStatus(RequestItem.Status.REJECT);
        requestItem.setApproveDate(new Date());
        requestItem.setApproveUser(SecurityUtil.getUserDetails().getStaffName());
        requestItemRepo.save(requestItem);
    }
    
}
