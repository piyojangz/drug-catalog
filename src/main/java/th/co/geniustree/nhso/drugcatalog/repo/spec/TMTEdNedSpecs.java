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
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug_;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed_;

/**
 *
 * @author Thanthathon
 */
public class TMTEdNedSpecs {
    
    public static Specification<TMTEdNed> tmtIdLike(final List<String> keywords) {
        return new Specification<TMTEdNed>() {

            @Override
            public Predicate toPredicate(Root<TMTEdNed> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(TMTEdNed_.tmtDrug).get(TMTDrug_.tmtId)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(TMTEdNed_.tmtDrug).get(TMTDrug_.tmtId)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
    
    public static Specification<TMTEdNed> fsnLike(final List<String> keywords) {
        return new Specification<TMTEdNed>() {

            @Override
            public Predicate toPredicate(Root<TMTEdNed> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(TMTEdNed_.tmtDrug).get(TMTDrug_.fsn)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(TMTEdNed_.tmtDrug).get(TMTDrug_.fsn)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
    
}
