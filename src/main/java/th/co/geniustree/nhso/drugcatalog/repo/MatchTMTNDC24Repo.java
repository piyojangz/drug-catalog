/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.MatchTMTNDC24;
import th.co.geniustree.nhso.drugcatalog.model.MatchTMTNDC24ID;

/**
 *
 * @author Thanthathon
 */
public interface MatchTMTNDC24Repo extends JpaRepository<MatchTMTNDC24, MatchTMTNDC24ID> {

    public Page<MatchTMTNDC24> findByTmtidContains(String tmtid, Pageable pageable);

    public Page<MatchTMTNDC24> findByNdc24Contains(String ndc24, Pageable pageable);

    public Page<MatchTMTNDC24> findByTmtidContainsOrNdc24Contains(String tmtid, String ndc24, Pageable pageable);
}
