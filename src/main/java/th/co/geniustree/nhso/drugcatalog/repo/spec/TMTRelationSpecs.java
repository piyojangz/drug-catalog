/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo.spec;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug_;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation_;

/**
 *
 * @author moth
 */
public class TMTRelationSpecs {

    public static Specification<TMTRelation> fsnContains(final List<String> keywords) {
        return new Specification<TMTRelation>() {

            @Override
            public Predicate toPredicate(Root<TMTRelation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String key : keywords) {
                    if (or == null) {
                        or = cb.like(cb.lower(root.get(TMTRelation_.parent).get(TMTDrug_.fsn)), "%" + key.toLowerCase() + "%");
                        or = cb.or(or, cb.like(cb.lower(root.get(TMTRelation_.child).get(TMTDrug_.fsn)), "%" + key.toLowerCase() + "%"));
                    } else {
                        or = cb.or(or, cb.like(cb.lower(root.get(TMTRelation_.parent).get(TMTDrug_.fsn)), "%" + key.toLowerCase() + "%"));
                        or = cb.or(or, cb.like(cb.lower(root.get(TMTRelation_.child).get(TMTDrug_.fsn)), "%" + key.toLowerCase() + "%"));
                    }
                }
                return or;
            }
        };
    }

    public static Specification<TMTRelation> tmtIdContains(final List<String> keywords) {
        return new Specification<TMTRelation>() {

            @Override
            public Predicate toPredicate(Root<TMTRelation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String key : keywords) {
                    if (or == null) {
                        or = cb.like(cb.lower(root.get(TMTRelation_.parent).get(TMTDrug_.tmtId)), "%" + key.toLowerCase() + "%");
                        or = cb.or(or, cb.like(cb.lower(root.get(TMTRelation_.child).get(TMTDrug_.tmtId)), "%" + key.toLowerCase() + "%"));
                    } else {
                        or = cb.or(or, cb.like(cb.lower(root.get(TMTRelation_.parent).get(TMTDrug_.tmtId)), "%" + key.toLowerCase() + "%"));
                        or = cb.or(or, cb.like(cb.lower(root.get(TMTRelation_.child).get(TMTDrug_.tmtId)), "%" + key.toLowerCase() + "%"));
                    }
                }
                return or;
            }
        };
    }

    public static Specification<TMTRelation> typeIn(final List<TMTDrug.Type> types) {
        return new Specification<TMTRelation>() {

            @Override
            public Predicate toPredicate(Root<TMTRelation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                or = root.get(TMTRelation_.parent).get(TMTDrug_.type).in(types);
                or = cb.or(or, root.get(TMTRelation_.child).get(TMTDrug_.type).in(types));
                return or;
            }
        };
    }

}
