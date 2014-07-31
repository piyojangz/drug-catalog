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
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug_;

/**
 *
 * @author moth
 */
public class TMTDrugSpecs {

    public static Specification<TMTDrug> fsnContains(final List<String> keywords) {
        return new Specification<TMTDrug>() {

            @Override
            public Predicate toPredicate(Root<TMTDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String key : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(TMTDrug_.fsn)), "%" + key.toLowerCase() + "%");
                    } else {
                        and = cb.and(and, cb.like(cb.lower(root.get(TMTDrug_.fsn)), "%" + key.toLowerCase() + "%"));
                    }
                }
                return and;
            }
        };
    }

    public static Specification<TMTDrug> tmtIdContains(final List<String> keywords) {
        return new Specification<TMTDrug>() {

            @Override
            public Predicate toPredicate(Root<TMTDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String key : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(TMTDrug_.tmtId)), "%" + key.toLowerCase() + "%");
                    } else {
                        and = cb.and(and, cb.like(cb.lower(root.get(TMTDrug_.tmtId)), "%" + key.toLowerCase() + "%"));
                    }
                }
                return and;
            }
        };
    }

    public static Specification<TMTDrug> typeIn(final List<Type> types) {
        return new Specification<TMTDrug>() {

            @Override
            public Predicate toPredicate(Root<TMTDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get(TMTDrug_.type).in(types);
            }
        };
    }
}
