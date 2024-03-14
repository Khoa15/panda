-- Task 4
alter session set current_schema=panda;
select * from dba_sys_privs where grantee='PANDA' ORDER BY GRANTEE;

select * from dba_tab_privs where grantee='PANDA' OR grantee='PANDA_REGISTER' OR grantee='PANDA_USER_ROLE' order by grantee;

select * from dba_roles order by role;
select * from dba_role_privs order by grantee;
select * from role_tab_privs where role = 'PANDA_USER_ROLE' OR role = 'PANDA_REGISTER';
select * from role_tab_privs order by role;
SELECT * FROM all_procedures WHERE OWNER='PANDA' ORDER BY OBJECT_NAME;
select * from all_tables WHERE OWNER='PANDA';
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
    SELECT * FROM all_procedures WHERE OWNER='PANDA' ORDER BY OBJECT_NAME;
    RETURN list_procedures;
END;
/

CREATE OR REPLACE FUNCTION get_panda_tables RETURN SYS_REFCURSOR
IS
    list_tables SYS_REFCURSOR;
BEGIN
    OPEN list_tables FOR
    SELECT * FROM all_tables WHERE OWNER='PANDA' ORDER BY TABLE_NAME;
    RETURN list_tables;
END;
/
--
--CREATE OR REPLACE FUNCTION get_dba_tab_privs RETURN SYS_REFCURSOR
--IS
--    list_tab_privs SYS_REFCURSOR;
--BEGIN
--    OPEN list_tab_privs FOR
--    select * from dba_tab_privs order by grantee;
--
--    RETURN list_tab_privs;
--END;
--/
--
--CREATE OR REPLACE FUNCTION get_dba_role_privs RETURN SYS_REFCURSOR
--IS
--    list_role_privs SYS_REFCURSOR;
--BEGIN
--    OPEN list_role_privs FOR
--    select * from dba_role_privs order by grantee;
--
--    RETURN list_role_privs;
--END;
--/

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
    p_lock INT
)
IS
BEGIN
    EXECUTE IMMEDIATE 'CREATE USER '
             || p_username
             || ' IDENTIFIED BY '
             || p_password;
    EXECUTE IMMEDIATE 'GRANT CREATE SESSION, CONNECT TO ' || p_username;
    EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' PROFILE ' || p_profile_name;
    EXECUTE IMMEDIATE 'GRANT PANDA_USER_ROLE TO ' || p_username;
    IF p_lock = 1 THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' ACCOUNT LOCK';
    END IF;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END ADD_ACCOUNT_PROFILE;
/

CREATE OR REPLACE FUNCTION GET_ALL_PROFILES RETURN SYS_REFCURSOR
IS
    c_profile SYS_REFCURSOR;
BEGIN
    OPEN c_profile FOR
    SELECT * FROM dba_profiles ORDER BY PROFILE;
    RETURN c_profile;
END;
/

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
            isChangePassword IN INT,
            p_profile_name IN VARCHAR2,
            isLock IN INT
)
IS
BEGIN
    EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' PROFILE ' || p_profile_name;
    
    IF isChangePassword = 1 THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' IDENTIFIED BY ' || p_password;
    END IF;
    IF isLock = 1 THEN
        EXECUTE IMMEDIATE 'ALTER USER ' || p_username || ' ACCOUNT LOCK';
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
        FROM dba_profiles;
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
