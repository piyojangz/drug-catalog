/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.service;

import th.co.geniustree.nhso.drugcatalog.model.request.RequestItem;

/**
 *
 * @author moth
 */
public interface ApproveService {
    public void approve(RequestItem item);

    public void reject(RequestItem requestItem);
}
