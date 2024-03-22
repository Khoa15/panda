
------- HOA
CREATE OR REPLACE FUNCTION find_projects_between_dates(start_date IN TIMESTAMP, end_date IN TIMESTAMP)
RETURN SYS_REFCURSOR
IS
    projects_cursor SYS_REFCURSOR;
BEGIN
    OPEN projects_cursor FOR
        SELECT *
        FROM project
        WHERE created_at BETWEEN start_date AND end_date;
    
    RETURN projects_cursor;
END;
/

CREATE OR REPLACE TRIGGER project_before_insert_trigger
BEFORE INSERT ON project
FOR EACH ROW
DECLARE
    daily_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO daily_count
    FROM project
    WHERE username = :NEW.username
    AND TRUNC(created_at) = TRUNC(SYSDATE);

    IF daily_count >= 3 THEN
        DBMS_OUTPUT.PUT_LINE('B?n ?? th?m ?? s? l??ng d? ?n cho ph?p trong ng?y. H?y th?n tr?ng khi th?m d? ?n ti?p theo.');

        RAISE_APPLICATION_ERROR(-20001, '?? th?m ?? s? l??ng d? ?n cho ph?p trong ng?y.');
    END IF;
END;
/

CREATE OR REPLACE FUNCTION get_tasks_today_in_project(project_id IN NUMBER) RETURN SYS_REFCURSOR
IS
    tasks_cursor SYS_REFCURSOR;
BEGIN
    OPEN tasks_cursor FOR
        SELECT *
        FROM task
        WHERE pid = project_id
        AND TRUNC(started_at) <= TRUNC(SYSDATE)
        AND TRUNC(ended_at) >= TRUNC(SYSDATE);

    RETURN tasks_cursor;
END;
/


