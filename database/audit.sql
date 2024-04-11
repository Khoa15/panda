ALTER SESSION SET CURRENT_SCHEMA=PANDA;

show parameter audit;
AUDIT ALL ON TABLES;

SELECT * FROM DBA_AUDIT_POLICIES;
SELECT * FROM UNIFIED_AUDIT_TRAIL  ORDER BY EVENT_TIMESTAMP DESC;
SELECT * FROM DBA_AUDIT_TRAIL;

CREATE OR REPLACE PROCEDURE ADD_FGA_POLICY
(
    p_object_name IN VARCHAR2,
    p_policy_name IN VARCHAR2,
    p_type IN VARCHAR2,
    p_audit_condition IN VARCHAR2
) IS
    v_audit_condition VARCHAR2(1000);
BEGIN
    IF p_audit_condition = 'All' THEN
        v_audit_condition := '1=1';
    ELSE
        v_audit_condition := 'SYS_CONTEXT(''USERENV'', ''SESSION_USER'') = ''' || UPPER(p_audit_condition) || '''';
    END IF;
    
    DBMS_FGA.add_policy(
        object_schema => 'PANDA',
        object_name => p_object_name,
        policy_name => p_policy_name,
        audit_condition => v_audit_condition,
        statement_types => p_type
    );
END;
/

CREATE OR REPLACE PROCEDURE DROP_FGA_POLICY
(
    p_object_name IN VARCHAR2,
    p_policy_name IN VARCHAR2
)
IS
BEGIN
    DBMS_FGA.DROP_POLICY(
       object_schema  => 'PANDA', 
       object_name    => p_object_name, 
       policy_name    => p_policy_name
   );
END;
/

CREATE OR REPLACE PROCEDURE MODIFY_FGA_POLICY
(
    p_object_name IN VARCHAR2,
    p_policy_name IN VARCHAR2,
    p_type IN VARCHAR2,
    p_audit_condition IN VARCHAR2
)IS
BEGIN
    BEGIN
        DROP_FGA_POLICY(
            p_object_name,
            p_policy_name
        );
    END;
    BEGIN
        ADD_FGA_POLICY(
            p_object_name,
            p_policy_name,
            p_type,
            p_audit_condition
        );
    END;
END;
/