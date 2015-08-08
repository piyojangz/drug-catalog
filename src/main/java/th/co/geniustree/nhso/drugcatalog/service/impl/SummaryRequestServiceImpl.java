/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequest;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequestMapper;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.SummaryRequestService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SummaryRequestServiceImpl implements SummaryRequestService {

    @Autowired
    private RequestItemRepo requestItemRepo;

    private long totalRequest;

    @Override
    public Page<SummaryRequest> loadSummaryRequest(String selectedZone, String selectedProvince, Pageable pageable) {
        List<SummaryRequest> summaryRequests = null;
        Page<Object[]> objPage = null;

        if (selectedProvince.equals(SummaryRequest.ALL_PROVINCE)) {
            if (selectedZone.equals(SummaryRequest.ALL_ZONE)) {
                objPage = requestItemRepo.countSummaryRequestAll(RequestItem.Status.REQUEST, pageable);
                totalRequest = requestItemRepo.countTotalRequestAll(RequestItem.Status.REQUEST);
            } else {
                objPage = requestItemRepo.countSummaryRequestByZone(RequestItem.Status.REQUEST, selectedZone, pageable);
                totalRequest = requestItemRepo.countTotalRequestByZone(RequestItem.Status.REQUEST, selectedZone);
            }
        } else {
            objPage = requestItemRepo.countSummaryRequestByProvince(RequestItem.Status.REQUEST, selectedProvince, pageable);
            totalRequest = requestItemRepo.countTotalRequestByProvince(RequestItem.Status.REQUEST, selectedProvince);
        }
        summaryRequests = SummaryRequestMapper.mapAllToModel(objPage.getContent());
        Page<SummaryRequest> summary = new PageImpl<>(summaryRequests, pageable, objPage.getTotalElements());
        return summary;
    }

    @Override
    public long getTotalReqeust() {
        return totalRequest;
    }

}
