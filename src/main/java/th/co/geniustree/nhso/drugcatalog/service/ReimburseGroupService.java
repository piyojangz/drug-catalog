/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;

/**
 *
 * @author Thanthathon
 */
public interface ReimburseGroupService {

    public ReimburseGroup save(String id, String name, boolean isSpecialProject);

    public ReimburseGroup findById(String id);
}
