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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.RequestItemSpecs;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;
import th.co.geniustree.nhso.drugcatalog.service.RequestItemService;

/**
 *
 * @author thanthathon.b
 */
@Component
@Scope
public class AdminInboxNullTMT implements Serializable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminInboxNullTMT.class);
    
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    @Autowired
    private RequestItemService requestItemService;
    @Autowired
    private ApproveService approveService;

    private List<String> selectColumns = new ArrayList<>();
    private final Set<String> errorColumnSet = new HashSet<>();

    private boolean nullTmt;
    private String hcode;
    private Hospital selectedHospital;
    private String keyword;
    private UploadHospitalDrugItem uploadDrugItem;
    private SpringDataLazyDataModelSupport<RequestItem> requestItems;
    private List<SpringDataLazyDataModelSupport<RequestItem>> requestItemHoldersNullTMT = new ArrayList<>();

    private String searchName;
    private String searchType;

    private List<RequestItem> approveRequests = new ArrayList<>();
    private List<RequestItem> notApproveRequests = new ArrayList<>();

    private long totalElements;
    private long displayElement;

    private RequestItem editMessageRequestItem;
    private String messageOfRequestItem;

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
        errorColumnSet.add("TMTID");
    }
    
    public String save() {
        try {
            if (notApproveHaveErrorColumn()) {
                List<RequestItem> merge = new ArrayList<>(approveRequests);
                merge.addAll(notApproveRequests);
                approveService.approveOrReject(merge);
                clearAll();
                //TODO send mail to each HCODE
                search();
                FacesMessageUtils.info("บันทึกเสร็จสิ้น");
            }
        } catch (Exception e) {
            FacesMessageUtils.error("รูปแบบข้อมูลไม่ถูกต้อง ไม่สามารถบันทึกข้อมูลได้");
            log.error(null, e);
        }
        return null;

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

    public void onActionAfterSelectHospitalFromInBoxZone() {
        if (hcode != null && !hcode.isEmpty()) {
            showSearchHospitalDialog();
        }
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

    public void showCompareTmtDialog() {
        Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        searchName = requestParameters.get("search");
        searchType = requestParameters.get("typeOfSearch");

        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 600);
        options.put("contentWidth", 1024);
        Map<String, List<String>> params = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        keywords.add(searchName);
        params.put("search", keywords);
        keywords = new ArrayList<>();
        keywords.add(searchType);
        params.put("typeOfSearch", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/common/drug/findTMTDialog", options, params);
    }

    public void searchHospitalDialogReturn(SelectEvent event) {
        selectedHospital = (Hospital) event.getObject();
        if (selectedHospital != null) {
            hcode = selectedHospital.getFullName();
            search();
        }
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

    public void search() {
        if (selectedHospital == null) {
            return;
        }
        requestItemHoldersNullTMT.clear();
        requestItems = new SpringDataLazyDataModelSupport<RequestItem>() {
            @Override
            public Page<RequestItem> load(Pageable pageAble) {
                Page<RequestItem> result = requestItemService.findByStatusAndHcodeAndTmtIdIsNull(RequestItem.Status.REQUEST, selectedHospital.getHcode(), pageAble);
                setApproveSelected(result);
                totalElements = result.getTotalElements();
                displayElement = result.getSize();
                return result;
            }
        };
        requestItemHoldersNullTMT.add(requestItems);
    }

    public void searchByKeyword() {
        final List<String> keywords = Splitter.on(CharMatcher.WHITESPACE).omitEmptyStrings().trimResults().splitToList(keyword);
        final Specifications<RequestItem> spec = spec().and(advanceSearchSpec(keywords));
        requestItems = new SpringDataLazyDataModelSupport<RequestItem>() {
            @Override
            public Page<RequestItem> load(Pageable pageAble) {

                Page<RequestItem> items = requestItemRepo.findAll(spec, pageAble);
                setApproveSelected(items);
                totalElements = items.getTotalElements();
                displayElement = items.getSize();
                return items;
            }
        };
        requestItemHoldersNullTMT.clear();
        requestItemHoldersNullTMT.add(requestItems);
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

    private void clearAll() {
        requestItemHoldersNullTMT.clear();
        notApproveRequests.clear();
        approveRequests.clear();
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

    public void selectRequestItem(RequestItem item) {
        this.editMessageRequestItem = item;
        messageOfRequestItem = editMessageRequestItem.getMessage();
    }

    public void error(RequestItem requestItem, String columnName) {
        requestItem.getErrorColumns().add(columnName);
        requestItem.setStatus(RequestItem.Status.REJECT);
        if (!notApproveRequests.contains(requestItem)) {
            notApproveRequests.add(requestItem);
        }
    }

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public boolean isNullTmt() {
        return nullTmt;
    }

    public void setNullTmt(boolean nullTmt) {
        this.nullTmt = nullTmt;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public Hospital getSelectedHospital() {
        return selectedHospital;
    }

    public void setSelectedHospital(Hospital selectedHospital) {
        this.selectedHospital = selectedHospital;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public UploadHospitalDrugItem getUploadDrugItem() {
        return uploadDrugItem;
    }

    public void setUploadDrugItem(UploadHospitalDrugItem uploadDrugItem) {
        this.uploadDrugItem = uploadDrugItem;
    }

    public SpringDataLazyDataModelSupport<RequestItem> getRequestItems() {
        return requestItems;
    }

    public void setRequestItems(SpringDataLazyDataModelSupport<RequestItem> requestItems) {
        this.requestItems = requestItems;
    }

    public List<SpringDataLazyDataModelSupport<RequestItem>> getRequestItemHoldersNullTMT() {
        return requestItemHoldersNullTMT;
    }

    public void setRequestItemHoldersNullTMT(List<SpringDataLazyDataModelSupport<RequestItem>> requestItemHoldersNullTMT) {
        this.requestItemHoldersNullTMT = requestItemHoldersNullTMT;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
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

    public RequestItem getEditMessageRequestItem() {
        return editMessageRequestItem;
    }

    public void setEditMessageRequestItem(RequestItem editMessageRequestItem) {
        this.editMessageRequestItem = editMessageRequestItem;
    }

    public String getMessageOfRequestItem() {
        return messageOfRequestItem;
    }

    public void setMessageOfRequestItem(String messageOfRequestItem) {
        this.messageOfRequestItem = messageOfRequestItem;
    }
    
}
