/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import java.util.Set;
import th.co.geniustree.nhso.drugcatalog.controller.admin.ApproveData;
import th.co.geniustree.nhso.drugcatalog.model.ApproveFile;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;

/**
 *
 * @author moth
 */
public interface ApproveService {

    public void approve(RequestItem item, String approveUser);

    public void approve(List<RequestItem> requestItems, String User);

    public void reject(RequestItem requestItem, String rejectUser);

    public void approveOrReject(List<RequestItem> items);

    public void approveOrReject(String hcode, String hospDrug, String tmt, boolean approve, Set<String> errorColumns, String userPid);

    public void approveOrRejects(List<ApproveData> datas);

    public void approveOrRejects(List<ApproveData> datas, ApproveFile approveFile);

    public void reApproveAndNotChangeRequestItemState(List<RequestItem> requestItems);

}
