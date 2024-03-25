ALTER SESSION SET CURRENT_SCHEMA = PANDA;

CREATE OR REPLACE PROCEDURE add_project
(
    p_name NVARCHAR2,
    p_description NVARCHAR2,
    p_priority NUMBER,
    p_started_at TIMESTAMP,
    p_ended_at TIMESTAMP
)
IS
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
END;
/
GRANT EXECUTE ON add_project TO PANDA_USER_ROLE;

CREATE OR REPLACE PROCEDURE update_project
(
    p_pid INT,
    p_name_old NVARCHAR2,
    p_name NVARCHAR2,
    p_description NVARCHAR2,
    p_priority NUMBER,
    p_started_at TIMESTAMP,
    p_ended_at TIMESTAMP    
)
IS
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

END;
/
GRANT EXECUTE ON update_project TO PANDA_USER_ROLE;

CREATE OR REPLACE PROCEDURE Delete_Project
(
    p_pid INT
)
IS
BEGIN
    DELETE FROM PROJECT WHERE id = p_pid;
    COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE;
            ROLLBACK;
END;
/
GRANT EXECUTE ON DELETE_PROJECT TO PANDA_USER_ROLE;


SELECT * FROM TASK;
