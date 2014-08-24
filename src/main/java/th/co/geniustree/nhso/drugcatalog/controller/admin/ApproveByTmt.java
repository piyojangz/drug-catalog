/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class ApproveByTmt implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ApproveByTmt.class);
    private static final RequestItem.Status[] STATUS = new RequestItem.Status[]{RequestItem.Status.ACCEPT, RequestItem.Status.REJECT};
    private String selectTmtId;
    private TMTDrug tmtDrug;
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    @Autowired
    private ApproveService approveService;
    private List<RequestItem> request;
    private List<RequestItem> approveRequests = new ArrayList<>();
    private List<RequestItem> notApproveRequests = new ArrayList<>();
    private String notApproveMessage;

    @PostConstruct
    public void postConstruct() {

    }

    public String getSelectTmtId() {
        return selectTmtId;
    }

    public void setSelectTmtId(String selectTmtId) {
        this.selectTmtId = selectTmtId;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public List<RequestItem> getRequest() {
        return request;
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

    public String getNotApproveMessage() {
        return notApproveMessage;
    }

    public void setNotApproveMessage(String notApproveMessage) {
        this.notApproveMessage = notApproveMessage;
    }

    public void approve(ValueChangeEvent event) {
        UIComponent component = event.getComponent();
        RequestItem item = (RequestItem) component.getAttributes().get("selectedItem");
        if (event.getNewValue() != null) {
            item.setStatus(RequestItem.Status.valueOf(event.getNewValue().toString()));
            if (item.getStatus() == RequestItem.Status.ACCEPT) {
                approveRequests.add(item);
                notApproveRequests.remove(item);
            } else {
                notApproveRequests.add(item);
                approveRequests.remove(item);
            }
        }
    }

    public void remove(RequestItem item) {
        notApproveRequests.remove(item);
        approveRequests.remove(item);
        item.setStatus(RequestItem.Status.REQUEST);
    }

    public String save() {
        assignNotApproveMassage();
        List<RequestItem> merge = new ArrayList<>(approveRequests);
        merge.addAll(notApproveRequests);
        approveService.approveOrReject(merge);
        notApproveRequests.clear();
        approveRequests.clear();
        //TODO send mail to each HCODE
        load();
        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        return "inbox-groupby-tmt";
    }

    public void load() {
        tmtDrug = tmtDrugRepo.findOne(selectTmtId);
        request = requestItemRepo.findByStatusAndTmtId(RequestItem.Status.REQUEST, selectTmtId);
    }

    private void assignNotApproveMassage() {
        for (RequestItem item : notApproveRequests) {
            if (Strings.isNullOrEmpty(item.getMessage())) {
                item.setMessage(notApproveMessage);
            }
        }
    }

    public static class SelectableRequestItemModel extends SpringDataLazyDataModelSupport<RequestItem> {

        @Override
        public Object getRowKey(Object object) {
            RequestItem requestItem = (RequestItem) object;
            return requestItem.getId().toString();
        }

        @Override
        public RequestItem getRowData(String rowKey) {
            List<RequestItem> wrappedData = (List<RequestItem>) getWrappedData();
            for (RequestItem item : wrappedData) {
                if (item.getId().toString().equals(rowKey)) {
                    return item;
                }
            }
            return null;
        }
    }
}
