alter session set current_schema=panda;
--- Task Week 2
create or replace PROCEDURE GetControlfileInfo (Controlfile_info OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Controlfile_info FOR
    SELECT status, name, is_recovery_dest_file, block_size FROM v$CONTROLFILE;
END;
/
--

create or replace PROCEDURE GetDatabaseInfo (Database_info OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Database_info FOR
    SELECT name, created, log_mode, open_mode, database_role FROM V$DATABASE; 
END;
/
-- 
create or replace PROCEDURE GetDatafileInfo (Datafile_info OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Datafile_info FOR
    select creation_change#, foreign_creation_time, name, con_id from v$datafile; 
END;
/
--
create or replace PROCEDURE GetInstanceInfo (Instance_info OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Instance_info FOR
    select instance_name, host_name, startup_time, status from v$instance; 
END;
/
--

create or replace PROCEDURE GetPgaStat (Pga_stat OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Pga_stat FOR
    SELECT * FROM v$pgastat;
END;
/
--

create or replace PROCEDURE GetProcessInfo (Process_info OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Process_info FOR
    SELECT ADDR, PID, SERIAL#, TRACEFILE FROM V$PROCESS; 
END;
/
--
create or replace PROCEDURE GetSgaInfo (Sga_info OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Sga_info FOR
    SELECT * FROM v$sgainfo;
END;
/
--

create or replace PROCEDURE GetSpfileInfo (Spfile_info OUT SYS_REFCURSOR) AS
BEGIN
    OPEN Spfile_info FOR
    SELECT name, type, value, ordinal FROM V$SPPARAMETER; 
END;
/

CREATE OR REPLACE PROCEDURE SelectSession(
    data_session OUT SYS_REFCURSOR
)
AS
BEGIN
    OPEN data_session FOR
    SELECT * FROM v$SESSION WHERE type!='BACKGROUND';
--    SELECT S.USERNAME, P.SPID, S.SERIAL#, S.MACHINE, S.PROGRAM, S.STATUS, S.SQL_ID, S.SERVER
--    FROM v$session S
--    JOIN v$process P ON S.PADDR = P.ADDR
--    WHERE S.type != 'background';

END;
/

CREATE OR REPLACE PROCEDURE GetTableSpaces(
    data_tablespaces OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN data_tablespaces FOR
    SELECT * FROM dba_tablespaces;
END;
/

--- La Hoàng Phúc

CREATE OR REPLACE FUNCTION get_process_with_session (
    s_id VARCHAR2
) RETURN SYS_REFCURSOR IS
    pro_session SYS_REFCURSOR;
BEGIN
-- open cursor
    OPEN pro_session FOR SELECT
                        s.sid,
                        s.serial#,
                        p.spid,
                        s.username,
                        s.program
                    FROM
                        v$session   s
                        JOIN v$process   p ON s.paddr = p.addr
                    WHERE
                        s.sid = s_id;
  -- Return the cursor
    RETURN pro_session;
END;
/

CREATE OR REPLACE FUNCTION is_user_signed_in (
    p_username VARCHAR2
) RETURN BOOLEAN IS
    v_session_count NUMBER;
BEGIN
    SELECT
        COUNT(*)
    INTO v_session_count
    FROM
        v$session
    WHERE
        username = p_username
        AND program IS NOT NULL;
    RETURN v_session_count > 0;
END;
/


CREATE OR REPLACE FUNCTION get_tablespaces RETURN SYS_REFCURSOR IS
-- Declare cursor variable
    c_table_spaces SYS_REFCURSOR;
BEGIN
-- open cursor
    OPEN c_table_spaces FOR SELECT
                                tablespace_name,
                                contents,
                                status,
                                extent_management,
                                allocation_type,
                                logging
                            FROM
                                dba_tablespaces;
  -- Return the cursor

    RETURN c_table_spaces;
END;
/

create or replace PROCEDURE AddTableSpaces (
    name_tbs IN VARCHAR2,
    size_tbs IN VARCHAR2
)
AS
    v_sql VARCHAR2(3000);
BEGIN
    v_sql := 'CREATE TABLESPACE ' || name_tbs || ' datafile ' || 'D:\APP\ASUS\ORADATA\ORCL\' || name_tbs || '.dbf size ' || size_tbs || 'm' ;
    EXECUTE IMMEDIATE v_sql;
END AddTableSpaces;
/