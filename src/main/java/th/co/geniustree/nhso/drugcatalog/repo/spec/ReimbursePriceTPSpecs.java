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
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTPID_;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP_;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug_;

/**
 *
 * @author Thanthathon
 */
public class ReimbursePriceTPSpecs {

    public static Specification<ReimbursePriceTP> tmtLike(final List<String> tmtid) {
        return new Specification<ReimbursePriceTP>() {
            @Override
            public Predicate toPredicate(Root<ReimbursePriceTP> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : tmtid) {
                    if (or == null) {
                        or = cb.like(root.get(ReimbursePriceTP_.tmtDrug).get(TMTDrug_.tmtId), "%" + keyword + "%");
                    } else {
                        cb.or(or, cb.like(root.get(ReimbursePriceTP_.tmtDrug).get(TMTDrug_.tmtId), "%" + keyword + "%"));
                    }
                }
                return or;
            }
        };
    }

    public static Specification<ReimbursePriceTP> fsnLike(final List<String> fsn) {
        return new Specification<ReimbursePriceTP>() {
            @Override
            public Predicate toPredicate(Root<ReimbursePriceTP> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : fsn) {
                    if (or == null) {
                        or = cb.like(root.get(ReimbursePriceTP_.tmtDrug).get(TMTDrug_.fsn), "%" + keyword + "%");
                    } else {
                        cb.or(or, cb.like(root.get(ReimbursePriceTP_.tmtDrug).get(TMTDrug_.fsn), "%" + keyword + "%"));
                    }
                }
                return or;
            }
        };
    }

    public static Specification<ReimbursePriceTP> hcodeLike(final List<String> kywords) {
        return new Specification<ReimbursePriceTP>() {
            @Override
            public Predicate toPredicate(Root<ReimbursePriceTP> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : kywords) {
                    if (or == null) {
                        or = cb.like(cb.lower(root.get(ReimbursePriceTP_.id).get(ReimbursePriceTPID_.hcode)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.or(or, cb.like(cb.lower(root.get(ReimbursePriceTP_.id).get(ReimbursePriceTPID_.hcode)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return or;
            }
        };
    }
    
    public static Specification<ReimbursePriceTP> hospDrugCodeLike(final List<String> kywords) {
        return new Specification<ReimbursePriceTP>() {
            @Override
            public Predicate toPredicate(Root<ReimbursePriceTP> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : kywords) {
                    if (or == null) {
                        or = cb.like(cb.lower(root.get(ReimbursePriceTP_.id).get(ReimbursePriceTPID_.hospDrugCode)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.or(or, cb.like(cb.lower(root.get(ReimbursePriceTP_.id).get(ReimbursePriceTPID_.hospDrugCode)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return or;
            }
        };
    }
}
