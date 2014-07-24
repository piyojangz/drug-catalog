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
        //TODO makeit testable.
        Collections.sort(items, new Comparator<UploadHospitalDrugItem>() {

            @Override
            public int compare(UploadHospitalDrugItem o1, UploadHospitalDrugItem o2) {
                Date date1;
                Date date2;
                if ("U".equalsIgnoreCase(o1.getUpdateFlag())) {
                    date1 = o1.getDateUpdateDate();
                } else {
                    date1 = o1.getDateChangeDate();
                }
                if ("U".equalsIgnoreCase(o1.getUpdateFlag())) {
                    date2 = o2.getDateUpdateDate();
                } else {
                    date2 = o2.getDateChangeDate();
                }
                return date1.compareTo(date2);
            }
        });
        items.addAll(0, flagA);
        items.addAll(flagDelete);
    }
}
