ALTER SESSION SET CURRENT_SCHEMA=PANDA;

SELECT * FROM DBA_AUDIT_POLICIES;

SELECT * FROM DBA_STMT_AUDIT_OPTS;

SELECT * FROM DBA_AUDIT_OBJECT;

SELECT DISTINCT OBJECT_NAME FROM dba_objects;

SELECT * FROM AUDIT_UNIFIED_POLICIES;

SELECT * FROM DBA_AUDIT_TRAIL;
SELECT * FROM unified_audit_trail;

SELECT * FROM PANDA.TASK;

AUDIT ALL ON PANDA.TASK;
NOAUDIT ALL ON PANDA.TASK;



BEGIN DBMS_AUDIT_MGMT.INIT_CLEANUP(
audit_trail_type          => DBMS_AUDIT_MGMT.AUDIT_TRAIL_ALL,
default_cleanup_interval => 2
);
END;
/


EXECUTE DBMS_AUDIT_MGMT.CREATE_PURGE_JOB(
  degree => 8,  -- Parallelism level (optional)
  use_last_arch_timestamp => TRUE
);

BEGIN
DBMS_AUDIT_MGMT.CLEAN_AUDIT_TRAIL(
    AUDIT_TRAIL_TYPE => DBMS_AUDIT_MGMT.AUDIT_TRAIL_UNIFIED,
    use_last_arch_timestamp  =>  FALSE
);
END;
/

//CREATE AUDIT POLICY TEST_AUDIT_POLICY ACTIONS ALL;

BEGIN
    DBMS_FGA.ADD_POLICY(
        object_schema    => 'PANDA',
        object_name      => 'TASK',
        policy_name      => 'AUDIT_SELECT_ON_TEN_BANG',
        audit_condition  =>  SYS_CONTEXT('USERENV','SESSION_USER') <> 'PANDA_USER',
        statement_types  => 'SELECT'
    );
END;
/

//AUDIT ALL ON PANDA.TASK BY ACCESS;

GRANT SELECT ON AUDIT_UNIFIED_POLICIES TO PANDA;
CREATE OR REPLACE FUNCTION get_policies_audit
RETURN SYS_REFCURSOR
IS
    list_policies SYS_REFCURSOR;
BEGIN
    OPEN list_policies FOR
    SELECT DISTINCT POLICY_NAME FROM AUDIT_UNIFIED_POLICIES;
    RETURN list_policies;
END;
/

CREATE OR REPLACE FUNCTION get_info_policies_audit
(
    p_policy VARCHAR
)
RETURN SYS_REFCURSOR
IS
    list_info SYS_REFCURSOR;
BEGIN
    OPEN list_info FOR
    SELECT AUDIT_CONDITION,
            CONDITION_EVAL_OPT,
            AUDIT_OPTION,
            AUDIT_OPTION_TYPE,
            OBJECT_SCHEMA,
            OBJECT_NAME,
            OBJECT_TYPE,
            AUDIT_ONLY_TOPLEVEL
        FROM AUDIT_UNIFIED_POLICIES WHERE POLICY_NAME =p_policy;
    RETURN list_info;
END;
/

CREATE OR REPLACE PROCEDURE CREATE_AUDIT_OBJECT
(
    p_name VARCHAR,
    p_statement VARCHAR,
    p_user VARCHAR,
    p_all CHAR
)
IS
    v_sql VARCHAR(1000);
BEGIN
    IF p_all = 'Y' THEN
    v_sql := 'CREATE AUDIT POLICY ' || p_name || ' ACTIONS ' || p_statement || ' ON ' || p_user;
    ELSE
    v_sql := 'CREATE AUDIT POLICY ' || p_name || ' ACTIONS ALL';
    END IF;
    EXECUTE IMMEDIATE v_sql;
END;
/

CREATE OR REPLACE PROCEDURE CREATE_AUDIT_SYSTEM
(
    p_name VARCHAR,
    p_statement VARCHAR,
    p_action VARCHAR,
    p_user VARCHAR
)
IS
    v_sql VARCHAR(1000);
BEGIN
    v_sql := 'CREATE AUDIT POLICY ' || p_name || ' PRIVILEGES ' || p_statement;
    if p_action != '' THEN
        v_sql := v_sql || ' ACTION ' || p_action;
         IF p_user != '' THEN 
            v_sql := v_sql || ' ON ' || p_user;
         END IF;
    END IF;
    EXECUTE IMMEDIATE v_sql;
END;
/
CREATE OR REPLACE PROCEDURE CREATE_AUDIT_ROLE
(
    p_name VARCHAR,
    p_statement VARCHAR,
    p_roles VARCHAR
)
IS
    v_sql VARCHAR(1000);
BEGIN
    v_sql := 'CREATE AUDIT POLICY ' || p_name || ' ROLE ' || p_roles || ' ON ' || p_user;
    EXECUTE IMMEDIATE v_sql;
END;
/