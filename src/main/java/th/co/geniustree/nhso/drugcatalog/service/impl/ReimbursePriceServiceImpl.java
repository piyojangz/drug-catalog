/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePricePK;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTPID;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.ReimbursePriceRepo;
import th.co.geniustree.nhso.drugcatalog.repo.ReimbursePriceTPRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimbursePriceSpecs;
import th.co.geniustree.nhso.drugcatalog.repo.spec.ReimbursePriceTPSpecs;
import th.co.geniustree.nhso.drugcatalog.service.ReimbursePriceService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReimbursePriceServiceImpl implements ReimbursePriceService {

    @Autowired
    private ReimbursePriceRepo reimbursePriceRepo;

    @Autowired
    private ReimbursePriceTPRepo reimbursePriceTPRepo;

    @Override
    public ReimbursePrice save(TMTDrug tmtDrug, BigDecimal price, Date dateEffective) {
        ReimbursePricePK pk = new ReimbursePricePK(tmtDrug.getTmtId(), dateEffective);
        ReimbursePrice reimbursePrice = new ReimbursePrice(pk);
        reimbursePrice.setPrice(price);
        return reimbursePriceRepo.save(reimbursePrice);
    }

    @Override
    public ReimbursePriceTP save(String hcode, String hospDrugCode, TMTDrug tmtDrug, String content, String specprep, BigDecimal price, Date dateEffective) {
        ReimbursePriceTPID id = new ReimbursePriceTPID(tmtDrug.getTmtId(), hospDrugCode, hcode, dateEffective);
        ReimbursePriceTP reimbursePrice = new ReimbursePriceTP(id);
        reimbursePrice.setContent(content);
        reimbursePrice.setSpecprep(specprep);
        reimbursePrice.setTmtDrug(tmtDrug);
        reimbursePrice.setPrice(price);
        return reimbursePriceTPRepo.save(reimbursePrice);
    }

    @Override
    public ReimbursePriceTP save(HospitalDrug hospitalDrug, BigDecimal price, Date dateEffective) {
        return save(hospitalDrug.getHcode(),
                hospitalDrug.getHospDrugCode(),
                hospitalDrug.getTmtDrug(),
                hospitalDrug.getContent(),
                hospitalDrug.getSpecPrep(),
                price,
                dateEffective);
    }

    @Override
    public ReimbursePrice edit(ReimbursePrice tmt) {
        return reimbursePriceRepo.save(tmt);
    }

    @Override
    public ReimbursePriceTP edit(ReimbursePriceTP tmt) {
        return reimbursePriceTPRepo.save(tmt);
    }

    @Override
    public void delete(ReimbursePrice tmt) {
        reimbursePriceRepo.delete(tmt);
    }

    @Override
    public void delete(ReimbursePriceTP tmt) {
        reimbursePriceTPRepo.delete(tmt);
    }

    @Override
    public Page<ReimbursePrice> findAll(Pageable pageable) {
        return reimbursePriceRepo.findAll(pageable);
    }

    @Override
    public Page<ReimbursePriceTP> findAllTP(Pageable pageable) {
        return reimbursePriceTPRepo.findAll(pageable);
    }

    @Override
    public Page<ReimbursePrice> search(String keyword, Pageable pageable) {
        List<String> keyList = Arrays.asList(keyword.split("\\s+"));
        Specification<ReimbursePrice> spec = Specifications.where(ReimbursePriceSpecs.tmtLike(keyList))
                .or(ReimbursePriceSpecs.fsnLike(keyList));
        return reimbursePriceRepo.findAll(spec, pageable);
    }

    @Override
    public Page<ReimbursePriceTP> searchTP(String keyword, Pageable pageable) {
        List<String> keyList = Arrays.asList(keyword.split("\\s+"));
        Specification<ReimbursePriceTP> spec = Specifications
                .where(ReimbursePriceTPSpecs.tmtLike(keyList))
                .or(ReimbursePriceTPSpecs.fsnLike(keyList))
                .or(ReimbursePriceTPSpecs.hcodeLike(keyList))
                .or(ReimbursePriceTPSpecs.hospDrugCodeLike(keyList))
                .or(ReimbursePriceTPSpecs.hnameLike(keyList));
        return reimbursePriceTPRepo.findAll(spec, pageable);
    }

    @Override
    public void save(List<ReimbursePrice> reimbursePrices) {
        reimbursePriceRepo.save(reimbursePrices);
    }

    @Override
    public void saveTP(List<ReimbursePriceTP> reimbursePrices) {
        reimbursePriceTPRepo.save(reimbursePrices);
    }

    @Override
    public boolean isExists(String tmtId, Date dateEffective) {
        return reimbursePriceRepo.exists(new ReimbursePricePK(tmtId, dateEffective));
    }

    @Override
    public boolean isExists(String hcode, String hospDrugCode, String tmtId, Date dateEffective) {
        return reimbursePriceTPRepo.exists(new ReimbursePriceTPID(tmtId, hospDrugCode, hcode, dateEffective));
    }

}
