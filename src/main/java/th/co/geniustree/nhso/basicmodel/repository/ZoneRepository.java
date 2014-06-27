/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.basicmodel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;

/**
 *
 * @author pramoth
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface ZoneRepository extends JpaRepository<Zone, String> {

    public Zone findByNhsoZone(String nhsoZone);

    @Query("SELECT COUNT(zne.nhsoZone) FROM Zone zne WHERE zne.nhsoZone = ?1")
    public long countByNshoZone(String nshoZone);
}
