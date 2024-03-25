alter session set current_schema=panda;
GRANT SELECT ON dba_ts_quotas TO PANDA;
GRANT SELECT ON dba_data_files TO PANDA;
GRANT SELECT ON dba_policies TO PANDA;
GRANT EXECUTE ON DBMS_FGA TO PANDA;
CREATE OR REPLACE FUNCTION get_policies RETURN SYS_REFCURSOR
IS
    list_policies SYS_REFCURSOR;
BEGIN
    OPEN list_policies FOR
        select * from dba_policies ORDER BY policy_group;
    RETURN list_policies;
END;
/
--SELECT * FROM  all_objects;
CREATE OR REPLACE PROCEDURE add_policy
(
    p_object_name VARCHAR,
    p_policy_name VARCHAR,
    p_audit_condition VARCHAR,
    p_audit_column VARCHAR,
    p_handler_schema VARCHAR,
    p_handler_module VARCHAR,
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
        DBMS_FGA.ADD_POLICY(
            object_schema    => 'PANDA', 
            object_name      => p_object_name,
            policy_name      => p_policy_name,
            audit_condition  => p_audit_condition,
            audit_column     => p_audit_column,
            handler_schema   => p_handler_schema,
            handler_module   => p_handler_module,
            enable           => v_enable,
            statement_types  => p_statement_types
        );
        EXCEPTION
            WHEN OTHERS THEN
                RAISE;
    END;
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