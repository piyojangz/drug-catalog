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
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem_;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem_;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug_;

/**
 *
 * @author moth
 */
public class RequestItemSpecs {

    public static Specification<RequestItem> hcodeEq(final String hcode) {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.uploadDrug).get(UploadHospitalDrug_.hcode), hcode);
            }

        };
    }

    public static Specification<RequestItem> hospDrugCodeLike(final List<String> keywords) {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.hospDrugCode)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.hospDrugCode)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<RequestItem> tmtIdLike(final List<String> keywords) {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.tmtId)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.tmtId)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<RequestItem> tmtIdIsNull() {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.isNull(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.tmtId));
            }

        };
    }

    public static Specification<RequestItem> statusEq(final RequestItem.Status status) {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(RequestItem_.status), status);
            }

        };
    }

    public static Specification<RequestItem> tradeNameLike(final List<String> keywords) {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.tradeName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.tradeName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<RequestItem> genericNameLike(final List<String> keywords) {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.genericName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.genericName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<RequestItem> dosageFormLike(final List<String> keywords) {
        return new Specification<RequestItem>() {

            @Override
            public Predicate toPredicate(Root<RequestItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.dosageForm)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(RequestItem_.uploadDrugItem).get(UploadHospitalDrugItem_.dosageForm)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
}
