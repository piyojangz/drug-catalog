/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo.spec;

import java.util.Date;
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
    
    public static Specification<TMTEdNed> edStatusLike(final List<String> keywords) {
        return new Specification<TMTEdNed>() {

            @Override
            public Predicate toPredicate(Root<TMTEdNed> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(TMTEdNed_.statusEd)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(TMTEdNed_.statusEd)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
    
    public static Specification<TMTEdNed> dateInRange(final Date start, final Date end) {
        return new Specification<TMTEdNed>() {

            @Override
            public Predicate toPredicate(Root<TMTEdNed> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get(TMTEdNed_.dateIn), start, end);
            }

        };
    }
    
    public static Specification<TMTEdNed> dateBefore(final Date date) {
        return new Specification<TMTEdNed>() {

            @Override
            public Predicate toPredicate(Root<TMTEdNed> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.lessThanOrEqualTo(root.get(TMTEdNed_.dateIn), date);
            }

        };
    }
    
    public static Specification<TMTEdNed> dateAfter(final Date date) {
        return new Specification<TMTEdNed>() {

            @Override
            public Predicate toPredicate(Root<TMTEdNed> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.greaterThanOrEqualTo(root.get(TMTEdNed_.dateIn), date);
            }

        };
    }
    
}
