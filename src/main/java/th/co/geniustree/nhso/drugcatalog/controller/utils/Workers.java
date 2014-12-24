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
public class Workers {

    public static void execute(Worker worker) {
        try {
            worker.completed(worker.run());
        } catch (Exception ex) {
            worker.fail(ex);
        }
    }
}
