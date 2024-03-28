ALTER SESSION SET CURRENT_SCHEMA=PANDA;

--CREATE OR REPLACE FUNCTION SELECT_INBOXES
--RETURN SYS_REFCURSOR
--IS
--    list_inboxes SYS_REFCURSOR;
--BEGIN
--    OPEN list_inboxes FOR
--        SELECT T.* FROM PROJECT
--        LEFT JOIN TASK T
--        ON T.PID = PROJECT.ID
--        WHERE USERNAME = user AND NAME = 'Inboxes';
--    RETURN list_inboxes;
--END;
--/

CREATE OR REPLACE PROCEDURE add_task(
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
)
IS
BEGIN
    INSERT INTO TASK(
        pid               ,
        description       ,
        done              ,
        type_date         ,
        is_full_day       ,
        type_loop         ,
        done_after_n_days ,
        priority          ,
        created_at        ,
        updated_at        ,
        started_at        ,
        ended_at          
    )
    VALUES
        (
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
GRANT EXECUTE ON ADD_TASK TO PANDA_USER_ROLE;

CREATE OR REPLACE FUNCTION select_task_by_description
(
    p_pid INT,
    p_tid INT
) RETURN SYS_REFCURSOR
IS
    list_task SYS_REFCURSOR;
BEGIN
    OPEN list_task FOR
    SELECT T.* FROM TASK T, PROJECT P 
    WHERE P.username = user AND T.PID = P.ID AND P.ID = p_pid AND T.ID = p_tid;
    RETURN list_task;
END;
/
GRANT EXECUTE ON select_task_by_description TO PANDA_USER_ROLE;


CREATE OR REPLACE PROCEDURE Update_Task
(
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
)
IS
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
    WHERE ID = p_tid;
    COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
END;
/
GRANT EXECUTE ON UPDATE_TASK TO PANDA_USER_ROLE;

CREATE OR REPLACE PROCEDURE DELETE_TASK
(
    p_tid NUMBER
)
IS
    v_tid NUMBER;
BEGIN
    
    SELECT T.ID INTO v_tid FROM TASK T, PROJECT P
    WHERE T.PID = P.ID AND P.USERNAME = USER AND T.ID = p_tid;
    IF v_tid IS NOT NULL THEN
        DELETE FROM TASK WHERE TASK.ID = p_tid;        
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Task not found!');
    END IF;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE;
            ROLLBACK;
END;
/
GRANT EXECUTE ON DELETE_TASK TO PANDA_USER_ROLE;

SELECT * FROM TASK;
