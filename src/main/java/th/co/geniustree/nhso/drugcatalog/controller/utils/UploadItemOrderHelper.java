/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;

/**
 *
 * @author moth
 */
public class UploadItemOrderHelper {

    public static void reorderByUpdateFlageAFirstDeleteLast(List<UploadHospitalDrugItem> items) {
        List<UploadHospitalDrugItem> flagA = new ArrayList<>();
        List<UploadHospitalDrugItem> flagDelete = new ArrayList<>();
        ListIterator<UploadHospitalDrugItem> listIterator = items.listIterator();
        while (listIterator.hasNext()) {
            UploadHospitalDrugItem next = listIterator.next();
            if ("A".equalsIgnoreCase(next.getUpdateFlag())) {
                flagA.add(next);
                listIterator.remove();
            } else if ("D".equalsIgnoreCase(next.getUpdateFlag())) {
                flagDelete.add(next);
                listIterator.remove();
            }
        }
        Collections.sort(items, new Comparator<UploadHospitalDrugItem>() {

            @Override
            public int compare(UploadHospitalDrugItem o1, UploadHospitalDrugItem o2) {
                return o1.getDateEffectiveDate().compareTo(o2.getDateEffectiveDate());
            }
        });
        items.addAll(0, flagA);
        items.addAll(flagDelete);
    }
}
