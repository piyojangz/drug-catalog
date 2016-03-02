/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelationID;
import th.co.geniustree.nhso.drugcatalog.repo.TMTRelationRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTRelationSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTRelationService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TMTRelationServiceImpl implements TMTRelationService {

    @Autowired
    private TMTRelationRepo tmtRelationRepo;

    @Override
    public Page<TMTRelation> findAll(Pageable pageable) {
        return tmtRelationRepo.findAll(pageable);
    }

    @Override
    public Page<TMTRelation> search(String keyword, Pageable pageable) {
        List<String> keywords = Arrays.asList(keyword.split("\\s+"));
        Specification<TMTRelation> spec = Specifications.where(TMTRelationSpecs.fsnContains(keywords)).or(TMTRelationSpecs.tmtIdContains(keywords));
        return tmtRelationRepo.findAll(spec, pageable);
    }

    @Override
    public void save(TMTRelation tmt) {
        tmtRelationRepo.save(tmt);
    }

    @Override
    public void saveAll(Collection<TMTRelation> tmtList) {
        tmtRelationRepo.save(tmtList);
    }

    @Override
    public void delete(TMTRelation tmt) {
        tmtRelationRepo.delete(tmt);
    }

    @Override
    public Page<TMTRelation> findBySpec(Specification<TMTRelation> spec, Pageable pageable) {
        return tmtRelationRepo.findAll(spec, pageable);
    }

    @Override
    public void deleteAllRelationByParent(TMTRelation relation) {
        List<TMTRelation> relations = tmtRelationRepo.findByParentTmtId(relation.getId().getParentId());
        for (TMTRelation r : relations) {
            tmtRelationRepo.delete(r);
        }
    }

    @Override
    public boolean isRelationExist(String parentTmtId, String childTmtId) {
        TMTRelationID relationId = new TMTRelationID();
        relationId.setParentId(parentTmtId);
        relationId.setChildId(childTmtId);
        return tmtRelationRepo.findOne(relationId) != null;
    }

    @Override
    public void deleteAllChildren(TMTDrug tmtDrug) {
        List<TMTRelation> tmtList = tmtRelationRepo.findByParentTmtId(tmtDrug.getTmtId());
        tmtRelationRepo.delete(tmtList);
    }

}
