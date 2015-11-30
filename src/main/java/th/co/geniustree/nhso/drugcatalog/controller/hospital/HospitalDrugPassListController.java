/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.drugcatalog.authen.Role;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.authen.WSUserDetails;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.HospitalDrugSpecs;

/**
 *
 * @author thanthathon.b
 */
@Scope("view")
@Component
public class HospitalDrugPassListController {

    private static final Logger LOG = LoggerFactory.getLogger(HospitalDrugPassListController.class);

    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    private SpringDataLazyDataModelSupport<HospitalDrug> all;
    private WSUserDetails user;
    private List<String> selectColumns = Arrays.asList(new String[]{"HOSPDRUGCODE", "TMTID", "GENERICNAME", "TRADENAME", "DOSAGEFORM"});
    private String keyword = "";
    private boolean wait;
    private boolean noTmt;
    private boolean approved;
    private boolean notApproved;
    private String hcode;
    private String selectedHcode;
    private Hospital selectedHospital;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;

    @PostConstruct
    public void postConstruct() {
        user = SecurityUtil.getUserDetails();
        if (user.getAuthorities().contains(Role.HOSPITAL)) {
            selectedHcode = user.getOrgId();
        }
        search();
    }

    public void showSearchHospitalDialog() {
        if (checkHospitalReturnOneElement()) {
            hcode = selectedHospital.getFullName();
            selectedHcode = selectedHospital.getHcode();
            search();
            return;
        }
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        List<String> keywords = new ArrayList<>();
        keywords.add(hcode);
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/common/searchHospitalDialog", options, params);
    }

    private boolean checkHospitalReturnOneElement() {
        String _keyword = "%" + Strings.nullToEmpty(this.hcode).trim() + "%";
        PageRequest pageRequest = new PageRequest(0, 3);
        Page<Hospital> findHospitalInTmt = uploadHospitalDrugRepo.findHospitalInTmt(_keyword, _keyword, pageRequest);
        if (findHospitalInTmt.getTotalElements() == 1) {
            selectedHospital = findHospitalInTmt.getContent().get(0);
            return true;
        } else {
            return false;
        }
    }

    public void searchHospitalDialogReturn(SelectEvent event) {
        Hospital hospital = (Hospital) event.getObject();
        if (hospital != null) {
            hcode = hospital.getFullName();
            selectedHcode = hospital.getHcode();
            search();
        }
        LOG.info("selected hospital from search dialog is => {}", selectedHcode);
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getSelectedHcode() {
        return selectedHcode;
    }

    public void setSelectedHcode(String selectedHcode) {
        this.selectedHcode = selectedHcode;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public boolean isNoTmt() {
        return noTmt;
    }

    public void setNoTmt(boolean noTmt) {
        this.noTmt = noTmt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
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

    public boolean isNotApproved() {
        return notApproved;
    }

    public void setNotApproved(boolean notApproved) {
        this.notApproved = notApproved;
    }

    public void search() {
        final List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);
        all = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                Specifications<HospitalDrug> hcodeEq = Specifications.where(HospitalDrugSpecs.hcodeEq(selectedHcode));
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
                Page<HospitalDrug> findAll = hospitalDrugRepo.findAll(hcodeEq.and(spec), pageAble);
                return findAll;
            }
        };
    }

    public WSUserDetails getUser() {
        return user;
    }

    public void setUser(WSUserDetails user) {
        this.user = user;
    }

    public String formattedDate() {
        return DateUtils.format("yyyyMMDD", new Date());
    }
}
