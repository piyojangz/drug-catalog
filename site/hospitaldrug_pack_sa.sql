create or replace PACKAGE "HOSPITALDRUG_PACK_SA" AS
  FUNCTION find_deleted(p_hospdrugcode VARCHAR2,
                        p_hcode        VARCHAR2,
                        p_tmtid        VARCHAR2,
                        p_date         DATE) RETURN VARCHAR2;
  FUNCTION find_status_ed(v_current_tmt  VARCHAR2,
                          p_date         DATE) RETURN VARCHAR2;
  FUNCTION is_drug_exist(p_hospdrugcode VARCHAR2, p_hcode VARCHAR2, p_tmtid VARCHAR2) RETURN NUMBER;
  FUNCTION find_reimburse_price(p_tmtid VARCHAR2, p_date DATE) RETURN NUMBER;
  FUNCTION find_reimburse_price_tp(p_hcode        VARCHAR2,
                                   p_hospdrugcode VARCHAR2,
                                   p_tmtid        VARCHAR2,
                                   p_date         DATE) RETURN REIMBURSE_PRICE_TP;
  FUNCTION find_drug_attr(p_hcode        VARCHAR2,
                          p_hospdrugcode VARCHAR2,
                          p_tmtid        VARCHAR2,
                          p_date         DATE) RETURN DRUGATTR;

  FUNCTION find_hospdrug_withtmt(p_hospdrugcode VARCHAR2,
                                 p_hcode        VARCHAR2,
                                 p_tmtid        VARCHAR2,
                                 p_date         DATE) RETURN hospitaldrug;
END HOSPITALDRUG_PACK_SA;