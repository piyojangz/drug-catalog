/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  thanthathon.b
 * Created: Nov 28, 2016
 */
create or replace PACKAGE BODY HOSPITALDRUG_PACK_SA AS
  ------------------------------------------------------------------------------------------------
  FUNCTION find_deleted(p_hospdrugcode VARCHAR2,
                        p_hcode        VARCHAR2,
                        p_tmtid        VARCHAR2,
                        p_date         DATE) RETURN VARCHAR2 AS
    v_deleted VARCHAR2(1);
    err_num   NUMBER;
    err_msg   VARCHAR2(1000);
  BEGIN
    SELECT CASE
             WHEN updateflag = 'D' THEN
              'Y'
             ELSE
              'N'
           END deleted
      INTO v_deleted
      FROM (SELECT item.*, parent.hcode
              FROM TMT_UPLOADHOSPDRUG_ITEM item
             INNER JOIN TMT_UPLOADHOSPDRUG parent
                ON (item.UPLOADHOSPDRUG_ID = parent.id)
             INNER JOIN TMT_REQUEST_ITEM req
                ON item.id = req.id
             WHERE parent.hcode = p_hcode
               AND req.STATUS = 'ACCEPT'
               AND item.hospdrugcode = p_hospdrugcode
               AND nvl(item.TMTID,'NULLID') = nvl(p_tmtid,'NULLID')
               AND TRUNC(to_date(TO_CHAR(item.DATEEFFECTIVEDATE,
                                         'DD-MM-YYYY'),
                                 'DD-MM-YYYY')) <= p_date
             ORDER BY item.DATEEFFECTIVEDATE DESC, item.UPDATEFLAG DESC)
     WHERE rownum = 1;
    RETURN v_deleted;
  EXCEPTION
    WHEN OTHERS THEN
      err_num := SQLCODE;
      err_msg := SUBSTR(SQLERRM, 1, 100);
      begin
        INSERT INTO TMT_HOSPITALDRUG_PCK_LOG
          (HCODE,
           HOSPDRUGCODE,
           TMTID,
           DATESERV,
           PROCESS_NAME,
           SQLCODE,
           ERROR_MSG,
           RUN_DATE)
        VALUES
          (p_hcode,
           p_hospdrugcode,
           null,
           p_date,
           'find_deleted',
           err_num,
           err_msg,
           sysdate);
      exception
        when others then
          null;
      end;
      RETURN NULL;
  END;
  ------------------------------------------------------------------------------------------------
  FUNCTION find_status_ed(v_current_tmt  VARCHAR2,
                          p_date         DATE) RETURN VARCHAR2 AS
    v_status_ed VARCHAR2(2);
  BEGIN
    SELECT status_ed
      INTO v_status_ed
      FROM (SELECT *
              FROM tmt_tmtedned ed
             WHERE ed.tmtid = v_current_tmt
               AND TRUNC(ed.datein) <= p_date
             ORDER BY ed.DATEIN DESC)
     WHERE rownum = 1;
    RETURN v_status_ed;
  EXCEPTION
    WHEN OTHERS THEN
      RETURN NULL;
  END find_status_ed;
  ------------------------------------------------------------------------------------------------ 
  FUNCTION is_drug_exist(p_hospdrugcode VARCHAR2, p_hcode VARCHAR2,p_tmtid VARCHAR2)
    RETURN NUMBER AS
    l_exist NUMBER := 0;
  BEGIN
    BEGIN
      SELECT CASE
                WHEN COUNT(*) > 0 THEN
                  1
                ELSE
                  0
              END exist
      INTO l_exist
      FROM TMT_HOSPDRUG_TRANS
      WHERE HCODE = p_hcode
      AND HOSPDRUGCODE = p_hospdrugcode
      AND nvl(TMTID,'NULLID') = nvl(p_tmtid,'NULLID');
    EXCEPTION
      WHEN OTHERS THEN
        NULL;
    END;
    RETURN l_exist;
  END is_drug_exist;
  ------------------------------------------------------------------------------------------------
  FUNCTION find_reimburse_price(p_tmtid VARCHAR2, p_date DATE) RETURN NUMBER AS
    v_reimburse_price NUMBER;
    err_num           NUMBER;
    err_msg           VARCHAR2(1000);
  BEGIN
    SELECT price
      INTO v_reimburse_price
      FROM (SELECT *
              FROM tmt_reimburse_price hprice
             WHERE hprice.tmtid = p_tmtid
               AND TRUNC(hprice.EFFECTIVE_DATE) <= p_date
             ORDER BY hprice.EFFECTIVE_DATE DESC, hprice.PRICE DESC)
     WHERE rownum = 1;
    RETURN v_reimburse_price;
  EXCEPTION
    WHEN OTHERS THEN
      err_num := SQLCODE;
      err_msg := SUBSTR(SQLERRM, 1, 100);
      begin
        INSERT INTO TMT_HOSPITALDRUG_PCK_LOG
          (HCODE,
           HOSPDRUGCODE,
           TMTID,
           DATESERV,
           PROCESS_NAME,
           SQLCODE,
           ERROR_MSG,
           RUN_DATE)
        VALUES
          (null,
           null,
           p_tmtid,
           p_date,
           'find_reimburse_price',
           err_num,
           err_msg,
           sysdate);
      exception
        when others then
          null;
      end;
      RETURN NULL;
  END find_reimburse_price;
  ------------------------------------------------------------------------------------------------
  FUNCTION find_reimburse_price_tp(p_hcode        VARCHAR2,
                                   p_hospdrugcode VARCHAR2,
                                   p_tmtid        VARCHAR2,
                                   p_date         DATE)
    RETURN REIMBURSE_PRICE_TP AS
    v_reimburse REIMBURSE_PRICE_TP := REIMBURSE_PRICE_TP(NULL, NULL, NULL);
    err_num     NUMBER;
    err_msg     VARCHAR2(1000);
  BEGIN
    SELECT price, content_cr
      INTO v_reimburse.price, v_reimburse.content_cr
      FROM (SELECT CONTENT_CR, PRICE
              FROM TMT_REIMBURSE_PRICE_TP hprice
             WHERE hprice.HCODE = p_hcode
               AND hprice.tmtid = p_tmtid
               AND hprice.HOSPDRUGCODE = p_hospdrugcode
               AND TRUNC(hprice.EFFECTIVE_DATE) <= p_date
             ORDER BY hprice.EFFECTIVE_DATE DESC)
     WHERE rownum = 1;
    RETURN v_reimburse;
  EXCEPTION
    WHEN OTHERS THEN
      err_num := SQLCODE;
      err_msg := SUBSTR(SQLERRM, 1, 100);
      begin
        INSERT INTO TMT_HOSPITALDRUG_PCK_LOG
          (HCODE,
           HOSPDRUGCODE,
           TMTID,
           DATESERV,
           PROCESS_NAME,
           SQLCODE,
           ERROR_MSG,
           RUN_DATE)
        VALUES
          (p_hcode,
           p_hospdrugcode,
           p_tmtid,
           p_date,
           'find_reimburse_price_tp',
           err_num,
           err_msg,
           sysdate);
      exception
        when others then
          null;
      end;
      RETURN NULL;
  END find_reimburse_price_tp;
  ------------------------------------------------------------------------------------------------
  FUNCTION find_drug_attr(p_hcode        VARCHAR2,
                          p_hospdrugcode VARCHAR2,
                          p_tmtid        VARCHAR2,
                          p_date         DATE) RETURN DRUGATTR AS
    v_drug  DRUGATTR := DRUGATTR(NULL,
                                 NULL,
                                 NULL,
                                 NULL,
                                 NULL,
                                 NULL,
                                 NULL,
                                 NULL,
                                 NULL);
    err_num NUMBER;
    err_msg VARCHAR2(1000);
  BEGIN
    FOR rcd IN (SELECT h.* 
                FROM TMT_HOSPDRUG_TRANS h 
                WHERE h.HCODE = p_hcode
                AND h.HOSPDRUGCODE = p_hospdrugcode
                AND nvl(h.TMTID,'NULLID') = nvl(p_tmtid,'NULLID')
                AND TRUNC(h.DATEEFFECTIVE) <= p_date
                ORDER BY trunc(h.DATEEFFECTIVE) DESC , h.updateflag DESC)
    LOOP
    /* ----------------------------------------------
        Merge data between flag U and flag E ( if not found hospdrug, then get data from flag A )
    */ ----------------------------------------------
      IF rcd.UPDATEFLAG = 'U' THEN
        IF v_drug.unit_price IS NULL THEN 
          v_drug.unit_price := rcd.UNITPRICE;
        END IF;
      ELSIF rcd.UPDATEFLAG = 'E' THEN
        IF v_drug.ised IS NULL THEN
          v_drug.manufacturer := rcd.manufacturer;
          v_drug.hosp_genericName := rcd.GENERICNAME;
          v_drug.hosp_tradeName := rcd.TRADENAME;
          v_drug.specprep := rcd.specprep;
          v_drug.productcat := rcd.productcat;
          v_drug.ised := rcd.ised;
          v_drug.ised_status := rcd.ised_status;
          v_drug.content := rcd.content;
          END IF;
      ELSIF rcd.UPDATEFLAG = 'A' THEN
        IF v_drug.unit_price IS NULL THEN
          v_drug.unit_price := rcd.UNITPRICE;
        END IF;
        IF v_drug.ised IS NULL THEN
          v_drug.manufacturer := rcd.manufacturer;
          v_drug.hosp_genericName := rcd.GENERICNAME;
          v_drug.hosp_tradeName := rcd.TRADENAME;
          v_drug.specprep := rcd.specprep;
          v_drug.productcat := rcd.productcat;
          v_drug.ised := rcd.ised;
          v_drug.ised_status := rcd.ised_status;
          v_drug.content := rcd.content;
        END IF;
        EXIT;
      ELSIF rcd.UPDATEFLAG = 'D' THEN
        EXIT;
      END IF;
--      EXIT WHEN rcd.UPDATEFLAG = 'A' or rcd.UPDATEFLAG = 'D';
    END LOOP;
    RETURN v_drug;
    
  EXCEPTION
    WHEN OTHERS THEN
      err_num := SQLCODE;
      err_msg := SUBSTR(SQLERRM, 1, 100);
      begin
        INSERT INTO TMT_HOSPITALDRUG_PCK_LOG
          (HCODE,
           HOSPDRUGCODE,
           TMTID,
           DATESERV,
           PROCESS_NAME,
           SQLCODE,
           ERROR_MSG,
           RUN_DATE)
        VALUES
          (p_hcode,
           p_hospdrugcode,
           p_tmtid,
           p_date,
           'find_drug_attr',
           err_num,
           err_msg,
           sysdate);
      exception
        when others then
          null;
      end;
      /**DBMS_OUTPUT.PUT_LINE (DBMS_UTILITY.FORMAT_CALL_STACK);--by Suwatchai**/
      v_drug.manufacturer     := NULL;
      v_drug.hosp_genericName := NULL;
      v_drug.hosp_tradeName   := NULL;
      v_drug.specprep         := NULL;
      v_drug.productcat       := NULL;
      v_drug.ised             := NULL;
      v_drug.ised_status      := NULL;
      v_drug.unit_price       := NULL;
      v_drug.content          := NULL;
      RETURN v_drug;
  END find_drug_attr;
  ------------------------------------------------------------------------------------------------
  FUNCTION find_hospdrug_withtmt(p_hospdrugcode VARCHAR2,
                                 p_hcode        VARCHAR2,
                                 p_tmtid        VARCHAR2,
                                 p_date         DATE) RETURN hospitaldrug AS
    v_hdrug                hospitaldrug := hospitaldrug(NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        NULL,
                                                        druggroup());
    v_unit_price           NUMBER := NULL;
    v_status_ed            VARCHAR2(2) := NULL;
    v_tmtid                VARCHAR2(6) := NULL;
    v_tmt_type             VARCHAR2(6) := NULL;
    v_tmt_dosageform       VARCHAR2(255) := NULL;
    v_tmt_dosageform_group VARCHAR2(255) := NULL;
    v_fsn                  NVARCHAR2(1000) := NULL;
    v_deleted              VARCHAR2(1) := NULL;
    v_current_tmt          VARCHAR2(6) := NULL;
    v_reimburse_price      NUMBER;
    v_reimburse_price_tp   REIMBURSE_PRICE_TP;
    v_drugattr             DRUGATTR;
    err_num                NUMBER;
    err_msg                VARCHAR2(1000);
  BEGIN
    FOR rcd IN (SELECT hdrg.tmtid,
                       hdrg.ised is_ed,
                       CASE
                         WHEN hdrg.APPROVED = 0 THEN
                          'N'
                         ELSE
                          'Y'
                       END APPROVED,
                       hdrg.ndc24 ndc24
                  FROM tmt_hospdrug_trans hdrg
                 WHERE hdrg.HCODE = p_hcode
                   AND hdrg.HOSPDRUGCODE = p_hospdrugcode
                   and nvl(hdrg.TMTID,'NULLID') = nvl(p_tmtid,'NULLID')) LOOP
      -- find drug
      BEGIN
        SELECT drg.tmtid,
               drg.type tmt_type,
               drg.fsn,
               drg.dosageform,
               drg.dosageform_group
          INTO v_tmtid,
               v_tmt_type,
               v_fsn,
               v_tmt_dosageform,
               v_tmt_dosageform_group
          FROM tmt_drug drg
         WHERE drg.tmtid = p_tmtid;
      EXCEPTION
        WHEN OTHERS THEN
          err_num := SQLCODE;
          err_msg := SUBSTR(SQLERRM, 1, 100);
          begin
            INSERT INTO TMT_HOSPITALDRUG_PCK_LOG
              (HCODE,
               HOSPDRUGCODE,
               TMTID,
               DATESERV,
               PROCESS_NAME,
               SQLCODE,
               ERROR_MSG,
               RUN_DATE)
            VALUES
              (p_hcode,
               p_hospdrugcode,
               p_tmtid,
               p_date,
               'find_hospdrug_withtmt',
               err_num,
               err_msg,
               sysdate);
          exception
            when others then
              null;
          end;
          v_tmtid          := NULL;
          v_tmt_type       := NULL;
          v_fsn            := NULL;
          v_tmt_dosageform := NULL;
      END;
      v_deleted                := find_deleted(p_hospdrugcode,
                                               p_hcode,
                                               p_tmtid,
                                               p_date);
      v_drugattr               := find_drug_attr(p_hcode,
                                                 p_hospdrugcode,
                                                 p_tmtid,
                                                 p_date);
      v_hdrug.content          := v_drugattr.content;
      v_reimburse_price        := find_reimburse_price(v_tmtid, p_date);
      v_hdrug.REIMB_UNIT_PRICE := v_reimburse_price;
      IF v_reimburse_price IS NULL THEN
        v_reimburse_price_tp     := find_reimburse_price_tp(p_hcode,
                                                            p_hospdrugcode,
                                                            v_tmtid,
                                                            p_date);
        v_hdrug.REIMB_UNIT_PRICE := v_reimburse_price_tp.price;
        v_hdrug.content          := v_reimburse_price_tp.content_cr;
      END IF;
      v_hdrug.tmtid            := v_tmtid;
      v_hdrug.tmt_type         := v_tmt_type;
      v_hdrug.fsn              := v_fsn;
      v_hdrug.manufacturer     := v_drugattr.manufacturer;
      v_hdrug.hosp_genericName := v_drugattr.hosp_genericName;
      v_hdrug.hosp_tradeName   := v_drugattr.hosp_tradeName;
      v_hdrug.unit_price       := v_drugattr.unit_price;
      v_hdrug.specprep         := v_drugattr.specprep;
      v_hdrug.is_ed            := v_drugattr.ised;
      v_hdrug.ISED_STATUS      := v_drugattr.ised_status;
      v_hdrug.ndc24            := rcd.ndc24;
      v_hdrug.deleted          := v_deleted;
      v_hdrug.APPROVED         := rcd.APPROVED;
      v_hdrug.productcat       := v_drugattr.productcat;
      v_hdrug.tmt_dosageform   := v_tmt_dosageform_group; --tmt_dosageform also return dosageform_group too
      v_hdrug.dosageform_group := v_tmt_dosageform_group;
    END LOOP;
    -- find group
    FOR rs IN (SELECT druggroup_name
                 FROM TMT_DRUGGROUPITEM item
                WHERE tmtid = v_tmtid
                  AND p_date BETWEEN TRUNC(item.datein) AND
                      TRUNC(NVL(item.dateout, sysdate))) LOOP
      v_hdrug.drggroup.extend();
      v_hdrug.drggroup(v_hdrug.drggroup.last) := rs.druggroup_name;
    END LOOP;
    RETURN v_hdrug;
  END find_hospdrug_withtmt;
END hospitaldrug_pack_sa;
