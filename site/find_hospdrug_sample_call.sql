set serveroutput on

declare
v_drug HospitalDrug;
begin
v_drug := hospitaldrug_pack.find_hospdrug('1000020', '11513', to_date('22/08/2014', 'dd/mm/yyyy'));
DBMS_OUTPUT.PUT_line('tmtid --> ' || v_drug.tmtid);
DBMS_OUTPUT.PUT_line('tmt_type --> ' || v_drug.tmt_type);
DBMS_OUTPUT.PUT_line('fsn --> ' || v_drug.fsn);
DBMS_OUTPUT.PUT_line('manufacturer --> ' || v_drug.manufacturer);
DBMS_OUTPUT.PUT_line('unit_price --> ' || v_drug.unit_price);
DBMS_OUTPUT.PUT_line('specprep --> ' || v_drug.specprep);
DBMS_OUTPUT.PUT_line('is_ed --> ' || v_drug.is_ed);
DBMS_OUTPUT.PUT_line('drug group --> ' || v_drug.drggroup.count);

for rs in 1..v_drug.drggroup.count loop
DBMS_OUTPUT.PUT_line('group --> ' || v_drug.drggroup(rs));
end loop;

end;
