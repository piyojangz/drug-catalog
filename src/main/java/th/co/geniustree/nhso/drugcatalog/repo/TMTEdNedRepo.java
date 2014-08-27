/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.TMTEdNedPK;

/**
 *
 * @author moth
 */
public interface TMTEdNedRepo extends JpaRepository<TMTEdNed, TMTEdNedPK>{
    
}
