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
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.log.TMTDrugGroupItemDeleted;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugGroupItemDeletedRepo;
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;

/**
 *
 * @author thanthathon.b
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TMTDrugGroupItemDeletedLogServiceImpl implements DeletedLogService<TMTDrugGroupItem> {

    private static final Logger LOG = LoggerFactory.getLogger(TMTDrugGroupItemDeletedLogServiceImpl.class);
    
    @Autowired
    private TMTDrugGroupItemDeletedRepo repo;

    @Override
    public boolean createLog(TMTDrugGroupItem item) {
        try {
            TMTDrugGroupItemDeleted deleted = new TMTDrugGroupItemDeleted();
            deleted.setCreateDate(item.getCreateDate());
            deleted.setDateIn(item.getDatein());
            deleted.setDateOut(item.getDateOut());
            deleted.setTmtId(item.getTmtDrug().getTmtId());
            deleted.setDrugGroup(item.getDrugGroup().getId());
            repo.save(deleted);
            return true;
        } catch (Exception e) {
            LOG.error("Can't save new LOG", e);
            return false;
        }
    }

}
