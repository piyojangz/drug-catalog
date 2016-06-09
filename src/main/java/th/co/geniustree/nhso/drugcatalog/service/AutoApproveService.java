/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

/**
 *
 * @author moth
 */
public interface AutoApproveService {

    public void approveRejectAndEditCountGreaterThanZero();

    public void approveRequestWhichTMTisNull();

    public void approveRequestWhichCreateOneline();

    public void approveByHcode(String hcode);

    public boolean approvePartial(int page, int pageSize);

    public void approveByRequestFlag(String flag, String approveUser);

}
