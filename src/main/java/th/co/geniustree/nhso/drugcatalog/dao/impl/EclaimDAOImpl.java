/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.dao.impl;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import th.co.geniustree.nhso.drugcatalog.dao.EclaimDAO;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredFunctionCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLrecord;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EclaimDAOImpl implements EclaimDAO {

    private static final String TYPE_NAME = "HOSPITALDRUG";
    private static final String COMPATIBLE_TYPE = "HOSPITALDRUG";
    private static final String PROCEDURE_NAME = "HOSPITALDRUG_PACK.find_hospdrug_withtmt";

    @PersistenceContext
    private EntityManager em;

    @Override
    public HospitalDrugType loadDrugInfo(String hospDrugCode, String hcode, String tmtid, Date dateEffective) {
        PLSQLrecord record = new PLSQLrecord();

        record.setTypeName(TYPE_NAME);
        record.setCompatibleType(COMPATIBLE_TYPE);
        record.setJavaType(HospitalDrugType.class);
        record.addField("tmtid", JDBCTypes.VARCHAR_TYPE);
        record.addField("tmt_type", JDBCTypes.VARCHAR_TYPE);
        record.addField("fsn", JDBCTypes.VARCHAR_TYPE);
        record.addField("manufacturer", JDBCTypes.VARCHAR_TYPE);
        record.addField("hosp_genericName", JDBCTypes.VARCHAR_TYPE);
        record.addField("hosp_tradeName", JDBCTypes.VARCHAR_TYPE);
        record.addField("unit_price", JDBCTypes.NUMERIC_TYPE);
        record.addField("unitprice", JDBCTypes.NUMERIC_TYPE);
        record.addField("SPECPREP", JDBCTypes.VARCHAR_TYPE);
        record.addField("is_ed", JDBCTypes.VARCHAR_TYPE);
        record.addField("ndc24", JDBCTypes.VARCHAR_TYPE);
        record.addField("deleted", JDBCTypes.VARCHAR_TYPE);
        record.addField("approved", JDBCTypes.VARCHAR_TYPE);
        record.addField("productcat", JDBCTypes.VARCHAR_TYPE);
        record.addField("TMT_DOSAGEFORM", JDBCTypes.VARCHAR_TYPE);
        record.addField("DOSAGEFORM_GROUP", JDBCTypes.VARCHAR_TYPE);
        record.addField("REIMB_UNIT_PRICE", JDBCTypes.NUMERIC_TYPE);
        record.addField("content", JDBCTypes.VARCHAR_TYPE);
        record.addField("ISED_STATUS", JDBCTypes.VARCHAR_TYPE);
        record.addField("drggroup", JDBCTypes.ARRAY_TYPE);

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall(record);
        call.addNamedArgument("p_hospdrugcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_hcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_tmtid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_date", JDBCTypes.DATE_TYPE);
        call.setProcedureName(PROCEDURE_NAME);
        DataReadQuery databaseQuery = new DataReadQuery(call);
        JpaEntityManager jem = (JpaEntityManager) em.getDelegate();
        DatabaseRecord result = (DatabaseRecord) jem.createQuery(databaseQuery)
                .setParameter("p_hospdrugcode", hospDrugCode)
                .setParameter("p_hcode", hcode)
                .setParameter("p_tmtid", tmtid)
                .setParameter("p_date", dateEffective).getSingleResult();
        Struct drug = (Struct) result.get("RESULT");
        return mappedToModel(drug);

    }

    private HospitalDrugType mappedToModel(Struct struct) {
        HospitalDrugType drug = new HospitalDrugType();
        try {
            Object[] objs = struct.getAttributes();

            drug.setTmtid((String) objs[0]);
            drug.setTmt_type((String) objs[1]);
            drug.setFsn((String) objs[2]);
            drug.setManufacturer((String) objs[3]);
            drug.setHosp_genericName((String) objs[4]);
            drug.setHosp_tradeName((String) objs[5]);
            drug.setUnit_price((BigDecimal) objs[6]);
            drug.setUnitprice((BigDecimal) objs[7]);
            drug.setSPECPREP((String) objs[8]);
            drug.setIs_ed((String) objs[9]);
            drug.setNdc24((String) objs[10]);
            drug.setDeleted((String) objs[11]);
            drug.setApproved((String) objs[12]);
            drug.setProductcat((String) objs[13]);
            drug.setTMT_DOSAGEFORM((String) objs[14]);
            drug.setDOSAGEFORM_GROUP((String) objs[15]);
            drug.setREIMB_UNIT_PRICE((BigDecimal) objs[16]);
            drug.setContent((String) objs[17]);
            drug.setISED_STATUS((String) objs[18]);
            drug.setDrggroup((Array) objs[19]);
        } catch (SQLException sqlEx) {
            return new HospitalDrugType();
        }
        return drug;
    }

    @Override
    public List<String> getDrugGroupsFrom(HospitalDrugType drug) {
        List<String> groups = new ArrayList<>();
        try {
            ResultSet rs = drug.getDrggroup().getResultSet();
            while (rs.next()) {
                String group = rs.getString(2);
                groups.add(group);
            }

        } catch (SQLException ex) {
        }
        return groups;
    }
}
