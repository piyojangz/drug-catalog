/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.RequestItemSpecs;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;
import th.co.geniustree.nhso.drugcatalog.service.RequestItemService;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class AdminInbox implements Serializable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminInbox.class);

    private List<String> selectColumns = new ArrayList<>();
    private final Set<String> errorColumnSet = new HashSet<>();

    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private RequestItemService requestItemService;
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    @Autowired
    private ApproveService approveService;

    private SpringDataLazyDataModelSupport<RequestItem> requestItems;
    private List<SpringDataLazyDataModelSupport<RequestItem>> requestItemHoldersNullTMT = new ArrayList<>();
    private List<List<RequestItem>> requestItemHolders = new ArrayList<>();
    private List<RequestItem> approveRequests = new ArrayList<>();
    private List<RequestItem> notApproveRequests = new ArrayList<>();

    private List<TMTDrug> tmtDrugs = new ArrayList<>();
    private UploadHospitalDrugItem uploadDrugItem;
    private String searchName;

    private Hospital selectedHospital;
    private String hcode;
    private long totalElements;
    private long displayElement;
    private String keyword;
    private boolean nullTmt;
    private String haveTmt;

    @PostConstruct
    public void postConstruct() {
        selectColumns.add("HOSPDRUGCODE");
        selectColumns.add("TMTID");
        selectColumns.add("GENERICNAME");
        selectColumns.add("TRADENAME");
        selectColumns.add("DOSAGEFORM");

        errorColumnSet.add("PRODUCTCAT");
        errorColumnSet.add("TRADENAME");
        errorColumnSet.add("GENRICNAME");
        errorColumnSet.add("STRENGTH");
        errorColumnSet.add("DOSAGEFORM");
        errorColumnSet.add("CONTENT");
        errorColumnSet.add("MANUFACTURER");
        errorColumnSet.add("UNITPRICE");
        errorColumnSet.add("ISED");
        errorColumnSet.add("SPECPREP");
    }

    public String getHaveTmt() {
        return haveTmt;
    }

    public void setHaveTmt(String haveTmt) {
        this.haveTmt = haveTmt;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public boolean isNullTmt() {
        return nullTmt;
    }

    public void setNullTmt(boolean nullTmt) {
        this.nullTmt = nullTmt;
    }

    public List<SpringDataLazyDataModelSupport<RequestItem>> getRequestItemHoldersNullTMT() {
        return requestItemHoldersNullTMT;
    }

    public void setRequestItemHoldersNullTMT(List<SpringDataLazyDataModelSupport<RequestItem>> requestItemHoldersNullTMT) {
        this.requestItemHoldersNullTMT = requestItemHoldersNullTMT;
    }

    public SpringDataLazyDataModelSupport<RequestItem> getRequestItems() {
        return requestItems;
    }

    public List<List<RequestItem>> getRequestItemHolders() {
        return requestItemHolders;
    }

    public void setRequestItemHolders(List<List<RequestItem>> requestItemHolders) {
        this.requestItemHolders = requestItemHolders;
    }

    public Hospital getSelectedHospital() {
        return selectedHospital;
    }

    public void setSelectedHospital(Hospital selectedHospital) {
        this.selectedHospital = selectedHospital;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public List<RequestItem> getApproveRequests() {
        return approveRequests;
    }

    public void setApproveRequests(List<RequestItem> approveRequests) {
        this.approveRequests = approveRequests;
    }

    public List<RequestItem> getNotApproveRequests() {
        return notApproveRequests;
    }

    public void setNotApproveRequests(List<RequestItem> notApproveRequests) {
        this.notApproveRequests = notApproveRequests;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getDisplayElement() {
        return displayElement;
    }

    public void setDisplayElement(long displayElement) {
        this.displayElement = displayElement;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public List<TMTDrug> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(List<TMTDrug> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
    }

    public UploadHospitalDrugItem getUploadDrugItem() {
        return uploadDrugItem;
    }

    public void setUploadDrugItem(UploadHospitalDrugItem uploadDrugItem) {
        this.uploadDrugItem = uploadDrugItem;
    }

    public void showSearchHospitalDialog() {
        clearAll();
        if (checkHospitalReturnOneElement()) {
            hcode = selectedHospital.getFullName();
            search();
            return;
        }
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(hcode);
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/common/searchHospitalDialog", options, params);
    }

    public void searchHospitalDialogReturn(SelectEvent event) {
        selectedHospital = (Hospital) event.getObject();
        if (selectedHospital != null) {
            hcode = selectedHospital.getFullName();
            search();
        }
        log.info("selected hospital from search dialog is => {}", selectedHospital.getFullName());
    }

    public void search() {
        if (selectedHospital != null) {
            if (nullTmt == true) {
                requestItemHoldersNullTMT.clear();
                requestItems = new SpringDataLazyDataModelSupport<RequestItem>() {
                    @Override
                    public Page<RequestItem> load(Pageable pageAble) {
                        Page<RequestItem> result = requestItemService.findByStatusAndHcodeAndTmtIdIsNull(RequestItem.Status.REQUEST, selectedHospital.getHcode(), pageAble);
                        setApproveSelected(result);
                        return result;
                    }
                };
                requestItemHoldersNullTMT.add(requestItems);
            } else {
                requestItemHolders.clear();
                log.debug("selectedHospital hcode -> {}", selectedHospital.getHcode());
                Page<RequestItem> pageResult = requestItemRepo.findByStatusAndHcodeAndNotDel(RequestItem.Status.REQUEST,
                        selectedHospital.getHcode(), new PageRequest(0, 10, Sort.Direction.ASC, "requestDate"));
                totalElements = pageResult.getTotalElements();
                displayElement = pageResult.getSize();
                for (RequestItem item : pageResult.getContent()) {
                    log.debug("hospdrugcode -> {}", item.getUploadDrugItem().getHospDrugCode());
                    List<RequestItem> requestItemList = new ArrayList<>();
                    requestItemList.add(createRequestFormTmt(item));
                    requestItemList.add(item);
                    requestItemHolders.add(requestItemList);
                }

            }
        }
    }

    private void setApproveSelected(Page<RequestItem> page) {
        List<RequestItem> items = page.getContent();
        for (RequestItem item : items) {
            for (RequestItem approve : approveRequests) {
                if (approve.equals(item)) {
                    item.setStatus(RequestItem.Status.ACCEPT);
                }
            }
            for (RequestItem notApprove : notApproveRequests) {
                if (notApprove.equals(item)) {
                    item.setStatus(RequestItem.Status.REJECT);
                    item.setErrorColumns(notApprove.getErrorColumns());
                }
            }
        }
    }

    private RequestItem createRequestFormTmt(RequestItem item) {
        TMTDrug tmtDrug = tmtDrugRepo.findOne(item.getUploadDrugItem().getTmtId());
        if (tmtDrug != null) {
            return new RequestItem(tmtDrug);
        }
        return new RequestItem();
    }

    private Specifications spec() {
        Specification hcodeSpec = RequestItemSpecs.hcodeEq(selectedHospital.getHcode());
        Specification requestStatusSpec = RequestItemSpecs.statusEq(RequestItem.Status.REQUEST);
        Specification deleteSpec = RequestItemSpecs.deleteIsFalse();
        Specification tmtSpec = RequestItemSpecs.tmtIdIsNull(nullTmt);

        Specifications spec = Specifications.where(hcodeSpec).and(requestStatusSpec).and(deleteSpec).and(tmtSpec);
        return spec;
    }

    private Specifications advanceSearchSpec(List<String> keywords) {
        Specifications<RequestItem> keySpec = Specifications.where(null);
        if (keywords != null) {
            if (selectColumns.contains("HOSPDRUGCODE")) {
                keySpec = keySpec.or(RequestItemSpecs.hospDrugCodeLike(keywords));
            }
            if (selectColumns.contains("TMTID")) {
                keySpec = keySpec.or(RequestItemSpecs.tmtIdLike(keywords));
            }
            if (selectColumns.contains("GENERICNAME")) {
                keySpec = keySpec.or(RequestItemSpecs.genericNameLike(keywords));
            }
            if (selectColumns.contains("TRADENAME")) {
                keySpec = keySpec.or(RequestItemSpecs.tradeNameLike(keywords));
            }
            if (selectColumns.contains("DOSAGEFORM")) {
                keySpec = keySpec.or(RequestItemSpecs.dosageFormLike(keywords));
            }
        }
        return keySpec;
    }

    public void searchByKeyword() {
        final List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);
        final Specifications<RequestItem> spec = spec().and(advanceSearchSpec(keywords));

        if (nullTmt == true) {
            requestItems = new SpringDataLazyDataModelSupport<RequestItem>() {
                @Override
                public Page<RequestItem> load(Pageable pageAble) {

                    Page<RequestItem> items = requestItemRepo.findAll(spec, pageAble);
                    setApproveSelected(items);
                    return items;
                }
            };
            requestItemHoldersNullTMT.clear();
            requestItemHoldersNullTMT.add(requestItems);
        } else {
            requestItemHolders.clear();
            Pageable pageRequest = new PageRequest(0, 10, Sort.Direction.ASC, "requestDate");
            Page<RequestItem> pageResult = requestItemRepo.findAll(spec, pageRequest);

            totalElements = pageResult.getTotalElements();
            displayElement = pageResult.getSize();
            for (RequestItem item : pageResult.getContent()) {
                log.debug("tmtid -> {}", item.getUploadDrugItem().getTmtId());
                List<RequestItem> requestItemList = new ArrayList<>();
                requestItemList.add(createRequestFormTmt(item));
                requestItemList.add(item);
                requestItemHolders.add(requestItemList);
            }
        }
    }

    public void error(RequestItem requestItem, String columnName) {
        requestItem.getErrorColumns().add(columnName);
        requestItem.setStatus(RequestItem.Status.REJECT);
        if (!notApproveRequests.contains(requestItem)) {
            notApproveRequests.add(requestItem);
        }
        log.info("add error to RequestId = {}", requestItem.getId());
    }

    public void clearErrorColumns(RequestItem requestItem) {
        Set<String> emptyColumn = new HashSet<>();
        requestItem.setErrorColumns(emptyColumn);
        requestItem.setStatus(RequestItem.Status.REQUEST);
        notApproveRequests.remove(requestItem);
        approveRequests.remove(requestItem);
    }

    public void approve(ValueChangeEvent event) {
        UIComponent component = event.getComponent();
        RequestItem item = (RequestItem) component.getAttributes().get("selectedItem");
        log.debug("selectItem = {}", item.getId());
        if (event.getNewValue() != null) {
            item.setStatus(RequestItem.Status.valueOf(event.getNewValue().toString()));
            if (item.getStatus() == RequestItem.Status.ACCEPT) {
                Set<String> set = new HashSet<>();
                item.setErrorColumns(set);
                approveRequests.add(item);
                notApproveRequests.remove(item);
            } else {
                item.setErrorColumns(errorColumnSet);
                notApproveRequests.add(item);
                approveRequests.remove(item);
            }
        }
    }

    public String save() {
        if (notApproveHaveErrorColumn()) {
            List<RequestItem> merge = new ArrayList<>(approveRequests);
            merge.addAll(notApproveRequests);
            approveService.approveOrReject(merge);
            clearAll();
            //TODO send mail to each HCODE
            search();
            FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        }
        return null;
    }

    public String clear() {
        clearAll();
        hcode = "";
        if (isNullTmt()) {
            return "/private/admin/drug/inbox-none-tmt.xhtml?faces-redirect=true";
        } else {
            return "/private/admin/drug/inbox.xhtml?faces-redirect=true";
        }
    }

    private void clearAll() {
        requestItemHolders.clear();
        requestItemHoldersNullTMT.clear();
        notApproveRequests.clear();
        approveRequests.clear();
    }

    private boolean checkHospitalReturnOneElement() {
        String _keyword = "%" + Strings.nullToEmpty(this.hcode).trim() + "%";
        PageRequest pageRequest = new PageRequest(0, 3);
        Page<Hospital> findHospitalInTmt = uploadHospitalDrugRepo.findHospitalInTmt(_keyword, _keyword, pageRequest);
        if (findHospitalInTmt.getTotalElements() == 1) {
            selectedHospital = findHospitalInTmt.getContent().get(0);
            log.info("selected hospital from search dialog is => {}", selectedHospital);
            return true;
        } else {
            return false;
        }
    }

    private boolean notApproveHaveErrorColumn() {
        for (RequestItem notApprove : notApproveRequests) {
            if (notApprove.getErrorColumns().isEmpty()) {
                FacesMessageUtils.error("จะต้องระบุ column ที่ไม่ให้ผ่านด้วย");
                return false;
            }
        }
        return true;
    }

    public void compareWithTMTDrug() {
        log.debug("search name -> {}", searchName);
        String[] searchSplit = searchName.split("[+]");
        List<String> searches = Arrays.asList(searchSplit);
        Specifications spec = Specifications.where(TMTDrugSpecs.fsnContains(searches));
        tmtDrugs = tmtDrugRepo.findAll(spec);
    }

    public void showCompareTmtDialog() {
        searchName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("genericName");
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 700);
        options.put("contentWidth", 900);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(searchName);
        params.put("genericName", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/common/drug/findTMTDialog", options, params);
    }

    public void onActionAfterSelectHospitalFromInBoxZone() {
        if (hcode != null) {
            if(!hcode.isEmpty())
            showSearchHospitalDialog();
        }
    }
}
