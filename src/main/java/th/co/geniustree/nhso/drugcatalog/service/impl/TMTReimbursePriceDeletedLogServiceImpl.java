/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.log.TMTReimbursePriceDeleted;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReimbursePriceDeletedRepo;
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;

/**
 *
 * @author thanthathon.b
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TMTReimbursePriceDeletedLogServiceImpl implements DeletedLogService<ReimbursePrice> {

    private static final Logger LOG = LoggerFactory.getLogger(TMTDrugGroupItemDeletedLogServiceImpl.class);

    @Autowired
    private TMTReimbursePriceDeletedRepo repo;

    @Override
    public boolean createLog(ReimbursePrice item) {
        try {
            TMTReimbursePriceDeleted deleted = new TMTReimbursePriceDeleted();
            deleted.setEffectiveDate(item.getId().getEffectiveDate());
            deleted.setTmtId(item.getTmtDrug().getTmtId());
            deleted.setPrice(item.getPrice());
            repo.save(deleted);
            LOG.info("Delete ReimbursePrice [TMT : {}, Price : {}, DateEffective : {}]",
                    item.getTmtDrug().getTmtId(),
                    item.getPrice(),
                    DateUtils.format("dd/MMM/yyyy", item.getId().getEffectiveDate()));
            return true;
        } catch (Exception e) {
            LOG.error("Can't save new LOG", e);
            return false;
        }
    }

}
