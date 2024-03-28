DECLARE
    vcount INTEGER := 0;
BEGIN
    SELECT
        COUNT(1)
    INTO vcount
    FROM
        dba_users
    WHERE
        username = 'PANDA';

    IF vcount != 0 THEN
        EXECUTE IMMEDIATE ( 'DROP USER panda CASCADE' );
    END IF;
END;
/

CREATE USER panda IDENTIFIED BY panda;

GRANT dba TO panda;

--GRANT EXECUTE ON DBMS_SESSION TO panda;

--GRANT SELECT, UPDATE, DELETE ON PANDA.ACCOUNT TO PANDA_USER_ROLE;

GRANT ALL PRIVILEGES TO panda;

GRANT SELECT ON v$sgainfo TO panda;

GRANT SELECT ON v$sga TO panda;

GRANT SELECT ON v$pgastat TO panda;

GRANT SELECT ON v$session TO panda;

GRANT SELECT ON v$process TO panda;

GRANT SELECT ON v$spparameter TO panda;

GRANT SELECT ON v$database TO panda;

GRANT SELECT ON v$instance TO panda;

GRANT SELECT ON v$datafile TO panda;

GRANT SELECT ON v$controlfile TO panda;

GRANT SELECT ON v$tablespace TO panda;

GRANT SELECT ON dba_tablespaces TO panda;

GRANT SELECT ON dba_tablespaces TO panda;

GRANT SELECT ON dba_users TO panda;
GRANT SELECT ON dba_sys_privs TO panda;
GRANT SELECT ON dba_tab_privs TO panda;
GRANT SELECT ON dba_role_privs TO panda;
GRANT SELECT ON dba_profiles to panda;
--conn panda/panda@//localhost:1521/orclpdb

ALTER SESSION SET current_schema = panda;

-- CREATE TABLES

CREATE TABLE account (
    avatar BLOB,
    ring_tone BLOB,
    username VARCHAR2(100) NOT NULL,
    fullname NVARCHAR2(100),
    CONSTRAINT pk_account PRIMARY KEY ( username )
);

CREATE TABLE project (
    id          NUMBER
        GENERATED BY DEFAULT AS IDENTITY,
    username    VARCHAR2(100) NOT NULL,
    name        NVARCHAR2(100),
    description NVARCHAR2(500),
    priority    NUMBER,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    started_at  TIMESTAMP,
    ended_at    TIMESTAMP,
    CONSTRAINT pk_project PRIMARY KEY ( id ),
    CONSTRAINT fk_project_account FOREIGN KEY ( username )
        REFERENCES account ( username )
);

CREATE TABLE task (
    id                NUMBER
        GENERATED BY DEFAULT AS IDENTITY,
    pid               NUMBER,
    description       NVARCHAR2(500) NOT NULL,
    done              NUMBER(1, 0) DEFAULT 0,
    type_date         NUMBER,
    is_full_day       NUMBER(1, 0) DEFAULT 0,
    type_loop         NUMBER,
    done_after_n_days NUMBER,
    priority          NUMBER,
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP,
    started_at        TIMESTAMP,
    ended_at          TIMESTAMP,
    CONSTRAINT pk_task PRIMARY KEY ( id ),
    CONSTRAINT fk_task_project FOREIGN KEY ( pid )
        REFERENCES project ( id )
);

CREATE TABLE collection (
    id       NUMBER
        GENERATED BY DEFAULT AS IDENTITY,
    username VARCHAR2(100) NOT NULL,
    name     NVARCHAR2(100) UNIQUE,
    CONSTRAINT pk_collection PRIMARY KEY ( id ),
    CONSTRAINT fk_collect_card_account FOREIGN KEY ( username )
        REFERENCES account ( username )
);

CREATE TABLE card (
    id                 NUMBER
        GENERATED BY DEFAULT AS IDENTITY,
    memory             NUMBER,
    n_learn            NUMBER,
    n_missed           NUMBER,
    learn_next_time_at TIMESTAMP,
    CONSTRAINT pk_card PRIMARY KEY ( id )
);

CREATE TABLE flashcard (
    cid   NUMBER,
    front NVARCHAR2(255),
    back  NVARCHAR2(255),
    CONSTRAINT pk_flashcard PRIMARY KEY ( cid ),
    CONSTRAINT fk_flashcard_card FOREIGN KEY ( cid )
        REFERENCES card ( id )
);

CREATE TABLE vocab (
    word  NVARCHAR2(100) NOT NULL,
    audio CLOB,
    ipa   NVARCHAR2(50),
    CONSTRAINT pk_vocab PRIMARY KEY ( word )
);
CREATE TABLE vocab_typevocab (
    word           NVARCHAR2(100),
    pos            NVARCHAR2(10),
    cid            NUMBER,
    meaning        NVARCHAR2(255) NOT NULL,
    n_write        NUMBER,
    n_write_missed NUMBER,
    CONSTRAINT pk_vocab_typevocab PRIMARY KEY ( word,
                                                pos ),
    CONSTRAINT fk_vocab_typevocab_vocab FOREIGN KEY ( word )
        REFERENCES vocab ( word ),
    CONSTRAINT fk_vocab_typevocab_card FOREIGN KEY ( cid )
        REFERENCES card ( id )
);

--CREATE TABLE example (
--    id       NUMBER
--        GENERATED BY DEFAULT AS IDENTITY,
--    sentence NVARCHAR2(100),
--    type     NUMBER,
--    CONSTRAINT pk_example PRIMARY KEY ( id )
--);
--
--CREATE TABLE example_vocab_typevocab (
--    sid  NUMBER,
--    word NVARCHAR2(100),
--    pos  NVARCHAR2(10),
--    CONSTRAINT pk_svt PRIMARY KEY ( sid,
--                                    word,
--                                    pos ),
--    CONSTRAINT fk_svt_example FOREIGN KEY ( sid )
--        REFERENCES example ( id ),
--    CONSTRAINT fk_svt_vocab_typevocab FOREIGN KEY ( word,
--                                                    pos )
--        REFERENCES vocab_typevocab ( word,
--                                     pos )
--);

CREATE TABLE sentence (
    id         NUMBER
        GENERATED BY DEFAULT AS IDENTITY,
    origin     NVARCHAR2(100),
    translated NVARCHAR2(100),
    type       NUMBER(3),
    CONSTRAINT pk_sentence PRIMARY KEY ( id )
);

CREATE TABLE sentence_vocab_typevocab (
    sid  NUMBER,
    word NVARCHAR2(100),
    pos  NVARCHAR2(10),
    CONSTRAINT pk_svt2 PRIMARY KEY ( sid,
                                     word,
                                     pos ),
    CONSTRAINT fk_svt_sentence2 FOREIGN KEY ( sid )
        REFERENCES sentence ( id ),
    CONSTRAINT fk_svt_vocab_typevocab2 FOREIGN KEY ( word,pos )
        REFERENCES vocab_typevocab ( word,pos )
);

CREATE TABLE collection_card (
    collect_id NUMBER,
    card_id    NUMBER,
    CONSTRAINT pk_collect_card PRIMARY KEY ( collect_id,
                                             card_id ),
    CONSTRAINT fk_collect_card_collect FOREIGN KEY ( collect_id )
        REFERENCES collection ( id ),
    CONSTRAINT fk_collect_card_card FOREIGN KEY ( card_id )
        REFERENCES card ( id )
);

-- PROCEDURE

CREATE OR REPLACE PROCEDURE add_account (
    p_avatar BLOB,
    p_ring_tone BLOB,
    p_username VARCHAR2,
    p_fullname NVARCHAR2,
    p_password VARCHAR2
) AS
    v_sql VARCHAR2(4000);
    v_project_id NUMBER;
    v_task_id NUMBER;
    up_username VARCHAR2(255);
BEGIN
    up_username := UPPER(p_username);
    v_sql := 'CREATE USER '
             || p_username
             || ' IDENTIFIED BY '
             || p_password;
    EXECUTE IMMEDIATE v_sql;
    v_sql := 'GRANT CREATE SESSION TO ' || p_username;    
    EXECUTE IMMEDIATE v_sql;
    -- tablespace quota, lock user
    
    v_sql := 'CREATE TABLESPACE ' || p_username || ' datafile ''D:\' || p_username || '.dbf'' size 100m AUTOEXTEND OFF';-- QUOTA 10MB ON ' || p_username ;
    EXECUTE IMMEDIATE v_sql;
    
    v_sql := 'ALTER USER ' || p_username || ' QUOTA 10M ON ' || p_username || '';
    EXECUTE IMMEDIATE v_sql;
    
    INSERT INTO ACCOUNT (avatar, ring_tone, username, fullname)
    VALUES (p_avatar, p_ring_tone, up_username, p_fullname);
    BEGIN
        INSERT INTO project (username, name, description, priority, created_at, updated_at)
        VALUES (up_username, 'Inboxes', null, 0, SYSTIMESTAMP, SYSTIMESTAMP)
        RETURNING id INTO v_project_id;
        
        BEGIN
            INSERT INTO task (pid, description, done, type_date, is_full_day, type_loop, done_after_n_days, priority, created_at, updated_at)
            VALUES (v_project_id, 'Inbox created automacally', 0, null, null, 0, 0, 0, SYSTIMESTAMP, SYSTIMESTAMP)
            RETURNING id INTO v_task_id;
        END;
    END;
    
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END add_account;
/

CREATE OR REPLACE PROCEDURE signout (
    p_username VARCHAR2
) AS
BEGIN
    FOR session_rec IN (
        SELECT
            sid,
            serial#
        FROM
            v$session
        WHERE
            username = p_username
            and
            sid != sys_context('USERENV', 'SID')
    ) LOOP
        EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION '''
                          || session_rec.sid
                          || ','
                          || session_rec.serial#
                          || ''' IMMEDIATE';
    END LOOP;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END signout;
/

--- PROCEDURE CARD

CREATE OR REPLACE FUNCTION select_cards_analysis_memory RETURN SYS_REFCURSOR IS
    v_cards_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cards_cursor FOR
        SELECT memory,
               COUNT(*) AS count
          FROM card
               LEFT JOIN collection_card cc ON cc.card_id = card.id
               LEFT JOIN collection c ON c.id = cc.collect_id
         WHERE c.username = user
      GROUP BY memory
      ORDER BY memory ASC;

    RETURN v_cards_cursor;
END select_cards_analysis_memory;
/



-- END PROCEDURE CARD

--- PROCEDURE VOCAB

-- c? 
--CREATE OR REPLACE PROCEDURE AddVocab (
--    p_word IN NVARCHAR2,
--    p_pos IN NVARCHAR2,
--    p_meaning IN NVARCHAR2,
--    p_audio IN CLOB,
--    p_ipa IN NVARCHAR2
--)
--AS
--    v_id NUMBER;
--BEGIN
--savepoint start_trans;
--    INSERT INTO Card (memory, n_learn, n_missed, learn_next_time_at)
--    VALUES (0, 0, 0, SYSDATE);
--
--    v_id := NULL; 
--
--    SELECT SEQ_VOCAB_TYPEVOCAB.CURRVAL INTO v_id FROM DUAL;
--
--    INSERT INTO Vocab_TypeVocab (word, pos, cid, meaning, n_write, n_write_missed)
--    VALUES (p_word, p_pos, v_id, p_meaning, 0, 0);
--
--    INSERT INTO Vocab (word, audio, ipa)
--    VALUES (p_word, p_audio, p_ipa);
--EXCEPTION
--    WHEN OTHERS THEN
--        ROLLBACK TO start_trans;
--        RAISE;
--END;
--/
-- m?i, th�m sequence 
--/

CREATE OR REPLACE PROCEDURE add_vocab (
    p_word    IN NVARCHAR2,
    p_pos     IN NVARCHAR2,
    p_meaning IN NVARCHAR2,
    p_audio   IN CLOB,
    p_ipa     IN NVARCHAR2
) IS
    l_id INT;
    v_card_id CARD.id%TYPE;
BEGIN
    INSERT INTO card (
        memory,
        n_learn,
        n_missed,
        learn_next_time_at
    ) VALUES (
        0,
        0,
        0,
        sysdate
    ) RETURNING id INTO v_card_id;

    INSERT INTO vocab (
        word,
        audio,
        ipa
    ) VALUES (
        p_word,
        p_audio,
        p_ipa
    );
    
    INSERT INTO vocab_typevocab (
        word,
        pos,
        cid,
        meaning,
        n_write,
        n_write_missed
    ) VALUES (
        p_word,
        p_pos,
        v_card_id,
        p_meaning,
        0,
        0
    );


    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
BEGIN add_vocab(
    'Spring',
    'noun',
    'the season of the year between winter and summer, lasting from March to June north of the equator, and from September to December south of the equator, when the weather becomes warmer, leaves and plants start to grow again and flowers appear',
    null,
    '/spr??/'
    );
END;
/
CREATE OR REPLACE FUNCTION select_vocab RETURN SYS_REFCURSOR
IS
    list_vocab SYS_REFCURSOR;
BEGIN
    OPEN list_vocab FOR SELECT
                               v.word, v.pos, v.meaning, s.origin
                            FROM
                                VOCAB_TYPEVOCAB v
                                LEFT JOIN CARD c
                                ON v.cid = c.id
                                LEFT JOIN COLLECTION_CARD CC
                                ON CC.CARD_ID = c.id
                                LEFT JOIN COLLECTION COL
                                ON CC.COLLECT_ID = COL.ID
                                LEFT JOIN sentence_vocab_typevocab svt
                                ON svt.word = v.word AND svt.pos = v.pos
                                LEFT JOIN sentence s
                                ON s.id = svt.sid
                                WHERE COL.username = user
                                ORDER BY v.word, v.pos
                                ;
    RETURN list_vocab;
END;
/

-- END PROCEDURE VOCAB

--- PROCEDURE SENTENCE

CREATE OR REPLACE FUNCTION select_sentences RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT s.*, svt.word, svt.pos
          FROM SENTENCE s
          JOIN SENTENCE_VOCAB_TYPEVOCAB svt
          ON svt.sid = s.id
          JOIN VOCAB_TYPEVOCAB vt
          ON vt.word = svt.word AND vt.pos = svt.pos
          LEFT JOIN card
          ON card.id = vt.cid
          LEFT JOIN COLLECTION_CARD cc
          ON cc.card_id = card.id
          LEFT JOIN Collection c
          ON c.id = cc.collect_id
          WHERE c.username = user;

    RETURN v_cursor;
END select_sentences;
/

CREATE OR REPLACE PROCEDURE add_sentence (
    origin     IN NVARCHAR2,
    translated IN NVARCHAR2,
    type       IN NUMBER,
    word       IN NVARCHAR2,
    pos        IN NVARCHAR2
) AS
    sid INT;
BEGIN
    INSERT INTO sentence (
        origin,
        translated,
        type
    ) VALUES (
        origin,
        translated,
        type
    ) RETURNING id INTO sid;

    INSERT INTO sentence_vocab_typevocab (
        sid,
        word,
        pos
    ) VALUES (
        sid,
        word,
        pos
    );

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END add_sentence;
/

-- END PROCEDURE SENTENCE

--- PROCEDURE COLLECTION

CREATE OR REPLACE FUNCTION select_collections RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT *
          FROM collection
         WHERE username = USER;

    RETURN v_cursor;
END select_collections;
/

CREATE OR REPLACE FUNCTION select_collections_analysis RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN

    OPEN v_cursor FOR
        SELECT c.id,
               c.name,
               COUNT(cc.card_id)  AS total_card,
               SUM(card.n_learn)  AS n_learn,
               SUM(card.n_missed) AS n_missed,
               COUNT(CASE WHEN card.memory > 5 THEN 1 ELSE NULL END) AS memory
          FROM panda.collection c
               LEFT JOIN panda.collection_card cc ON cc.collect_id = c.id
               LEFT JOIN panda.card card ON card.id = cc.card_id
         WHERE c.username = user
        GROUP BY c.name, c.id;

    RETURN v_cursor;
END select_collections_analysis;
/


CREATE OR REPLACE PROCEDURE delete_collection (
    name_param IN NVARCHAR2
) AS
BEGIN
    DELETE FROM collection
    WHERE
        name = name_param
        AND username = user;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
CREATE OR REPLACE PROCEDURE add_collection (
    name_param IN NVARCHAR2
) AS
BEGIN
    INSERT INTO collection (
        username,
        name
    ) VALUES (
        user,
        name_param
    );

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
        ROLLBACK;
END;
/

-- END PROCEDURE COLLECTION

--- PROCEDURE PROJECT

CREATE OR REPLACE FUNCTION select_projects RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT P.ID, P.NAME, P.DESCRIPTION, P.PRIORITY, P.CREATED_AT, P.UPDATED_AT, P.STARTED_AT, P.ENDED_AT, COUNT(CASE WHEN t.DONE = 1 THEN 1 ELSE NULL END)
        FROM panda.project P
        LEFT JOIN panda.task t ON t.pid = P.ID
        WHERE P.username = USER
        GROUP BY P.ID, P.NAME, P.DESCRIPTION, P.PRIORITY, P.CREATED_AT, P.UPDATED_AT, P.STARTED_AT, P.ENDED_AT;

    RETURN v_cursor;
END select_projects;
/



-- END PROCEDURE PROJECT

--- PROCEDURE TASK

CREATE OR REPLACE FUNCTION select_inboxes RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN

    OPEN v_cursor FOR
        SELECT t.*
          FROM task t
               RIGHT JOIN project p ON p.id = t.pid
         WHERE p.username = user AND p.name = 'Inboxes'
         ;

    RETURN v_cursor;
END select_inboxes;
/

CREATE OR REPLACE FUNCTION select_tasks RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT TASK.* FROM PROJECT P, TASK
    WHERE P.USERNAME = user AND TASK.PID = P.ID
    ORDER BY pid;
        
    RETURN v_cursor;
END;
/
CREATE OR REPLACE FUNCTION search_tasks
(
    key VARCHAR2
)
RETURN SYS_REFCURSOR 

IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT TASK.* FROM PROJECT P, TASK
    WHERE P.USERNAME = user AND TASK.PID = P.ID AND TASK.DESCRIPTION LIKE '%'|| key ||'%'
    ORDER BY pid;
        
    RETURN v_cursor;
END;
/
CREATE OR REPLACE FUNCTION select_tasks_today RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT t.*
          FROM task t
               RIGHT JOIN project p ON p.id = t.pid
         WHERE p.username = user
           AND p.ended_at <= SYSDATE
           AND p.started_at >= TRUNC(SYSDATE);

    RETURN v_cursor;
END select_tasks_today;
/


CREATE OR REPLACE FUNCTION select_tasks_today_by_project_id(
    p_pid    IN NUMBER
) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    v_username VARCHAR2(100);
BEGIN
    v_username := sys_context('USERENV', 'CURRENT_USER');

    OPEN v_cursor FOR
        SELECT t.*
          FROM task t
               RIGHT JOIN project p ON p.id = t.pid
         WHERE p.username = v_username
           AND p.ended_at <= SYSDATE
           AND p.started_at >= TRUNC(SYSDATE)
           AND t.pid = p_pid;

    RETURN v_cursor;
END select_tasks_today_by_project_id;
/

CREATE OR REPLACE PROCEDURE insert_task (
    p_pid               IN NUMBER,
    p_description       IN NCLOB,
    p_done              IN NUMBER,
    p_type_date         IN NUMBER,
    p_is_full_day       IN NUMBER,
    p_type_loop         IN NUMBER,
    p_done_after_n_days IN NUMBER,
    p_priority          IN NUMBER,
    p_created_at        IN DATE,
    p_updated_at        IN DATE,
    p_started_at        IN DATE,
    p_ended_at          IN DATE
) AS
BEGIN
    INSERT INTO task (
        pid,
        description,
        done,
        type_date,
        is_full_day,
        type_loop,
        done_after_n_days,
        priority,
        created_at,
        updated_at,
        started_at,
        ended_at
    ) VALUES (
        p_pid,
        p_description,
        p_done,
        p_type_date,
        p_is_full_day,
        p_type_loop,
        p_done_after_n_days,
        p_priority,
        p_created_at,
        p_updated_at,
        p_started_at,
        p_ended_at
    );

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
SELECT * FROM task;
CREATE OR REPLACE PROCEDURE insert_inbox (
    p_description IN NCLOB
) AS
    v_pid INT;
BEGIN
    SELECT
        id
    INTO v_pid
    FROM
        project
    WHERE
        project.username = user
        AND
        project.name = 'Inboxes';

    INSERT INTO task (
        pid,
        description
    ) VALUES (
        v_pid,
        p_description
    );

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE delete_task AS
    v_tid INT;
BEGIN
    SELECT
        t.id
    INTO v_tid
    FROM
        task    t
        LEFT JOIN project p ON p.id = t.pid
    WHERE
        p.username = sys_context('USERENV', 'CURRENT_USER');

    DELETE FROM task
    WHERE
        task.id = v_tid;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- END PROCEDURE TASK


DROP ROLE PANDA_USER_ROLE;

CREATE ROLE PANDA_USER_ROLE;
GRANT CONNECT TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.ADD_COLLECTION TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.ADD_SENTENCE TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.ADD_VOCAB TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.DELETE_COLLECTION TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.DELETE_TASK TO PANDA_USER_ROLE;
--GRANT EXECUTE ON PANDA.GET_CARDS_ANALYSISMEMORY TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.INSERT_INBOX TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.INSERT_TASK TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_CARDS_ANALYSIS_MEMORY TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_COLLECTIONS TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_COLLECTIONS_ANALYSIS TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_INBOXES TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_PROJECTS TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_TASKS TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_TASKS_TODAY TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_TASKS_TODAY_BY_PROJECT_ID TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SIGNOUT TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.SELECT_VOCAB TO PANDA_USER_ROLE;
GRANT EXECUTE ON PANDA.search_tasks TO PANDA_USER_ROLE;
GRANT SELECT ON PANDA.COLLECTION TO PANDA_USER_ROLE;
GRANT SELECT ON PANDA.CARD TO PANDA_USER_ROLE;
GRANT SELECT ON PANDA.COLLECTION_CARD TO PANDA_USER_ROLE;

--REVOKE SELECT ON PANDA.COLLECTION FROM PANDA_USER_ROLE;
--REVOKE SELECT ON PANDA.CARD FROM PANDA_USER_ROLE;
--REVOKE SELECT ON PANDA.COLLECTION_CARD FROM PANDA_USER_ROLE;

DECLARE
    vcount INTEGER := 0;
BEGIN
    SELECT
        COUNT(1)
    INTO vcount
    FROM
        dba_users
    WHERE
        username = 'PANDA_REGISTER';

    IF vcount != 0 THEN
        EXECUTE IMMEDIATE ( 'DROP USER PANDA_REGISTER CASCADE' );
    END IF;
END;
/

CREATE USER panda_register IDENTIFIED BY panda_register;

GRANT connect TO panda_register;

GRANT EXECUTE ON panda.add_account TO panda_register;

DECLARE
    vcount INTEGER := 0;
BEGIN
    SELECT
        COUNT(1)
    INTO vcount
    FROM
        dba_users
    WHERE
        username = 'PANDA_USER';

    IF vcount != 0 THEN
        EXECUTE IMMEDIATE ( 'DROP USER PANDA_USER CASCADE' );
    END IF;
END;
/

--CREATE USER panda_user IDENTIFIED BY panda_user;
call add_account(null, null, 'panda_user', 'PANDA USER', 'panda_user');
/

GRANT PANDA_USER_ROLE TO panda_user;
GRANT PANDA_USER_ROLE TO panda;
GRANT SELECT ON PROJECT TO panda;

Alter session set current_schema=panda;
--DECLARE
--    v_clob CLOB;
--    v_blob BLOB;
--    v_bfile BFILE;
--BEGIN
--    v_bfile := BFILENAME('C:\Users\Khoa\Pictures', 'meme-meo-cuoi.jpg');
--
--    DBMS_LOB.fileopen(v_bfile, DBMS_LOB.file_readonly);
--
--    DBMS_LOB.loadfromfile(v_blob, v_bfile, DBMS_LOB.getlength(v_bfile));
--
--    DBMS_LOB.createtemporary(v_clob, TRUE);
--    DBMS_LOB.converttoclob(dest_lob => v_clob,
--                            src_blob => v_blob,
--                            amount => DBMS_LOB.getLength(v_blob),
--                            dest_offset => 1,
--                            src_offset => 1,
--                            bfile_csid => DBMS_LOB.default_csid,
--                            lang_context => DBMS_LOB.default_lang_ctx,
--                            warning => DBMS_LOB.warnings);
--
--    INSERT INTO account (avatar, username, fullname) VALUES (v_clob, 'panda_user', 'PANDA USER');
--
--    DBMS_LOB.fileclose(v_bfile);
--    DBMS_LOB.freetemporary(v_clob);
--END;
--/


--INSERT INTO ACCOUNT (AVATAR, USERNAME, FULLNAME) VALUES ('panda_user', 'PANDA USER');


INSERT INTO project (username, name, description, priority, created_at, updated_at, started_at, ended_at)
VALUES ('PANDA_USER', 'oracle', NULL, 1, TO_DATE('20/12/2023', 'DD/MM/YYYY'), TO_DATE('05/01/2024', 'DD/MM/YYYY'), TO_DATE('20/01/2024', 'DD/MM/YYYY'), TO_DATE('01/06/2024', 'DD/MM/YYYY'));

INSERT INTO task (pid, description, priority, created_at)
VALUES (2, 'Write unit tests', 2, TO_DATE('2024-03-05', 'YYYY-MM-DD'));

INSERT INTO task (pid, description, priority, created_at)
VALUES (2, 'Fix bugs', 3, TO_DATE('2024-03-05', 'YYYY-MM-DD'));

INSERT INTO task (pid, description, priority, created_at)
VALUES (2, 'Deploy to production', 1, TO_DATE('2024-03-05', 'YYYY-MM-DD'));

INSERT INTO collection (username, name) VALUES
('PANDA_USER', 'Oracle');

INSERT INTO card (memory, n_learn, n_missed, learn_next_time_at)
VALUES
(0, 0, 0, null);

INSERT INTO card (memory, n_learn, n_missed, learn_next_time_at)
VALUES
(0, 0, 0, null);

INSERT INTO card (memory, n_learn, n_missed, learn_next_time_at)
VALUES
(0, 0, 0, null);

INSERT INTO card (memory, n_learn, n_missed, learn_next_time_at)
VALUES
(0, 0, 0, null);

INSERT INTO flashcard (cid, front, back) VALUES (1, 'Oracle database l� g�?', 'Oracle database l� m?t h? qu?n tr? c? s? d? li?u quan h? (RDBMS) ???c ph�t tri?n b?i Oracle Corporation. N� ???c s? d?ng r?ng r�i trong c�c doanh nghi?p v� t? ch?c ?? l?u tr? v� qu?n l� d? li?u.');
INSERT INTO flashcard (cid, front, back) VALUES (2, 'C�c phi�n b?n oracle', 'Enterprise edition, standard edition, express edition, oracle lite');
INSERT INTO flashcard (cid, front, back) VALUES (3, 'Express edition d�ng cho h? ?i?u h�nh n�o?', 'Linux v� windows');
INSERT INTO flashcard (cid, front, back) VALUES (4, 'Machine learning c� t? phi�n b?n n�o c?a oracle?', '18c');



INSERT INTO collection_card (collect_id, card_id) VALUES (1, 1);
INSERT INTO collection_card (collect_id, card_id) VALUES (1, 2);
INSERT INTO collection_card (collect_id, card_id) VALUES (1, 3);
INSERT INTO collection_card (collect_id, card_id) VALUES (1, 4);

-- 2
INSERT INTO collection (username, name) values
('PANDA_USER', 'English');


INSERT INTO card (memory, n_learn, n_missed, learn_next_time_at)
VALUES
(0, 0, 0, null);
/
INSERT INTO collection_card (collect_id, card_id) VALUES (2, 5);


INSERT INTO vocab(word, audio, ipa)
values
    ('healthfully', null, '/?hel?.f?l.i/');

INSERT INTO vocab_typevocab(word, pos, cid, meaning, n_write, n_write_missed)
values
    ('healthfully', 'adverb', 5, 'in a way that helps you to produce good health', 0, 0);
    
INSERT INTO sentence(origin, translated, type) VALUES ('The website gives advice to dieters for ways to eat more healthfully and lose weight.', null, 0);
INSERT INTO sentence(origin, translated, type) VALUES ('Living healthfully requires being active and getting regular exercise.', null, 0);
/
INSERT INTO SENTENCE_VOCAB_TYPEVOCAB (sid, word, pos) VALUES (1, 'healthfully', 'adverb');
INSERT INTO SENTENCE_VOCAB_TYPEVOCAB (sid, word, pos) VALUES (2, 'healthfully', 'adverb');
/
--INSERT INTO ACCOUNT VALUES ('panda', '123');
/
--INSERT INTO PANDA.PROJECT (username, name, description, created_at, updated_at, started_at, ended_at) 
--VALUES ('panda', 'panda', '', null, null, null, null);
/