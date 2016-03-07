/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input;

/**
 *
 * @author Thanthathon
 */
public interface UploadRelationshipModel {

    public String getParentTmtId();

    public void setParentTmtId(String parentTmtId);

    public String getChildTmtId();

    public void setChildTmtId(String childTmtId);
}
