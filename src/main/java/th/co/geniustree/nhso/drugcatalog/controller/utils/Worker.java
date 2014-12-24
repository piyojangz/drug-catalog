/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

/**
 *
 * @author moth
 */
public class Worker {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Worker.class);

    public Object run() throws Exception {
        return null;
    }

    public void completed(Object result) {
        if (result != null) {
            FacesMessageUtils.info(result.toString());
        }
    }

    public void fail(Exception e) {
        FacesMessageUtils.error(e);
        log.error(e.getMessage(), e);
    }
}
