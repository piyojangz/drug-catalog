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
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.model.log.TMTReimbursePriceTPDeleted;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReimbursePriceTPDeletedRepo;
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;

/**
 *
 * @author thanthathon.b
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TMTReimbursePriceTPDeletedLogServiceImpl implements DeletedLogService<ReimbursePriceTP> {

    private static final Logger LOG = LoggerFactory.getLogger(TMTReimbursePriceTPDeletedLogServiceImpl.class);
    
    @Autowired
    private TMTReimbursePriceTPDeletedRepo repo;
    
    @Override
    public boolean createLog(ReimbursePriceTP item) {
        try {
            TMTReimbursePriceTPDeleted deleted = new TMTReimbursePriceTPDeleted();
            deleted.setDateEffective(item.getId().getDateEffective());
            deleted.setTmtId(item.getId().getTmtId());
            deleted.setHcode(item.getId().getHcode());
            deleted.setHospDrugCode(item.getId().getHospDrugCode());
            deleted.setPrice(item.getPrice());
            deleted.setContent(item.getContent());
            deleted.setSpecprep(item.getSpecprep());
            repo.save(deleted);
            LOG.info("Delete ReimbursePriceTP [TMT : {}, HCODE : {}, HOSPDRUGCODE : {}, DateEffective : {},CONTENT_CR : {}, SPECPREP : {}, PRICE : {}]",
                    item.getId().getTmtId(),
                    item.getId().getHcode(),
                    item.getId().getHospDrugCode(),
                    DateUtils.format("dd/MMM/yyyy", item.getId().getDateEffective()),
                    item.getContent(),
                    item.getSpecprep(),
                    item.getPrice());
            return true;
        } catch (Exception e) {
            LOG.error("Can't save new LOG", e);
            return false;
        }
    }
    
}
