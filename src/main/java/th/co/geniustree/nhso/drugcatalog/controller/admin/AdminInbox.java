/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class AdminInbox implements Serializable {

    @Autowired
    private RequestItemRepo requestItemRepo;
    private SpringDataLazyDataModelSupport<RequestItem> requestItems;

    @PostConstruct
    public void postConstruct() {
        requestItems = new SpringDataLazyDataModelSupport<RequestItem>(){

            @Override
            public Page<RequestItem> load(Pageable pageAble) {
                return requestItemRepo.findByStatus(RequestItem.Status.REQUEST,pageAble);
            }
            
        }; 
    }
    public SpringDataLazyDataModelSupport<RequestItem> getRequestItems() {
        return requestItems;
    }
}
