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
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.log.TMTEdNedDeleted;
import th.co.geniustree.nhso.drugcatalog.repo.TMTEdNedDeletedRepo;
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;

/**
 *
 * @author thanthathon.b
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TMTEdNedDeletedLogServiceImpl implements DeletedLogService<TMTEdNed> {

    private static final Logger LOG = LoggerFactory.getLogger(TMTEdNedDeletedLogServiceImpl.class);

    @Autowired
    private TMTEdNedDeletedRepo repo;

    @Override
    public boolean createLog(TMTEdNed item) {
        try {
            TMTEdNedDeleted deleted = new TMTEdNedDeleted();
            deleted.setCreateDate(item.getCreateDate());
            deleted.setDateIn(item.getDateIn());
            deleted.setTmtId(item.getTmtDrug().getTmtId());
            deleted.setClassifier(item.getClassifier());
            deleted.setStatusEd(item.getStatusEd());
            repo.save(deleted);
            LOG.info("Delete TMT EDNED [TMT:{} , ED:{} , DATEIN:{} , CLASSIFIER:{}]",
                    item.getTmtDrug().getTmtId(),
                    item.getStatusEd(),
                    DateUtils.format("dd/MMM/yyyy", item.getDateIn()),
                    item.getClassifier());
            return true;
        } catch (Exception e) {
            LOG.error("Can't save new LOG", e);
            return false;
        }
    }

}
