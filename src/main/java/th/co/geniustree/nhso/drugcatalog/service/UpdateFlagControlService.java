/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

/**
 *
 * @author thanthathon.b
 */
public interface UpdateFlagControlService<T> {
    
    /**
     * Validate รายการยาที่เป็น Flag A
     * รายการยานี้ (HCODE + HOSPDRUGCODE) ต้องไม่มีในฐานข้อมูล หรือมีแต่ ต้องมีสถานะเป็น REJECT เท่านั้น และห้ามมี Flag อื่นเข้ามาก่อน 
     * @param item
     * @param addError set true for add error
     * @return true if valid
     */
    public boolean validateFlagA(T item, boolean addError);
    
    /**
     * Validate รายการยาที่เป็น Flag E หรือ Flag U
     * <ul>
     * <li>รายการยานี้ (HCODE + HOSPDRUGCODE) ต้องพบ Flag A ที่ ACCEPT และ ยังไม่ถูกลบ</li>
     * <li>ห้ามมีข้อมูล (HCODE, HOSPDRUGCODE, TMTID, DATEEFFECTIVE, UPDATEFLAG) ซ้ำกันใน flag นั้นๆ</li>
     * </ul>
     * @param item
     * @param addError set true for add error
     * @return true if valid
     */
    public boolean validateFlagEU(T item, boolean addError);
    
    /**
     * Validate รายการยาที่เป็น Flag D
     * <ul>
     * <li>รายการยานี้ (HCODE + HOSPDRUGCODE) ต้องพบ Flag A ที่ ACCEPT และ ยังไม่ถูกลบ</li>
     * <li>dateEffective ที่ส่งมานั้น ต้องไม่มาก่อนหรือวันเดียวกันกับ dateEffective ของFlag A</li>
     * <li>ห้ามใช้ dateeffective ซ้ำกับวันอื่น</li>
     * </ul>
     * @param item
     * @param addError set true for add error
     * @return true if valid
     */
    public boolean validateFlagD(T item, boolean addError);
}
