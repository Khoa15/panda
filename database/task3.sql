alter session set current_schema=panda;
CREATE OR REPLACE FUNCTION get_last_login (p_username VARCHAR2) RETURN TIMESTAMP
IS
    date_last_login TIMESTAMP;
BEGIN
    SELECT
        last_login INTO date_last_login
    FROM dba_users
    where username = p_username;
    RETURN date_last_login;
END;
/


CREATE OR REPLACE FUNCTION get_policy_in_user RETURN SYS_REFCURSOR IS
    c_policy SYS_REFCURSOR;
BEGIN

    OPEN c_policy FOR SELECT
                                tablespace_name,
                                contents,
                                status,
                                extent_management,
                                allocation_type,
                                logging
                            FROM
                                dba_tablespaces;

    RETURN c_policy;
END;
/
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

CREATE OR REPLACE FUNCTION get_policies(p_owner IN VARCHAR2)
RETURN SYS_REFCURSOR
IS
    policies_cursor SYS_REFCURSOR;
BEGIN
    OPEN policies_cursor FOR
        SELECT object_owner, policy_name,  function
        FROM all_policies
        WHERE object_owner = UPPER(p_owner);

    RETURN policies_cursor;
END;
/