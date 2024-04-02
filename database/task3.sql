alter session set current_schema=panda;
GRANT SELECT ON dba_ts_quotas TO PANDA;
GRANT SELECT ON dba_data_files TO PANDA;
GRANT SELECT ON dba_policies TO PANDA;
GRANT SELECT ON UNIFIED_AUDIT_TRAIL TO PANDA;
GRANT SELECT ON DBA_AUDIT_POLICIES TO PANDA;
GRANT EXECUTE ON DBMS_FGA TO PANDA;

CREATE OR REPLACE FUNCTION get_audit_policies RETURN SYS_REFCURSOR
IS
    list_audit_policies SYS_REFCURSOR;
BEGIN
    OPEN list_audit_policies FOR
    SELECT POLICY_NAME, OBJECT_NAME, POLICY_TEXT, POLICY_COLUMN, POLICY_COLUMN_OPTIONS, SEL, INS, UPD, DEL, ENABLED
FROM DBA_AUDIT_POLICIES WHERE OBJECT_SCHEMA='PANDA';
    RETURN list_audit_policies;
END;
/
CREATE OR REPLACE FUNCTION get_audit_trail RETURN SYS_REFCURSOR
IS
    list_policies SYS_REFCURSOR;
BEGIN
    OPEN list_policies FOR
        SELECT OBJECT_SCHEMA, OBJECT_NAME, ACTION_NAME, CURRENT_USER, SQL_TEXT, EVENT_TIMESTAMP_UTC
        FROM UNIFIED_AUDIT_TRAIL WHERE DBUSERNAME='PANDA' ORDER BY EVENT_TIMESTAMP_UTC DESC;

    RETURN list_policies;
END;
/

CREATE OR REPLACE PROCEDURE drop_audit_policy
(
    p_object_name VARCHAR,
    p_policy_name VARCHAR
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

CREATE OR REPLACE PROCEDURE add_policy
(
    p_object_name VARCHAR,
    p_policy_name VARCHAR,
    p_audit_condition VARCHAR2,
    p_audit_column VARCHAR,
    p_enable CHAR,
    p_statement_types VARCHAR
)
IS
    v_enable BOOLEAN;
BEGIN
    BEGIN
        IF p_enable = '1' THEN
            v_enable := true;
        ELSE
            v_enable := false;
        END IF;
        If p_audit_condition = 'all' THEN
            DBMS_FGA.ADD_POLICY(
                object_schema    => 'PANDA', 
                object_name      => p_object_name,
                policy_name      => p_policy_name,
                audit_column     => p_audit_column,
                enable           => v_enable,
                statement_types  => p_statement_types
            );
        ELSE
            DBMS_FGA.ADD_POLICY(
                object_schema    => 'PANDA', 
                object_name      => p_object_name,
                policy_name      => p_policy_name,
                audit_condition  => p_audit_condition,
                audit_column     => p_audit_column,
                enable           => v_enable,
                statement_types  => p_statement_types
            );
        END IF;
        
        EXCEPTION
            WHEN OTHERS THEN
                RAISE;
    END;
END;
/
CREATE OR REPLACE PROCEDURE modify_audit_policy
(
    p_object_name VARCHAR,
    p_policy_name VARCHAR,
    p_audit_condition VARCHAR,
    p_audit_column VARCHAR,
    p_enable CHAR,
    p_statement_types VARCHAR
)
IS
    v_enable BOOLEAN;
BEGIN
    BEGIN
        drop_audit_policy(p_object_name, p_policy_name);
        add_policy(
            p_object_name,
            p_policy_name,
            p_audit_condition,
            p_audit_column,
            p_enable,
            p_statement_types
        );
    END;
END;
/

CREATE OR REPLACE FUNCTION get_all_objects RETURN SYS_REFCURSOR
IS
    list_objects SYS_REFCURSOR;
BEGIN
    OPEN list_objects FOR
    SELECT OBJECT_ID, OBJECT_NAME, OBJECT_TYPE FROM dba_objects WHERE OWNER = 'PANDA';
    RETURN list_objects;
END;
/
CREATE OR REPLACE FUNCTION get_all_users RETURN SYS_REFCURSOR
IS
    list_users SYS_REFCURSOR;
BEGIN
    OPEN list_users FOR
    --SELECT user_id, username, account_status, expiry_date, profile, last_login FROM dba_users;
    SELECT
                        u.user_id,
                          u.username,
                          u.account_status,
                          u.profile,
                          u.last_login,
                          u.default_tablespace,
                          q.tablespace_name,
                          q.max_bytes   AS quota_size,
                          d.file_name,
                          d.bytes       AS datafile_size
                      FROM
                          dba_users        u
                          LEFT JOIN dba_ts_quotas    q ON u.username = q.username
                          LEFT JOIN dba_data_files   d ON u.default_tablespace = d.tablespace_name;   
    RETURN list_users;
END;
/
CREATE OR REPLACE TRIGGER project_before_insert_trigger
BEFORE INSERT ON project
FOR EACH ROW
DECLARE
    daily_count NUMBER;
BEGIN
//SGA (PACKAGE)
    SELECT COUNT(*)
    INTO daily_count
    FROM project
    WHERE username = :NEW.username
    AND TRUNC(created_at) = TRUNC(SYSDATE);

    IF daily_count >= 3 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Hey! That''s too much adding now.');
    END IF;
END;
/
CREATE OR REPLACE FUNCTION get_last_login RETURN TIMESTAMP
IS
    date_last_login TIMESTAMP;
BEGIN
    SELECT
        last_login INTO date_last_login
    FROM dba_users
    where username = user;
    RETURN date_last_login;
END;
/
GRANT SELECT ON dba_users TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.GET_LAST_LOGIN TO PANDA_USER_ROLE;
/

--CREATE OR REPLACE FUNCTION get_policies (p_owner IN VARCHAR2)
--RETURN SYS_REFCURSOR IS
--    c_policy SYS_REFCURSOR;
--BEGIN
--
--    OPEN c_policy FOR SELECT
--                                object_owner, policy_name, function
--                            FROM
--                                all_policies
--                                WHERE object_owner = UPPER(p_owner);
--
--    RETURN c_policy;
--END;
--/
CREATE OR REPLACE FUNCTION vpd_account_access_policy (
    schema_name IN VARCHAR2,
    table_name IN VARCHAR2
) RETURN VARCHAR2 AS
    v_predicate VARCHAR2(4000);
BEGIN
    IF SYS_CONTEXT('USERENV', 'SESSION_USER') = 'PANDA' THEN
        v_predicate := '1=2'; -- Kh��c󠨠ng n௠kh?p v?i ?i?u ki?n n�?
    ELSE
        v_predicate := '1=1'; -- T?t c? cᣠh஧ kh?p v?i ?i?u ki?n n�?
    END IF;
    RETURN v_predicate;
END;
/

CREATE OR REPLACE FUNCTION vpd_account_access_policy (
    schema_name IN VARCHAR2,
    table_name IN VARCHAR2
) RETURN VARCHAR2 AS
    v_predicate VARCHAR2(4000);
BEGIN
    IF SYS_CONTEXT('USERENV', 'SESSION_USER') = 'PANDA' THEN
        v_predicate := '1=2'; -- Kh?ng c? h?ng n?o kh?p v?i ?i?u ki?n n?y
    ELSE
        v_predicate := '1=1'; -- T?t c? c?c h?ng kh?p v?i ?i?u ki?n n?y
    END IF;
    RETURN v_predicate;
END;

/

--CREATE OR REPLACE FUNCTION get_policies(p_owner IN VARCHAR2)
--RETURN SYS_REFCURSOR
--IS
--    policies_cursor SYS_REFCURSOR;
--BEGIN
--    OPEN policies_cursor FOR
--        SELECT object_owner, policy_name, function
--        FROM all_policies
--        WHERE object_owner = UPPER(p_owner);
--
--    RETURN policies_cursor;
--END;
--/

CREATE OR REPLACE FUNCTION get_quota_user(
    p_user IN VARCHAR
) RETURN SYS_REFCURSOR
IS
    quotas SYS_REFCURSOR;
BEGIN
    OPEN quotas FOR
                    SELECT
                        u.user_id,
                          u.username,
                          u.account_status,
                          u.profile,
                          u.last_login,
                          u.default_tablespace,
                          q.tablespace_name,
                          q.max_bytes   AS quota_size,
                          d.file_name,
                          d.bytes       AS datafile_size
                      FROM
                          dba_users        u
                          LEFT JOIN dba_ts_quotas    q ON u.username = q.username
                          LEFT JOIN dba_data_files   d ON u.default_tablespace = d.tablespace_name
                    WHERE u.username = p_user;
    RETURN quotas;
END;
/