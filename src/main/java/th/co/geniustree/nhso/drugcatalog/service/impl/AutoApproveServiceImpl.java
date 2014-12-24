/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;
import th.co.geniustree.nhso.drugcatalog.service.AutoApproveService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AutoApproveServiceImpl implements AutoApproveService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoApproveServiceImpl.class);
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private ApproveService approveService;

    @Override
    public void approveRejectAndEditCountGreaterThanZero() {
        List<RequestItem> items = requestItemRepo.findByStatusAndEditCountGreaterThan(RequestItem.Status.REQUEST, 0);
        List<RequestItem> itemsThatExports = new ArrayList<>();
        for (RequestItem item : items) {
            long countTmtApproveFile = requestItemRepo.countTmtApproveFile(item.getId());
            if (countTmtApproveFile > 0) {
                itemsThatExports.add(item);
            }
            log.info("auto approve for request and editCount>0 ==>{}", itemsThatExports.size());
            approveService.approve(itemsThatExports);
        }
    }

}
