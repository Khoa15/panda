-- Task 4
alter session set current_schema=panda;
-- Role user about system
--GRANT SELECT ON role_tab_privs TO PANDA;
--GRANT SELECT ON all_objects TO PANDA;
GRANT SELECT ON dba_role_privs TO PANDA;
GRANT SELECT ON dba_roles TO PANDA;
CREATE OR REPLACE PROCEDURE delete_all_data_user (
    p_user VARCHAR
)
IS
BEGIN
    BEGIN
        DELETE FROM sentence_vocab_typevocab WHERE word IN (
            SELECT word FROM vocab_typevocab WHERE cid IN (
                SELECT id FROM card WHERE id IN (
                    SELECT card_id FROM collection_card WHERE collect_id IN (
                        SELECT id FROM collection WHERE username = p_user
                    )
                )
            )
        );
    
        DELETE FROM collection_card WHERE collect_id IN (
            SELECT id FROM collection WHERE username = p_user
        );
    
        DELETE FROM vocab_typevocab WHERE cid IN (
            SELECT id FROM card WHERE id IN (
                SELECT card_id FROM collection_card WHERE collect_id IN (
                    SELECT id FROM collection WHERE username = p_user
                )
            )
        );
    
        DELETE FROM flashcard WHERE cid IN (
            SELECT id FROM card WHERE id IN (
                SELECT card_id FROM collection_card WHERE collect_id IN (
                    SELECT id FROM collection WHERE username = p_user
                )
            )
        );
    
        DELETE FROM card WHERE id IN (
            SELECT card_id FROM collection_card WHERE collect_id IN (
                SELECT id FROM collection WHERE username = p_user
            )
        );
    
        DELETE FROM collection WHERE username = p_user;
    
        DELETE FROM task WHERE pid IN (
            SELECT id FROM project WHERE username = p_user
        );
    
        DELETE FROM project WHERE username = p_user;
    
        DELETE FROM account WHERE username = p_user;
    
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END;
END;
/

--EXECUTE delete_tablespace('PANDA_USER');
CREATE OR REPLACE PROCEDURE remove_user(
    p_user VARCHAR
)
IS
BEGIN
    BEGIN
        EXECUTE IMMEDIATE 'DROP USER ' || p_user || ' CASCADE';
        BEGIN delete_tablespace(p_user); END;
        BEGIN delete_all_data_user(p_user); END;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE;
    END;
END;
/
CREATE OR REPLACE FUNCTION get_dba_tab_privs RETURN SYS_REFCURSOR
IS
    list_tab_privs SYS_REFCURSOR;
BEGIN
    OPEN list_tab_privs FOR
    select * from dba_tab_privs ORDER BY GRANTEE;

    RETURN list_tab_privs;
END;
/
CREATE OR REPLACE FUNCTION get_panda_procedures RETURN SYS_REFCURSOR
IS
    list_procedures SYS_REFCURSOR;
BEGIN
    OPEN list_procedures FOR
    SELECT object_name FROM all_procedures WHERE OWNER='PANDA' ORDER BY OBJECT_NAME;
    RETURN list_procedures;
END;
/

CREATE OR REPLACE FUNCTION get_panda_tables RETURN SYS_REFCURSOR
IS
    list_tables SYS_REFCURSOR;
BEGIN
    OPEN list_tables FOR
    SELECT table_name FROM all_tables WHERE OWNER='PANDA' ORDER BY TABLE_NAME;
    RETURN list_tables;
END;
/

CREATE OR REPLACE FUNCTION get_insert_sys_privs
(
    p_grantee VARCHAR
)
RETURN SYS_REFCURSOR
IS
    list_sys_privs SYS_REFCURSOR;
BEGIN
    OPEN list_sys_privs FOR
    SELECT privilege
    FROM dba_sys_privs 
    
    MINUS
    
    SELECT privilege
    FROM dba_sys_privs 
    WHERE grantee = p_grantee;

    RETURN list_sys_privs;
END;
/
CREATE OR REPLACE FUNCTION get_sys_privs
(
    p_grantee VARCHAR
)
RETURN SYS_REFCURSOR
IS
    list_sys_privs SYS_REFCURSOR;
BEGIN
    OPEN list_sys_privs FOR
    SELECT privilege, admin_option, common, inherited FROM dba_sys_privs WHERE grantee = p_grantee;
    RETURN list_sys_privs;
END;
/
CREATE OR REPLACE FUNCTION get_role_others
(
    p_role VARCHAR,
    p_insert CHAR
)
RETURN SYS_REFCURSOR
IS
    list_role_others SYS_REFCURSOR;
BEGIN

    IF p_insert = 'Y' THEN
        OPEN list_role_others FOR
        SELECT OBJECT_NAME, OBJECT_TYPE 
        FROM all_objects 
        WHERE OWNER = 'PANDA' 
        AND (OBJECT_TYPE = 'PROCEDURE' OR OBJECT_TYPE='FUNCTION' OR OBJECT_TYPE='TABLE') 
        AND OBJECT_NAME NOT IN (
            SELECT TABLE_NAME 
            FROM dba_tab_privs 
            WHERE grantee = p_role
        )
        ORDER BY OBJECT_TYPE;
    ELSE 
        OPEN list_role_others FOR
        SELECT TABLE_NAME, type, privilege, grantable
        FROM dba_tab_privs 
        WHERE grantee = p_role;
    END IF;
    RETURN list_role_others;
END;
/
CREATE OR REPLACE FUNCTION get_insert_granted_roles
(
    p_grantee VARCHAR
)
RETURN SYS_REFCURSOR
IS
    list_granted_roles SYS_REFCURSOR;
BEGIN
    
    OPEN list_granted_roles FOR
    SELECT role FROM dba_roles
    MINUS
    SELECT granted_role
    from dba_role_privs WHERE grantee=p_grantee order by role;
    RETURN list_granted_roles;
END;
/
CREATE OR REPLACE FUNCTION get_granted_roles
(
    p_grantee VARCHAR
)
RETURN SYS_REFCURSOR
IS
    list_granted_roles SYS_REFCURSOR;
BEGIN
    
    OPEN list_granted_roles FOR
    select granted_role, admin_option, delegate_option, default_role, common, inherited from dba_role_privs WHERE grantee=p_grantee order by granted_role;
    RETURN list_granted_roles;
END ;
/

CREATE OR REPLACE FUNCTION get_list_users RETURN SYS_REFCURSOR
IS
    list_users SYS_REFCURSOR;
BEGIN
    OPEN list_users FOR
    SELECT USER_ID, USERNAME, ACCOUNT_STATUS, LOCK_DATE, PROFILE, DEFAULT_TABLESPACE, TEMPORARY_TABLESPACE FROM dba_users ORDER BY username;
    RETURN list_users;
END;
/

CREATE OR REPLACE FUNCTION get_dba_profiles RETURN SYS_REFCURSOR
IS
    list_profiles SYS_REFCURSOR;
BEGIN
    OPEN list_profiles FOR
    SELECT DISTINCT PROFILE FROM dba_profiles order by profile ASC;
    RETURN list_profiles;
END;
/



CREATE OR REPLACE PROCEDURE CREATE_PROFILE (p_profile IN VARCHAR2, p_resource_name IN VARCHAR2, p_limit IN VARCHAR2)
IS
BEGIN
    EXECUTE IMMEDIATE 'CREATE PROFILE ' || p_profile || ' LIMIT ' || p_resource_name || ' ' || p_limit;
END;
/

CREATE OR REPLACE FUNCTION get_users_privs RETURN SYS_REFCURSOR
IS
    list_users SYS_REFCURSOR;
BEGIN
    OPEN list_users FOR
    SELECT username FROM dba_users order by username;
    RETURN list_users;
END;
/
CREATE OR REPLACE FUNCTION get_roles
RETURN SYS_REFCURSOR
IS
    list_tab_privs SYS_REFCURSOR;
BEGIN
    OPEN list_tab_privs FOR
    select role from dba_roles order by role;--role_tab_privs
    RETURN list_tab_privs;
END;
/


CREATE OR REPLACE FUNCTION get_role_tab_privs (p_role VARCHAR2)
RETURN SYS_REFCURSOR
IS
    list_tab_privs SYS_REFCURSOR;
BEGIN
    OPEN list_tab_privs FOR
    select table_name, column_name, privilege, inherited from role_tab_privs WHERE role = p_role order by table_name;
    RETURN list_tab_privs;
END;
/
CREATE OR REPLACE FUNCTION get_role_tab_privs_privs (
    p_role VARCHAR2,
    p_table_name VARCHAR2
)
RETURN SYS_REFCURSOR
IS
    list_tab_privs_privs SYS_REFCURSOR;
BEGIN
    OPEN list_tab_privs_privs FOR
    SELECT privilege FROM dba_tab_privs WHERE grantee = p_role AND TABLE_NAME = p_table_name;
    RETURN list_tab_privs_privs;
END;
/
select * from role_tab_privs where role = 'PANDA_USER_ROLE';
GRANT UPDATE ON COLLECTION TO PANDA_USER_ROLE;
--CREATE OR REPLACE FUNCTION get_role_tab_privs 

CREATE OR REPLACE FUNCTION get_procedures_functions_tables (p_role VARCHAR2) RETURN SYS_REFCURSOR
IS
    list_objects SYS_REFCURSOR;
BEGIN
    OPEN list_objects FOR
    SELECT OBJECT_NAME, OBJECT_TYPE 
    FROM all_objects 
    WHERE OWNER = 'PANDA' 
    AND (OBJECT_TYPE = 'PROCEDURE' OR OBJECT_TYPE='FUNCTION' OR OBJECT_TYPE='TABLE') 
    AND OBJECT_NAME NOT IN (
        SELECT TABLE_NAME 
        FROM role_tab_privs 
        WHERE ROLE = p_role
    )
    ORDER BY OBJECT_TYPE;
    RETURN list_objects;
END;
/

CREATE OR REPLACE PROCEDURE revoke_role (
    p_grantee VARCHAR, 
    p_object VARCHAR,
    p_privilege VARCHAR
)
IS
    v_has VARCHAR2(100);
BEGIN
    SELECT COALESCE(MAX(CASE WHEN table_name = p_object THEN 'Y' ELSE 'N' END), 'N') INTO v_has
            FROM dba_tab_privs 
            WHERE grantee = p_grantee
            and table_name = p_object;

    IF v_has = 'Y' THEN
        EXECUTE IMMEDIATE 'REVOKE ' || p_privilege || ' ON ' || p_object || ' FROM ' || p_grantee;
    ELSE
        EXECUTE IMMEDIATE 'REVOKE ' || p_object || ' FROM ' || p_grantee;
    END IF;
END;
/
//SET SERVEROUT ON;
CREATE OR REPLACE PROCEDURE GRANT_OR_REVOKE_GRANT
(
    p_grantee VARCHAR2,
    p_privilege VARCHAR2,
    p_object VARCHAR2,
    p_grant_option CHAR,
    p_grant CHAR
)
IS
    v_has_priv CHAR(1) := 'N';
    v_has_grant_option CHAR(3) := 'N';
    v_sql VARCHAR(4000);
BEGIN
        SELECT COALESCE(MAX(CASE WHEN privilege = p_privilege THEN 'Y' ELSE 'N' END), 'N')
        INTO v_has_priv
        FROM dba_tab_privs
        WHERE table_name = p_object
        AND privilege = p_privilege
        AND grantee = p_grantee;
        
        
            IF p_grant = 'N' AND v_has_priv = 'Y' THEN
                 EXECUTE IMMEDIATE 'REVOKE '|| p_privilege ||' ON ' || p_object || ' FROM ' || p_grantee;
            ELSIF p_grant = 'Y' THEN
                DBMS_OUTPUT.PUT_LINE(p_grant_option);
                
                SELECT COALESCE(MAX(CASE WHEN grantable = 'YES' THEN 'Y' ELSE 'N' END), 'N')
                INTO v_has_grant_option
                FROM dba_tab_privs
                WHERE table_name = p_object
                AND privilege = p_privilege
                AND grantee = p_grantee;
            
                v_sql := 'GRANT '||p_privilege||' ON ' || p_object || ' TO ' || p_grantee;
                IF p_grant_option = 'Y' THEN
                    v_sql := v_sql || ' WITH GRANT OPTION';
                ELSIF p_grant_option = 'N' AND v_has_grant_option = 'Y' THEN
                    EXECUTE IMMEDIATE 'REVOKE '|| p_privilege ||' ON ' || p_object || ' FROM ' || p_grantee;
                END IF;
                EXECUTE IMMEDIATE v_sql;
            END IF;
END;
/
CREATE OR REPLACE PROCEDURE update_role_privs (
    p_role VARCHAR2,
    p_object VARCHAR2,
    p_execute CHAR,
    p_grant_execute CHAR,
    p_select CHAR,
    p_grant_select CHAR,
    p_update CHAR,
    p_grant_update CHAR,
    p_delete CHAR,
    p_grant_delete CHAR,
    p_insert CHAR,
    p_grant_insert CHAR,
    p_sys_priv CHAR,
    p_option VARCHAR
)
IS
    v_has_execute CHAR(1) := 'N';
    v_has_select CHAR(1) := 'N';
    v_has_update CHAR(1) := 'N';
    v_has_delete CHAR(1) := 'N';
    v_has_insert CHAR(1) := 'N';
    v_has_sys_priv CHAR(1) := 'N';
    v_has_option CHAR(1) := 'N';
    v_text_option VARCHAR(100) := '';
BEGIN
    BEGIN
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'EXECUTE',
            p_object,
            p_grant_execute,
            p_execute
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'SELECT',
            p_object,
            p_grant_select,
            p_select
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'UPDATE',
            p_object,
            p_grant_update,
            p_select
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'DELETE',
            p_object,
            p_grant_delete,
            p_select
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'INSERT',
            p_object,
            p_grant_insert,
            p_insert
        );
        
        SELECT COALESCE(MAX(CASE WHEN GRANTED_ROLE = p_object THEN 'Y' ELSE 'N' END), 'N')
        INTO v_has_sys_priv
        FROM dba_role_privs
        WHERE grantee = p_role;
        
        SELECT COALESCE(MAX(CASE WHEN ADMIN_OPTION = 'YES' THEN 'Y' ELSE 'N' END), 'N')
        INTO v_has_option
        FROM dba_role_privs
        WHERE grantee = p_role AND GRANTED_ROLE = p_object;

        
        IF v_has_sys_priv = 'N' THEN
            SELECT COALESCE(MAX(CASE WHEN privilege = p_object THEN 'Y' ELSE 'N' END), 'N')
            INTO v_has_sys_priv
            FROM dba_sys_privs
            WHERE grantee = p_role;
        END IF;
        
        IF v_has_option = 'N' THEN
        SELECT COALESCE(MAX(CASE WHEN ADMIN_OPTION = 'YES' THEN 'Y' ELSE 'N' END), 'N')
            INTO v_has_option
            FROM dba_sys_privs
            WHERE grantee = p_role AND privilege = p_object;
            END IF;
        
        IF v_has_option = 'Y' AND p_option = 'N' THEN
            EXECUTE IMMEDIATE 'REVOKE ' || p_object || ' FROM ' || p_role;
            EXECUTE IMMEDIATE 'GRANT ' || p_object || ' to ' || p_role;
        END IF;
        
        IF p_sys_priv = 'Y' AND v_has_sys_priv = 'N' THEN
            IF p_option = 'Y' THEN
                EXECUTE IMMEDIATE 'GRANT ' || p_object || ' to ' || p_role || ' WITH ADMIN OPTION';
            ELSE
                EXECUTE IMMEDIATE 'GRANT ' || p_object || ' to ' || p_role;
            END IF;
        END IF;
        IF v_has_sys_priv = 'Y' AND v_has_option = 'N' AND p_option = 'Y' THEN
            EXECUTE IMMEDIATE 'GRANT ' || p_object || ' to ' || p_role || ' WITH ADMIN OPTION'; 
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE;
    END;
END;
/


CREATE OR REPLACE PROCEDURE insert_role_privs (
    p_role VARCHAR2,
    p_object VARCHAR2,
    p_execute CHAR,
    p_grant_execute CHAR,
    p_select CHAR,
    p_grant_select CHAR,
    p_update CHAR,
    p_grant_update CHAR,
    p_delete CHAR,
    p_grant_delete CHAR,
    p_insert CHAR,
    p_grant_insert CHAR,
    p_sys_priv CHAR,
    p_option VARCHAR
)
IS
BEGIN
    EXECUTE IMMEDIATE 'CREATE ROLE ' || p_role;

        GRANT_OR_REVOKE_GRANT(
            p_role,
            'EXECUTE',
            p_object,
            p_grant_execute,
            p_execute
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'SELECT',
            p_object,
            p_grant_select,
            p_select
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'UPDATE',
            p_object,
            p_grant_update,
            p_select
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'DELETE',
            p_object,
            p_grant_delete,
            p_select
        );
        GRANT_OR_REVOKE_GRANT(
            p_role,
            'INSERT',
            p_object,
            p_grant_insert,
            p_insert
        );
--    IF p_execute = 'Y' THEN
--        EXECUTE IMMEDIATE 'GRANT EXECUTE ON ' || p_object || ' TO ' || p_role || ' ' || p_option;
--    END IF;
--
--    IF p_select = 'Y' THEN
--        EXECUTE IMMEDIATE 'GRANT SELECT ON ' || p_object || ' TO ' || p_role || ' ' || p_option;
--    END IF;
--
--    IF p_update = 'Y' THEN
--        EXECUTE IMMEDIATE 'GRANT UPDATE ON ' || p_object || ' TO ' || p_role || ' ' || p_option;
--    END IF;
--
--    IF p_delete = 'Y' THEN
--        EXECUTE IMMEDIATE 'GRANT DELETE ON ' || p_object || ' TO ' || p_role || ' ' || p_option;
--    END IF;
--
--    IF p_insert = 'Y' THEN
--        EXECUTE IMMEDIATE 'GRANT INSERT ON ' || p_object || ' TO ' || p_role || ' ' || p_option;
--    END IF;

    IF p_sys_priv = 'Y' THEN
        EXECUTE IMMEDIATE 'GRANT ' || p_object || ' TO ' || p_role || ' ' || p_option;
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE delete_role_privs(
    p_role VARCHAR
) IS
BEGIN
    EXECUTE IMMEDIATE 'DROP ROLE ' || p_role;
END;
/




------------ PHUC
CREATE OR REPLACE PROCEDURE Resources_profile 
is
begin 
    execute immediate 'CREATE PROFILE RESOURCES_PROFILE_USER LIMIT
    SESSIONS_PER_USER 3
    CPU_PER_SESSION 600
    IDLE_TIME 60
    CONNECT_TIME 500
    ';
END;
/

create or replace procedure user_lock
is
begin
    execute immediate ' CREATE PROFILE MATKHAU_3 LIMIT
    FAILED_LOGIN_ATTEMPTS 3
    PASSWORD_LIFE_TIME 30
    PASSWORD_REUSE_MAX 5
    PASSWORD_LOCK_TIME 1 / 24
    PASSWORD_GRACE_TIME 10';
END;
/

CREATE OR REPLACE PROCEDURE PROC_CREATE_PROFILE(nameProfile in varchar2, sessionN in int, connectTime in int,reUsePass in int, failLogin in int, passlock in varchar2)
is
begin
    execute immediate 'create profile ' ||nameProfile|| ' limit
     SESSIONS_PER_USER '||sessionN|| '
     CONNECT_TIME '||connectTime|| '
     PASSWORD_REUSE_MAX '||reUsePass||' 
     FAILED_LOGIN_ATTEMPTS '||failLogin|| '
     PASSWORD_LOCK_TIME '|| passlock||' ';
end;     
/


CREATE OR REPLACE PROCEDURE ADD_ACCOUNT_PROFILE (
    p_username VARCHAR2,
    p_fullname NVARCHAR2,
    p_password VARCHAR2,
    p_profile_name varchar2,
    p_tablespace VARCHAR2,
    p_quota VARCHAR2,
    p_lock CHAR
)
IS
    BEGIN
        INSERT INTO ACCOUNT (avatar, username, fullname) VALUES
        (null, p_username, p_fullname);
        EXECUTE IMMEDIATE 'CREATE USER '
                 || p_username
                 || ' IDENTIFIED BY '
                 || p_password;
        EXECUTE IMMEDIATE 'GRANT CREATE SESSION TO ' || p_username;
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' PROFILE ' || p_profile_name;
        EXECUTE IMMEDIATE 'GRANT PANDA_USER_ROLE TO ' || p_username;
        EXECUTE IMMEDIATE 'CREATE TABLESPACE ' || p_username || ' datafile ''D:\' || p_username || '.dbf'' size 100m AUTOEXTEND OFF';

        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' QUOTA 10M ON ' || p_username || '';
        IF p_lock = '1' THEN  
            EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' ACCOUNT LOCK';
        END IF;
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
END ADD_ACCOUNT_PROFILE;
/

--CREATE OR REPLACE FUNCTION GET_ALL_PROFILES RETURN SYS_REFCURSOR
--IS
--    c_profile SYS_REFCURSOR;
--BEGIN
--    OPEN c_profile FOR
--    SELECT * FROM dba_profiles ORDER BY PROFILE;
--    RETURN c_profile;
--END;
--/

CREATE OR REPLACE FUNCTION GET_PROFILE (p_profile IN VARCHAR2) RETURN SYS_REFCURSOR
IS
    c_profile SYS_REFCURSOR;
BEGIN
    OPEN c_profile FOR
    select * from dba_profiles where profile=p_profile AND (RESOURCE_NAME ='CONNECT_TIME' OR RESOURCE_NAME ='IDLE_TIME' OR RESOURCE_NAME = 'PASSWORD_LOCK_TIME' OR RESOURCE_NAME = 'SESSION_PER_USER' OR RESOURCE_NAME = 'IDLE_TIME' OR RESOURCE_NAME='FAILED_LOGIN_ATTEMPTS');
    return c_profile;
END;
/

CREATE OR REPLACE PROCEDURE modify_user_profile (
    p_username IN VARCHAR2,
    p_password IN VARCHAR2,
    p_profile_name IN VARCHAR2,
    p_tablespace_name IN VARCHAR2,
    p_quota IN VARCHAR,
    p_lock IN CHAR
)
IS
    v_need_password_update BOOLEAN := FALSE;
    v_need_lock_account BOOLEAN := FALSE;
BEGIN
    IF p_password IS NOT NULL AND LENGTH(TRIM(p_password)) > 0 THEN
        v_need_password_update := TRUE;
    END IF;

    IF p_profile_name IS NOT NULL AND LENGTH(TRIM(p_profile_name)) > 0 THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' PROFILE ' || p_profile_name;
    END IF;

    IF p_tablespace_name IS NOT NULL AND LENGTH(TRIM(p_tablespace_name)) > 0 THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' DEFAULT TABLESPACE ' || p_tablespace_name;
    END IF;
    
    IF p_quota IS NOT NULL AND LENGTH(TRIM(p_quota)) > 0 THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' QUOTA ' || p_quota || ' ON ' || p_tablespace_name;
    END IF;

    IF p_lock = '1' THEN
        v_need_lock_account := TRUE;
    END IF;

    IF v_need_password_update THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' IDENTIFIED BY ' || p_password;
    END IF;

    IF v_need_lock_account THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' ACCOUNT LOCK';
    ELSE
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' ACCOUNT UNLOCK';    
    END IF;
END modify_user_profile;
/




-- xoa profile
CREATE OR REPLACE PROCEDURE DROP_PROFILE(nameProfile in varchar2)
IS
begin
    execute immediate 'DROP PROFILE '||nameProfile||' ';
end;
/
CREATE OR REPLACE PROCEDURE modify_profile (
    p_profile_name IN VARCHAR2,
    p_new_profile_name IN VARCHAR2 DEFAULT NULL,
    p_resource_limit IN VARCHAR2 DEFAULT NULL
) AS
BEGIN
    EXECUTE IMMEDIATE 'ALTER PROFILE ' || p_profile_name || ' RENAME TO ' || p_new_profile_name;
    EXECUTE IMMEDIATE 'ALTER PROFILE ' || p_profile_name || ' LIMIT ' || p_resource_limit;
end modify_profile;
/
CREATE OR REPLACE PROCEDURE DROP_USER(nameUSER in varchar2)
IS
begin
    execute immediate 'DROP USER '||nameUSER||' CASCADE ';
end;
/

CREATE OR REPLACE FUNCTION get_user(
    p_user IN VARCHAR2
) RETURN SYS_REFCURSOR
AS
    result_user SYS_REFCURSOR;
BEGIN
    OPEN result_user FOR
    SELECT du.profile, du.DEFAULT_TABLESPACE, acc.*
    FROM dba_users du
    LEFT JOIN PANDA.ACCOUNT acc ON acc.username = du.username
    WHERE du.username = p_user;
    RETURN result_user;
END;
/
CREATE OR REPLACE PROCEDURE update_profile (
    p_profile_name IN VARCHAR2,
    p_resource_name IN VARCHAR2 DEFAULT NULL,
    p_resource_limit IN VARCHAR2 DEFAULT NULL
) AS
BEGIN
   EXECUTE IMMEDIATE 'ALTER PROFILE ' || p_profile_name || ' LIMIT ' || p_resource_name || ' ' || p_resource_limit;
END update_profile;
/










---------------- HOA
CREATE OR REPLACE FUNCTION get_user_info RETURN SYS_REFCURSOR IS
    user_info_cursor SYS_REFCURSOR;
BEGIN
    OPEN user_info_cursor FOR
    SELECT username, account_status, profile, created
    FROM dba_users
    ORDER BY username;
    
    RETURN user_info_cursor;
END;
/


--GRANT EXECUTE ON deleteaccount TO panda;

-- 
CREATE OR REPLACE PROCEDURE deleteaccount 
AS
    v_sql VARCHAR2(4000);
BEGIN
    DELETE FROM panda.task WHERE pid IN (SELECT id FROM panda.project WHERE username = user);

    DELETE FROM panda.project WHERE username = user;

    DELETE FROM panda.ACCOUNT WHERE username = user;

    EXECUTE IMMEDIATE 'DROP USER ' || user || ' CASCADE ';

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END deleteaccount;
/


CREATE OR REPLACE FUNCTION get_all_profiles RETURN SYS_REFCURSOR IS
    profiles_cursor SYS_REFCURSOR;
BEGIN
    OPEN profiles_cursor FOR
        SELECT profile, resource_name, resource_type, limit
        FROM dba_profiles order by profile;
    RETURN profiles_cursor;
END get_all_profiles;
/

--select profile, resource_name, resource_type, limit FROM dba_profiles;
--
--
--SELECT  username, profile
--FROM dba_users
--WHERE username = 'PANDA';
--
--GRANT EXECUTE ON panda.get_all_profiles TO panda;
--GRANT SELECT ON dba_profiles TO panda;


CREATE OR REPLACE PROCEDURE apply_profile_to_user (
    p_username VARCHAR2,
    p_profile_name VARCHAR2
) AS
    l_profile_count INTEGER;
    l_user_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO
    l_profile_count
    FROM dba_profiles
    WHERE profile = p_profile_name;

    IF l_profile_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Profile does not exist.');
    END IF;

    SELECT COUNT(*) INTO
    l_user_count
    FROM dba_users
    WHERE username = p_username;

    IF l_user_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'User does not exist.');
    END IF;

    EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' PROFILE ' || p_profile_name;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END apply_profile_to_user;
/


CREATE OR REPLACE PROCEDURE delete_profile_cascade (
    p_profile_name VARCHAR2
) AS
    l_username VARCHAR2(100);
BEGIN
    FOR user_rec IN (SELECT username FROM dba_users WHERE profile = p_profile_name) LOOP
        l_username := user_rec.username;

        EXECUTE IMMEDIATE 'ALTER USER ' || l_username || ' PROFILE DEFAULT';
    END LOOP;

    EXECUTE IMMEDIATE 'DROP PROFILE ' || p_profile_name;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END delete_profile_cascade;
/
