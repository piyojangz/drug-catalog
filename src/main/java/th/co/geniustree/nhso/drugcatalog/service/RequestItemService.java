/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;

/**
 *
 * @author moth
 */
public interface RequestItemService {
    public void generateRequest(UploadHospitalDrugItem uploadItem);
    public void generateAll();
    public List<RequestItem> findAllByStatusAndTmtId(RequestItem.Status status, String tmtId);
}
