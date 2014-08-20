/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.authen.WSUserDetails;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.HospitalDrugSpecs;

/**
 *
 * @author moth
 */
@Scope("session")
@Component
public class HospitalDrugListController implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(HospitalDrugListController.class);

    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    private SpringDataLazyDataModelSupport<HospitalDrug> all;
    private SpringDataLazyDataModelSupport<HospitalDrug> models;
    private SpringDataLazyDataModelSupport<HospitalDrug> noTmtModels;
    private SpringDataLazyDataModelSupport<HospitalDrug> waitModels;
    private WSUserDetails user;
    private List<String> selectColumns = Arrays.asList(new String[]{"HOSPDRUGCODE", "TMTID", "GENERICNAME", "TRADENAME", "DOSAGEFORM"});
    private String keyword;

    @PostConstruct
    public void postConstruct() {
        user = SecurityUtil.getUserDetails();
        models = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalDrugRepo.findByHcodeAndApproved(user.getOrgId(), true, pageAble);
            }
        };
        noTmtModels = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalDrugRepo.findByHcodeAndTmtIdIsNull(user.getOrgId(), pageAble);
            }
        };
        waitModels = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalDrugRepo.findByHcodeAndApprovedAndTmtIdIsNotNull(user.getOrgId(), false, pageAble);
            }

        };
        search();
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getModels() {
        return models;
    }

    public void setModels(SpringDataLazyDataModelSupport<HospitalDrug> models) {
        this.models = models;
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getNoTmtModels() {
        return noTmtModels;
    }

    public void setNoTmtModels(SpringDataLazyDataModelSupport<HospitalDrug> noTmtModels) {
        this.noTmtModels = noTmtModels;
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getWaitModels() {
        return waitModels;
    }

    public void setWaitModels(SpringDataLazyDataModelSupport<HospitalDrug> waitModels) {
        this.waitModels = waitModels;
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getAll() {
        return all;
    }

    public void setAll(SpringDataLazyDataModelSupport<HospitalDrug> all) {
        this.all = all;
    }

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public void search() {
        final List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);
        all = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                Specifications<HospitalDrug> hcodeEq = Specifications.where(HospitalDrugSpecs.hcodeEq(user.getOrgId()));
                Specifications<HospitalDrug> spec = Specifications.where(null);
                if (keywords != null) {

                    if (selectColumns.contains("HOSPDRUGCODE")) {
                        spec = spec.or(HospitalDrugSpecs.hospDrugCodeLike(keywords));
                    }
                    if (selectColumns.contains("TMTID")) {
                        spec = spec.or(HospitalDrugSpecs.tmtIdLike(keywords));
                    }
                    if (selectColumns.contains("GENERICNAME")) {
                        spec = spec.or(HospitalDrugSpecs.genericNameLike(keywords));
                    }
                    if (selectColumns.contains("TRADENAME")) {
                        spec = spec.or(HospitalDrugSpecs.tradeNameLike(keywords));
                    }
                    if (selectColumns.contains("DOSAGEFORM")) {
                        spec = spec.or(HospitalDrugSpecs.dosageFormLike(keywords));
                    }
                }
                return hospitalDrugRepo.findAll(hcodeEq.and(spec), pageAble);
            }

        };
    }

}
