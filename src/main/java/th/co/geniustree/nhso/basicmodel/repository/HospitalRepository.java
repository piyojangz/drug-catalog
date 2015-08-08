/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.basicmodel.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.readonly.Province;

/**
 *
 * @author pramoth
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface HospitalRepository extends JpaRepository<Hospital, String> {

    @Query("select h from Hospital h where  h.province = ?1")
    public Page<Hospital> findByProvince(Province province, Pageable pageable);
    
    @Query("select h from Hospital h where  h.province.id = ?1")
    public Page<Hospital> findByProvince(String province, Pageable pageable);

    @Query("select h from Hospital h where  h.hname like ?1")
    public Page<Hospital> findLikeHname(String hname, Pageable pageable);

    @Query("select h from Hospital h where  h.hcode like ?1")
    public Page<Hospital> findLikeHcode(String hcode, Pageable pageable);

    @Query("select h from Hospital h where  h.hcode = ?1")
    public Hospital findByHcode(String hcode);

    @Query("select h from Hospital h where  h.hcode like ?1 or h.hname like ?2")
    Page<Hospital> findLikeHcodeOrLikeHname(String hcode, String hname, Pageable pageable);

    @Query("select h from Hospital h where  (h.hcode like ?1 or h.hname like ?2) and h.province in ?3")
    Page<Hospital> findLikeHcodeOrLikeHnameAndProvince(String hcode, String hname, List<Province> province, Pageable pageable);

    @Query("select h.hcode from Hospital h where h.statusUC ='1' or h.statusUC='4'")
    public Page<String> findAllHcodeWithStatusUCOneAndFour(Pageable pageable);
}
