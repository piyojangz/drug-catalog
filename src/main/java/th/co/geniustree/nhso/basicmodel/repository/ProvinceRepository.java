/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.basicmodel.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;

/**
 *
 * @author pramoth
 */
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public interface ProvinceRepository extends JpaRepository<Province, String> {

    @Query("SELECT prov FROM Province prov WHERE prov.nhsoZone = ?1")
    public List<Province> findByZone(Zone zone);
    @Query("SELECT prov FROM Province prov WHERE prov.nhsoZone.nhsoZone = ?1")
    public List<Province> findByNhsoZone(String nhsoZone,Sort sort);

    public Province findById(String id);
}
