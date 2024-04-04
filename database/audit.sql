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

CREATE OR REPLACE FUNCTION get_list_username RETURN SYS_REFCURSOR
IS
    list_username SYS_REFCURSOR;
BEGIN
    OPEN list_username FOR
    SELECT username FROM all_users;
    RETURN list_username;
END;
/

CREATE OR REPLACE FUNCTION get_audit_option_sys_action
RETURN SYS_REFCURSOR
IS
    list_audit SYS_REFCURSOR;
BEGIN
    OPEN list_audit FOR
    SELECT NAME FROM AUDITABLE_SYSTEM_ACTIONS WHERE COMPONENT = 'STANDARD';
    RETURN list_audit;
END;
/

CREATE OR REPLACE FUNCTION get_audit_option_syspriv
RETURN SYS_REFCURSOR
IS
    list_audit SYS_REFCURSOR;
BEGIN
    OPEN list_audit FOR
    SELECT NAME FROM SYSTEM_PRIVILEGE_MAP;
    RETURN list_audit;
END;
/
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

CREATE OR REPLACE PROCEDURE CREATE_AUDIT
(
    p_name VARCHAR,
    p_sys_privs VARCHAR,
    p_actions VARCHAR,
    p_roles VARCHAR,
    p_user VARCHAR,
    p_evaluate VARCHAR,
    p_only_top VARCHAR,
    p_container VARCHAR
)
IS
    v_sql VARCHAR(4000);
BEGIN
    v_sql := 'CREATE AUDIT POLICY ' || p_name || ' ';
    IF LENGTH(p_sys_privs) > 0 THEN
    v_sql := v_sql || ' PRIVILEGES ' || p_sys_privs;
    END IF;
    IF LENGTH(p_actions) > 0 THEN
    v_sql := v_sql || ' ACTIONS ' || p_actions;
    END IF;
    IF LENGTH(p_roles) > 0 THEN
    v_sql := v_sql || p_roles;
    END IF;
    IF LENGTH(p_user) > 0 THEN
        v_sql := v_sql || ' WHEN ''SYS_CONTEXT(''''USERENV'''', ''''SESSION_USER'''') = ''''|| p_user ||''''''  EVALUATE PER ' || p_evaluate ;
    END IF;
    
    IF p_only_top = 'Y' THEN
    v_sql := v_sql || ' ONLY TOPLEVEL ';
    END IF;
    IF LENGTH(p_container) > 0 THEN
    v_sql := v_sql || ' CONTAINER = ' || p_container;
    END IF;
    DBMS_OUTPUT.PUT_LINE(v_sql);
    EXECUTE IMMEDIATE v_sql;
END;
/

BEGIN
CREATE_AUDIT(
    p_name =>  'test',
    p_sys_privs => 'CREATE ANY TABLE',
    p_actions => '',
    p_roles => '',
    p_user => 'ABC',
    p_evaluate => 'SESSION',
    p_only_top => 'N',
    p_container => ''
);
END;
/


