/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.input.ExcelTMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNedPK;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTEdNedRepo;
import th.co.geniustree.nhso.drugcatalog.service.TMTEdNedService;

/**
 *
 * @author moth
 */
@Service
public class TMTEdNedServiceImpl implements TMTEdNedService {

    @Autowired
    private TMTEdNedRepo tmtEdNedRepo;
    @Autowired
    private TMTDrugRepo tMTDrugRepo;

    @Override
    public void save(List<ExcelTMTEdNed> excelTMTEdNeds) {
        for (ExcelTMTEdNed excel : excelTMTEdNeds) {
            TMTEdNed tmtEdNed = new TMTEdNed();
            BeanUtils.copyProperties(excel, tmtEdNed);
            TMTDrug findOne = tMTDrugRepo.findOne(tmtEdNed.getTmtId());
            tmtEdNed.setTmtDrug(findOne);
            findOne.getEdNeds().add(tmtEdNed);
            tmtEdNedRepo.save(tmtEdNed);
        }
    }

    @Override
    public boolean exist(String tmtId, Date dateIn) {
        return tmtEdNedRepo.exists(new TMTEdNedPK(tmtId, dateIn));
    }

    @Override
    public Page<TMTEdNed> findAll(Pageable pageable) {
        return tmtEdNedRepo.findAll(pageable);
    }

    @Override
    public TMTEdNed save(TMTDrug tmtDrug, Fund fund, Date dateIn, String statusEd, Date createDate) {
        TMTEdNed tmtEdNed = new TMTEdNed();
        tmtEdNed.setDateIn(dateIn);
        tmtEdNed.setStatusEd(statusEd);
        tmtEdNed.setCreateDate(new Date());
        tmtEdNed.setTmtId(tmtDrug.getTmtId());
        tmtEdNed.setClassifier(fund.getCode());
        return tmtEdNedRepo.save(tmtEdNed);
    }

    @Override
    public Page<TMTEdNed> findBySpec(Specification<TMTEdNed> spec, Pageable pageable) {
        return tmtEdNedRepo.findAll(spec, pageable);
    }

    @Override
    public void delete(TMTEdNed edNed) {
        tmtEdNedRepo.delete(edNed);
    }

    @Override
    public TMTEdNed edit(TMTEdNed edNed) {
        return tmtEdNedRepo.save(edNed);
    }
    
    

}
