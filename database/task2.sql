alter session set current_schema=panda;
--- Task Week 2
create or replace FUNCTION GetControlfileInfo RETURN SYS_REFCURSOR
IS
    Controlfile_info SYS_REFCURSOR;
BEGIN
    OPEN Controlfile_info FOR
    SELECT status, name, is_recovery_dest_file, block_size FROM v$CONTROLFILE;
    RETURN Controlfile_info;
END;
/
--

create or replace FUNCTION GetDatabaseInfo RETURN SYS_REFCURSOR
IS
    Database_info SYS_REFCURSOR;
BEGIN
    OPEN Database_info FOR
    SELECT name, created, log_mode, open_mode, database_role FROM V$DATABASE; 
    RETURN Database_info;
END;
/
-- 
create or replace FUNCTION GetDatafileInfo RETURN SYS_REFCURSOR
IS
    Datafile_info SYS_REFCURSOR;
BEGIN
    OPEN Datafile_info FOR
    select creation_change#, foreign_creation_time, name, con_id from v$datafile; 
    RETURN Datafile_info;
END;
/
--
create or replace FUNCTION GetInstanceInfo RETURN SYS_REFCURSOR
IS
    Instance_info SYS_REFCURSOR;
BEGIN
    OPEN Instance_info FOR
    select instance_name, host_name, startup_time, status from v$instance; 
    RETURN Instance_info;
END;
/
--

create or replace FUNCTION GetPgaStat RETURN SYS_REFCURSOR
IS
    Pga_stat SYS_REFCURSOR;
BEGIN
    OPEN Pga_stat FOR
    SELECT * FROM v$pgastat;
    RETURN Pga_stat;
END;
/
--

create or replace FUNCTION GetProcessInfo RETURN SYS_REFCURSOR
IS
    Process_info SYS_REFCURSOR;
BEGIN
    OPEN Process_info FOR
    SELECT ADDR, PID, SERIAL#, TRACEFILE FROM V$PROCESS; 
    RETURN Process_info;
END;
/
--
create or replace FUNCTION GetSgaInfo RETURN SYS_REFCURSOR
IS
    Sga_info SYS_REFCURSOR;
BEGIN
    OPEN Sga_info FOR
    SELECT * FROM v$sgainfo;

    RETURN Sga_info;
END;
/
--

create or replace FUNCTION GetSpfileInfo RETURN SYS_REFCURSOR
IS
    Spfile_info SYS_REFCURSOR;
BEGIN
    OPEN Spfile_info FOR
    SELECT name, type, value, ordinal FROM V$SPPARAMETER; 
    RETURN Spfile_info;
END;
/

CREATE OR REPLACE FUNCTION SelectSession RETURN SYS_REFCURSOR
IS
    data_session SYS_REFCURSOR;
BEGIN
    OPEN data_session FOR
    SELECT * FROM v$SESSION WHERE type!='BACKGROUND';
    
    RETURN data_session;

END;
/
-- m?i 
CREATE OR REPLACE FUNCTION SelectSession RETURN SYS_REFCURSOR
IS
    data_session SYS_REFCURSOR;
BEGIN
    OPEN data_session FOR
    SELECT SID, serial#, USERNAME, STATUS, OSUSER, MACHINE
    FROM v$SESSION 
    WHERE type!='BACKGROUND';
    
    RETURN data_session;
END;
/

--
CREATE OR REPLACE FUNCTION GetTableSpaces RETURN SYS_REFCURSOR
IS
    data_tablespaces SYS_REFCURSOR;
BEGIN
    OPEN data_tablespaces FOR
    SELECT * FROM dba_tablespaces;
    RETURN data_tablespaces;
END;
/

-- m?i 
CREATE OR REPLACE FUNCTION GetTableSpaces RETURN SYS_REFCURSOR
IS
    data_tablespaces SYS_REFCURSOR;
BEGIN
    OPEN data_tablespaces FOR
    SELECT tablespace_name, contents, status, logging, extent_management
    FROM dba_tablespaces;
    RETURN data_tablespaces;
END;
/


--- La Ho?ng Ph?c

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
    location_tbs IN VARCHAR2,
    size_tbs IN VARCHAR2
)
AS
    v_sql VARCHAR2(3000);
BEGIN
    v_sql := 'CREATE TABLESPACE ' || name_tbs || ' datafile ''' || location_tbs || ''' size ' || size_tbs || 'm' ;
    EXECUTE IMMEDIATE v_sql;
END AddTableSpaces;
/

create or replace PROCEDURE AddTableSpacesAndManyDatafiles (
    name_tbs IN VARCHAR2,
    seq_datafiles IN VARCHAR2
)
AS
    v_sql VARCHAR2(3000);
BEGIN
    v_sql := 'CREATE TABLESPACE ' || name_tbs || ' datafile ' || seq_datafiles ;
    EXECUTE IMMEDIATE v_sql;
END AddTableSpacesAndManyDatafiles;
/

-- KHOA
CREATE OR REPLACE PROCEDURE add_datafile(
    tablespace_name IN VARCHAR2,
    location_dt IN VARCHAR2,
    size_dt IN VARCHAR2
)
AS
    v_sql VARCHAR2(3000);
BEGIN
    v_sql := 'ALTER TABLESPACE ' || tablespace_name || ' ADD DATAFILE '''|| location_dt ||''' SIZE ' || size_dt ||'M';
    EXECUTE IMMEDIATE v_sql;
END add_datafile;
/