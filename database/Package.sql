--- Project package 

ALTER SESSION SET CURRENT_SCHEMA=PANDA;

CREATE OR REPLACE PACKAGE ProjectPackage AS
    PROCEDURE AddProject(
        p_name NVARCHAR2,
        p_description NVARCHAR2,
        p_priority NUMBER,
        p_started_at TIMESTAMP,
        p_ended_at TIMESTAMP
    );
    
    PROCEDURE UpdateProject(
        p_pid INT,
        p_name_old NVARCHAR2,
        p_name NVARCHAR2,
        p_description NVARCHAR2,
        p_priority NUMBER,
        p_started_at TIMESTAMP,
        p_ended_at TIMESTAMP
    );
    
    PROCEDURE DeleteProject(
        p_pid INT
    );
    
END ProjectPackage;
/

CREATE OR REPLACE PACKAGE BODY ProjectPackage AS

    PROCEDURE AddProject(
        p_name NVARCHAR2,
        p_description NVARCHAR2,
        p_priority NUMBER,
        p_started_at TIMESTAMP,
        p_ended_at TIMESTAMP
    ) IS
    BEGIN
        INSERT INTO PROJECT (username, name, description, priority, started_at, ended_at)
        VALUES (
            user,
            p_name,
            p_description,
            p_priority,
            p_started_at,
            p_ended_at
        );
        COMMIT;
        EXCEPTION
            WHEN OTHERS THEN
                ROLLBACK;
                RAISE;
    END AddProject;

    PROCEDURE UpdateProject(
        p_pid INT,
        p_name_old NVARCHAR2,
        p_name NVARCHAR2,
        p_description NVARCHAR2,
        p_priority NUMBER,
        p_started_at TIMESTAMP,
        p_ended_at TIMESTAMP
    ) IS
    BEGIN
        UPDATE PROJECT SET
            name = p_name,
            description = p_description,
            priority = p_priority,
            started_at = p_started_at,
            ended_at = p_ended_at
        WHERE username = user AND id = p_pid;
        COMMIT;
        EXCEPTION
            WHEN OTHERS THEN
                ROLLBACK;
                RAISE;
    END UpdateProject;

    PROCEDURE DeleteProject(
        p_pid INT
    ) IS
    BEGIN
        DELETE FROM PROJECT WHERE id = p_pid;
        COMMIT;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE;
                ROLLBACK;
    END DeleteProject;

END ProjectPackage;
/


-- Task package 
CREATE OR REPLACE PACKAGE TaskPackage AS
    PROCEDURE AddTask(
        p_pid               NUMBER,
        p_description       NVARCHAR2,
        p_done              NUMBER,
        p_type_date         NUMBER,
        p_is_full_day       NUMBER,
        p_type_loop         NUMBER,
        p_done_after_n_days NUMBER,
        p_priority          NUMBER,
        p_created_at        TIMESTAMP,
        p_updated_at        TIMESTAMP,
        p_started_at        TIMESTAMP,
        p_ended_at          TIMESTAMP
    );

    FUNCTION SelectTaskByDescription(
        p_pid INT,
        p_tid INT
    ) RETURN SYS_REFCURSOR;

    PROCEDURE UpdateTask(
        p_tid               NUMBER,
        p_pid               NUMBER,
        p_description       NVARCHAR2,
        p_done              NUMBER,
        p_type_date         NUMBER,
        p_is_full_day       NUMBER,
        p_type_loop         NUMBER,
        p_done_after_n_days NUMBER,
        p_priority          NUMBER,
        p_created_at        TIMESTAMP,
        p_updated_at        TIMESTAMP,
        p_started_at        TIMESTAMP,
        p_ended_at          TIMESTAMP
    );

    PROCEDURE DeleteTask(
        p_tid NUMBER
    );

END TaskPackage;
/

CREATE OR REPLACE PACKAGE BODY TaskPackage AS

    PROCEDURE AddTask(
        p_pid               NUMBER,
        p_description       NVARCHAR2,
        p_done              NUMBER,
        p_type_date         NUMBER,
        p_is_full_day       NUMBER,
        p_type_loop         NUMBER,
        p_done_after_n_days NUMBER,
        p_priority          NUMBER,
        p_created_at        TIMESTAMP,
        p_updated_at        TIMESTAMP,
        p_started_at        TIMESTAMP,
        p_ended_at          TIMESTAMP
    ) IS
    BEGIN
        INSERT INTO TASK(
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
        )
        VALUES (
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
    END AddTask;

    FUNCTION SelectTaskByDescription(
        p_pid INT,
        p_tid INT
    ) RETURN SYS_REFCURSOR IS
        list_task SYS_REFCURSOR;
    BEGIN
        OPEN list_task FOR
        SELECT T.*
        FROM TASK T, PROJECT P
        WHERE P.username = user AND T.pid = P.id AND P.id = p_pid AND T.id = p_tid;
        RETURN list_task;
    END SelectTaskByDescription;

    PROCEDURE UpdateTask(
        p_tid               NUMBER,
        p_pid               NUMBER,
        p_description       NVARCHAR2,
        p_done              NUMBER,
        p_type_date         NUMBER,
        p_is_full_day       NUMBER,
        p_type_loop         NUMBER,
        p_done_after_n_days NUMBER,
        p_priority          NUMBER,
        p_created_at        TIMESTAMP,
        p_updated_at        TIMESTAMP,
        p_started_at        TIMESTAMP,
        p_ended_at          TIMESTAMP
    ) IS
    BEGIN
        UPDATE TASK SET
            pid               = p_pid,
            description       = p_description,
            done              = p_done,
            type_date         = p_type_date,
            is_full_day       = p_is_full_day,
            type_loop         = p_type_loop,
            done_after_n_days = p_done_after_n_days,
            priority          = p_priority,
            created_at        = p_created_at,
            updated_at        = p_updated_at,
            started_at        = p_started_at,
            ended_at          = p_ended_at
        WHERE id = p_tid;
        COMMIT;
        EXCEPTION
            WHEN OTHERS THEN
                ROLLBACK;
                RAISE;
    END UpdateTask;

    PROCEDURE DeleteTask(
        p_tid NUMBER
    ) IS
        v_tid NUMBER;
    BEGIN
        SELECT T.id INTO v_tid
        FROM TASK T, PROJECT P
        WHERE T.pid = P.id AND P.username = USER AND T.id = p_tid;
        
        IF v_tid IS NOT NULL THEN
            DELETE FROM TASK WHERE id = p_tid;
        ELSE
            RAISE_APPLICATION_ERROR(-20001, 'Task not found!');
        END IF;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE;
                ROLLBACK;
    END DeleteTask;

END TaskPackage;
/


-- Flashcard Package
CREATE OR REPLACE PACKAGE FlashcardPackage AS
    PROCEDURE AddFlashcard(
        p_front NVARCHAR2,
        p_back NVARCHAR2,
        p_cid INT
    );

    FUNCTION SelectCards(
        p_cid INT
    ) RETURN SYS_REFCURSOR;

END FlashcardPackage;
/

CREATE OR REPLACE PACKAGE BODY FlashcardPackage AS

    PROCEDURE AddFlashcard(
        p_front NVARCHAR2,
        p_back NVARCHAR2,
        p_cid INT
    ) IS
        v_card_id CARD.id%TYPE;
    BEGIN
        BEGIN
            INSERT INTO CARD (memory, n_learn, n_missed, learn_next_time_at)
            VALUES (0, 0, 0, null) RETURNING id INTO v_card_id;

            INSERT INTO FLASHCARD (cid, front, back) VALUES (v_card_id, p_front, p_back);
            INSERT INTO collection_card (collect_id, card_id) VALUES (p_cid, v_card_id);
            COMMIT;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE;
                ROLLBACK;
        END;
    END AddFlashcard;

    FUNCTION SelectCards(
        p_cid INT
    ) RETURN SYS_REFCURSOR IS
        list_cards SYS_REFCURSOR;
    BEGIN
        OPEN list_cards FOR
        SELECT c.ID AS CARD_ID,
               co.ID AS COLLECTION_ID,
               f.FRONT,
               f.BACK,
               v.WORD,
               v.MEANING,
               v.POS
        FROM COLLECTION co
                 LEFT JOIN COLLECTION_CARD cc ON cc.COLLECT_ID = co.ID
                 LEFT JOIN CARD c ON c.ID = cc.CARD_ID
                 LEFT JOIN FLASHCARD f ON f.CID = c.ID
                 LEFT JOIN VOCAB_TYPEVOCAB v ON v.CID = c.ID
        WHERE co.USERNAME = user AND co.ID = p_cid;

        RETURN list_cards;
    END SelectCards;

END FlashcardPackage;
/

