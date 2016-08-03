/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;
import th.co.geniustree.nhso.drugcatalog.service.ReimbursePriceService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author thanthathon.b
 */
@Component
@Scope("view")
public class ReimbursePriceTPController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ReimbursePriceTPController.class);

    @Autowired
    private ReimbursePriceService reimbursePriceService;

    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;

    @Autowired
    private TMTDrugService tmtDrugService;
    
    private SpringDataLazyDataModelSupport<ReimbursePriceTP> data;
    private String keyword;
    private ReimbursePriceTP selectedReimbursePriceTP;

    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs;
    private String searchForSelectKeyword;
    private TMTDrug selectTMTDrug;
    private SpringDataLazyDataModelSupport<HospitalDrug> hospitalDrugs;
    private HospitalDrug selectHospitalDrug;
    private Date dateEffective;
    private BigDecimal price;
    
    @PostConstruct
    public void postConstruct() {
        loadData();
    }

    public void searchTMTDrug() {
        tmtDrugs = new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageable) {
                Specification<TMTDrug> spec = Specifications
                        .where(TMTDrugSpecs.typeIn(Arrays.asList(new TMTDrug.Type[]{Type.TP})));
                if (!Strings.isNullOrEmpty(searchForSelectKeyword)) {
                    List<String> keywords = Arrays.asList(searchForSelectKeyword.split("\\s+"));
                    spec = Specifications.where(spec)
                            .and(Specifications
                                    .where(TMTDrugSpecs.tmtIdContains(keywords))
                                    .or(TMTDrugSpecs.fsnContains(keywords)));
                }
                return tmtDrugService.findAllAndEagerGroup(spec, pageable);
            }
        };
    }

    public void loadHospitalDrug(TMTDrug select) {
        selectTMTDrug = select;
        hospitalDrugs = new SpringDataLazyDataModelSupport<HospitalDrug>() {
            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalDrugRepo.findByTMTIDAndApprovedAndNotDeleted(selectTMTDrug.getTmtId(), pageAble);
            }
        };
    }

    public void backToSelectTMT() {
        selectTMTDrug = null;
        hospitalDrugs = null;
    }

    public void reset() {
        selectHospitalDrug = null;
        dateEffective = null;
        price = null;
    }

    public void select(HospitalDrug hospitalDrug) {
        selectHospitalDrug = hospitalDrug;
    }

    public void loadData() {
        data = new SpringDataLazyDataModelSupport<ReimbursePriceTP>() {

            @Override
            public Page<ReimbursePriceTP> load(Pageable pageAble) {
                return reimbursePriceService.findAllTP(pageAble);
            }

        };
    }

    public void search() {
        data = new SpringDataLazyDataModelSupport<ReimbursePriceTP>() {

            @Override
            public Page<ReimbursePriceTP> load(Pageable pageAble) {
                return reimbursePriceService.searchTP(keyword, pageAble);
            }

        };
    }

    public void addNewReimbursePriceTP() {
        try {
            reimbursePriceService.save(selectHospitalDrug, price, dateEffective);
            FacesMessageUtils.info("กำหนดราคายา TP เรียบร้อย");
        } catch (Exception e) {
            FacesMessageUtils.error("เพิ่มรายการยาไม่สำเร็จ");
            LOG.debug("Can't add new ReimbursePriceTP", e);
        }

    }
    
    public void onSelectReimbursePriceTP(ReimbursePriceTP selected){
        selectedReimbursePriceTP = selected;
    }
    
    public void delete() {
        try {
            reimbursePriceService.delete(selectedReimbursePriceTP);
            FacesMessageUtils.info("ลบข้อมูลราคายา เรียบร้อย กรุณาตรวจสอบข้อมูล");
        } catch (Exception e) {
            LOG.error("Can't delete TMT_TMTEDNED", e);
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้");
        }
    }

    public void cancelDelete() {
        selectedReimbursePriceTP = null;
    }

    public SpringDataLazyDataModelSupport<ReimbursePriceTP> getData() {
        return data;
    }

    public void setData(SpringDataLazyDataModelSupport<ReimbursePriceTP> data) {
        this.data = data;
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getHospitalDrugs() {
        return hospitalDrugs;
    }

    public void setHospitalDrugs(SpringDataLazyDataModelSupport<HospitalDrug> hospitalDrugs) {
        this.hospitalDrugs = hospitalDrugs;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public HospitalDrug getSelectHospitalDrug() {
        return selectHospitalDrug;
    }

    public void setSelectHospitalDrug(HospitalDrug selectHospitalDrug) {
        this.selectHospitalDrug = selectHospitalDrug;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSearchForSelectKeyword() {
        return searchForSelectKeyword;
    }

    public void setSearchForSelectKeyword(String searchForSelectKeyword) {
        this.searchForSelectKeyword = searchForSelectKeyword;
    }

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
    }

    public TMTDrug getSelectTMTDrug() {
        return selectTMTDrug;
    }

    public void setSelectTMTDrug(TMTDrug selectTMTDrug) {
        this.selectTMTDrug = selectTMTDrug;
    }

    public ReimbursePriceTP getSelectedReimbursePriceTP() {
        return selectedReimbursePriceTP;
    }

    public void setSelectedReimbursePriceTP(ReimbursePriceTP selectedReimbursePriceTP) {
        this.selectedReimbursePriceTP = selectedReimbursePriceTP;
    }

}
