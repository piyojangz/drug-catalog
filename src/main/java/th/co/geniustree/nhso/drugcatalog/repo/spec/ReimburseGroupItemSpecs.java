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
import th.co.geniustree.nhso.drugcatalog.model.Drug_;
import th.co.geniustree.nhso.drugcatalog.model.Fund_;
import th.co.geniustree.nhso.drugcatalog.model.ICD10_;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem_;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup_;

/**
 *
 * @author Thanthathon
 */
public class ReimburseGroupItemSpecs {

    public static Specification<ReimburseGroupItem> tmtIdLike(final List<String> keywords) {
        return new Specification<ReimburseGroupItem>() {

            @Override
            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(root.get(ReimburseGroupItem_.drug).get(Drug_.tmtId), "%" + keyword + "%");
                    } else {
                        cb.and(and, cb.like(root.get(ReimburseGroupItem_.drug).get(Drug_.tmtId), "%" + keyword + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<ReimburseGroupItem> fundIdLike(final List<String> keywords) {
        return new Specification<ReimburseGroupItem>() {

            @Override
            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(root.get(ReimburseGroupItem_.fund).get(Fund_.fundCode), "%" + keyword + "%");
                    } else {
                        cb.and(and, cb.like(root.get(ReimburseGroupItem_.fund).get(Fund_.fundCode), "%" + keyword + "%"));
                    }
                }
                return and;
            }
        };
    }

    public static Specification<ReimburseGroupItem> fundNameLike(final List<String> keywords) {
        return new Specification<ReimburseGroupItem>() {

            @Override
            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(ReimburseGroupItem_.fund).get(Fund_.name)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(ReimburseGroupItem_.fund).get(Fund_.name)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

//    public static Specification<ReimburseGroupItem> icd10IdLike(final List<String> keywords) {
//        return new Specification<ReimburseGroupItem>() {
//
//            @Override
//            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Predicate and = null;
//                for (String keyword : keywords) {
//                    if (and == null) {
//                        and = cb.like(cb.lower(root.get(ReimburseGroupItem_.icd10).get(ICD10_.id)), "%" + keyword.toLowerCase() + "%");
//                    } else {
//                        cb.and(and, cb.like(cb.lower(root.get(ReimburseGroupItem_.icd10).get(ICD10_.id)), "%" + keyword.toLowerCase() + "%"));
//                    }
//                }
//                return and;
//            }
//        };
//    }
//    
//    public static Specification<ReimburseGroupItem> icd10NameLike(final List<String> keywords) {
//        return new Specification<ReimburseGroupItem>() {
//
//            @Override
//            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Predicate and = null;
//                for (String keyword : keywords) {
//                    if (and == null) {
//                        and = cb.like(cb.lower(root.get(ReimburseGroupItem_.icd10).get(ICD10_.name)), "%" + keyword.toLowerCase() + "%");
//                    } else {
//                        cb.and(and, cb.like(cb.lower(root.get(ReimburseGroupItem_.icd10).get(ICD10_.name)), "%" + keyword.toLowerCase() + "%"));
//                    }
//                }
//                return and;
//            }
//        };
//    }

    public static Specification<ReimburseGroupItem> edStatusEq(final List<String> keywords) {
        return new Specification<ReimburseGroupItem>() {

            @Override
            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.equal(root.get(ReimburseGroupItem_.edStatus), "%" + keyword.toUpperCase() + "%");
                    } else {
                        cb.and(and, cb.equal(root.get(ReimburseGroupItem_.edStatus), "%" + keyword.toUpperCase() + "%"));
                    }
                }
                return and;
            }
        };
    }

//    public static Specification<ReimburseGroupItem> reimburseGroupIdLike(final List<String> keywords) {
//        return new Specification<ReimburseGroupItem>() {
//
//            @Override
//            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Predicate and = null;
//                for (String keyword : keywords) {
//                    if (and == null) {
//                        and = cb.like(cb.lower(root.get(ReimburseGroupItem_.reimburseGroup).get(ReimburseGroup_.id)), "%" + keyword.toLowerCase() + "%");
//                    } else {
//                        cb.and(and, cb.like(cb.lower(root.get(ReimburseGroupItem_.reimburseGroup).get(ReimburseGroup_.id)), "%" + keyword.toLowerCase() + "%"));
//                    }
//                }
//                return and;
//            }
//        };
//    }
//
//    public static Specification<ReimburseGroupItem> specialProjectEq(final Boolean specialProject) {
//        return new Specification<ReimburseGroupItem>() {
//
//            @Override
//            public Predicate toPredicate(Root<ReimburseGroupItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                return cb.equal(root.get(ReimburseGroupItem_.reimburseGroup).get(ReimburseGroup_.specialProject), specialProject);
//            }
//        };
//    }

}
