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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.Worker;
import th.co.geniustree.nhso.drugcatalog.controller.utils.Workers;
import th.co.geniustree.nhso.drugcatalog.model.NDC24;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.TMTEdNedRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.UploadHospitalDrugItemSpecs;
import th.co.geniustree.nhso.drugcatalog.repo.spec.UploadHospitalDrugItemTempSpecs;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class HospitalDrugListController implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(HospitalDrugListController.class);

    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    private SpringDataLazyDataModelSupport<UploadHospitalDrugItem> all;
    private WSUserDetails user;
    private List<String> selectColumns = Arrays.asList(new String[]{"HOSPDRUGCODE", "TMTID", "GENERICNAME", "TRADENAME", "DOSAGEFORM"});
    private String keyword = "";
    private boolean wait;
    private boolean noTmt;
    private boolean approved;
    private boolean notApproved;
    private Map<UploadHospitalDrugItem, UploadHospitalDrugItemEx> uploadItemEx;
    @Autowired
    private TMTEdNedRepo tmtEdNedRepo;
    private List<String> selectedProductCats = Arrays.asList(new String[]{"1", "2", "3", "4", "5", "6", "7"});
    private boolean selectedOnlyNullTMT = false;
    
    private UploadHospitalDrugItem errorItem;

    @PostConstruct
    public void postConstruct() {
        user = SecurityUtil.getUserDetails();
        search();
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

    public SpringDataLazyDataModelSupport<UploadHospitalDrugItem> getAll() {
        return all;
    }

    public void setAll(SpringDataLazyDataModelSupport<UploadHospitalDrugItem> all) {
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

    public List<String> getSelectedProductCats() {
        return selectedProductCats;
    }

    public void setSelectedProductCats(List<String> selectedProductCats) {
        this.selectedProductCats = selectedProductCats;
    }

    public UploadHospitalDrugItem getErrorItem() {
        return errorItem;
    }

    public void setErrorItem(UploadHospitalDrugItem errorItem) {
        this.errorItem = errorItem;
    }

    public boolean isSelectedOnlyNullTMT() {
        return selectedOnlyNullTMT;
    }

    public void setSelectedOnlyNullTMT(boolean selectedOnlyNullTMT) {
        this.selectedOnlyNullTMT = selectedOnlyNullTMT;
    }

    public void search() {
        final List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);
        all = new SpringDataLazyDataModelSupport<UploadHospitalDrugItem>() {

            @Override
            public Page<UploadHospitalDrugItem> load(Pageable pageAble) {
                Specifications<UploadHospitalDrugItem> hcodeEq = Specifications.where(UploadHospitalDrugItemSpecs.hcodeEq(user.getOrgId()))
                        .and(UploadHospitalDrugItemSpecs.notDelete());
                Specifications<UploadHospitalDrugItem> spec = Specifications.where(null);
                if (keywords != null) {
                    if (selectColumns.contains("HOSPDRUGCODE")) {
                        spec = spec.or(UploadHospitalDrugItemSpecs.hospDrugCodeLike(keywords));
                    }
                    if (selectColumns.contains("TMTID")) {
                        spec = spec.or(UploadHospitalDrugItemSpecs.tmtIdLike(keywords));
                    }
                    if (selectColumns.contains("GENERICNAME")) {
                        spec = spec.or(UploadHospitalDrugItemSpecs.genericNameLike(keywords));
                    }
                    if (selectColumns.contains("TRADENAME")) {
                        spec = spec.or(UploadHospitalDrugItemSpecs.tradeNameLike(keywords));
                    }
                    if (selectColumns.contains("DOSAGEFORM")) {
                        spec = spec.or(UploadHospitalDrugItemSpecs.dosageFormLike(keywords));
                    }
                }
                spec = Specifications.where(spec).and(UploadHospitalDrugItemSpecs.productCatIn(selectedProductCats));
                if (selectedOnlyNullTMT) {
                    spec = spec.and(UploadHospitalDrugItemSpecs.tmtidIsNull());
                }
                if (wait) {
                    spec = spec.and(UploadHospitalDrugItemSpecs.waitApprove());
                }
//                if (noTmt) {
//                    spec = spec.and(UploadHospitalDrugItemSpecs.noTmt());
//                }
                if (approved) {
                    spec = spec.and(UploadHospitalDrugItemSpecs.approved());
                }
                if (notApproved) {
                    LOG.debug("view not approve drug.");
                    spec = spec.and(UploadHospitalDrugItemSpecs.notApproved());
                }
                Page<UploadHospitalDrugItem> findAll = uploadHospitalDrugItemRepo.findAll(hcodeEq.and(spec), pageAble);
                if (approved) {
                    makeUploadEx(findAll);
                }
                return findAll;
            }
        };
    }

    private void makeUploadEx(Page<UploadHospitalDrugItem> findAll) {
        uploadItemEx = new HashMap<>();
        for (UploadHospitalDrugItem item : findAll) {
            String findLastestEdByTmtId = tmtEdNedRepo.findLastestEdByTmtId(item.getTmtId(), item.getDateEffectiveDate());
            if (findLastestEdByTmtId != null) {
                uploadItemEx.put(item, new UploadHospitalDrugItemEx((String) findLastestEdByTmtId, "01"));
            } else {
                uploadItemEx.put(item, new UploadHospitalDrugItemEx(item.getIsed(), "99"));
            }//TODO FIX NPE
            List<NDC24> ndc24s = item.getTmtDrug().getNdc24s();
            String ndc24 = "";
            if (ndc24s.size() == 1) {
                ndc24 = ndc24s.get(0).getNdc24();
            }
            uploadItemEx.get(item).setNdc24(ndc24);
        }
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

    public Map<UploadHospitalDrugItem, UploadHospitalDrugItemEx> getUploadItemEx() {
        return uploadItemEx;
    }

    public void setUploadItemEx(Map<UploadHospitalDrugItem, UploadHospitalDrugItemEx> uploadItemEx) {
        this.uploadItemEx = uploadItemEx;
    }

    public void delete(final UploadHospitalDrugItem item) {
        Workers.execute(new Worker() {

            @Override
            public Object run() throws Exception {
                if (item.getRequestItem().getStatus() == RequestItem.Status.ACCEPT) {
                    return "ไม่สามารถลบรายการที่อนุมัติแล้ว";
                }
                item.getRequestItem().setDeleted(Boolean.TRUE);
                uploadHospitalDrugItemRepo.save(item);
                return "ลบเสร็จแล้ว";
            }

        });
    }
    
    public void showRemarkDialog(UploadHospitalDrugItem item){
        errorItem = item;
    }
    
    public void closeRemarkDialog(){
        errorItem = null;
    }

    public static class UploadHospitalDrugItemEx {

        private String isedApproved;
        private String isedStatus;
        private String ndc24;

        public UploadHospitalDrugItemEx(String isedApproved, String isedStatus) {
            this.isedApproved = isedApproved;
            this.isedStatus = isedStatus;
            this.ndc24 = ndc24;
        }

        public String getIsedApproved() {
            return isedApproved;
        }

        public void setIsedApproved(String isedApproved) {
            this.isedApproved = isedApproved;
        }

        public String getIsedStatus() {
            return isedStatus;
        }

        public void setIsedStatus(String isedStatus) {
            this.isedStatus = isedStatus;
        }

        public String getNdc24() {
            return ndc24;
        }

        public void setNdc24(String ndc24) {
            this.ndc24 = ndc24;
        }

    }
}
