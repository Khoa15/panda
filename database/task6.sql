ALTER SESSION SET CURRENT_SCHEMA=PANDA;

--- PHUC
-- SELECT ALL TRIGGER DA CAI DAT
CREATE OR REPLACE FUNCTION SELECT_ALL_TRIGGERS RETURN SYS_REFCURSOR
IS
    CUR_TRIG SYS_REFCURSOR;
BEGIN
    OPEN CUR_TRIG FOR
        SELECT * FROM ALL_TRIGGERS where OWNER ='PANDA'; 
    RETURN CUR_TRIG;
END SELECT_ALL_TRIGGERS;
/

-- trigger khi create cap nhat thoi gian, va update cung se cap nhat
CREATE OR REPLACE TRIGGER project_timestamp
BEFORE INSERT OR UPDATE ON project
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :NEW.created_at := SYSTIMESTAMP;
    END IF;
    :NEW.updated_at := SYSTIMESTAMP;
END;
/
-- khi xoa 1 collection se xoa cac dong du lieu lien quan den id collection nay
-- :OLD de "tham chieu doi tuong" dung trong trigger la DELETE hoac UPDATE
CREATE OR REPLACE TRIGGER delete_collection_data
AFTER DELETE ON collection
FOR EACH ROW
BEGIN
    -- xoa collection
    DELETE FROM collection_card 
    WHERE collect_id = :OLD.id;

    -- xoa vocab_typevocab - cid - collection 
    DELETE FROM vocab_typevocab WHERE cid IN (SELECT card_id FROM collection_card 
                                              WHERE collect_id = :OLD.id);

    -- xoa sentence_vocab_typevocab -cid- vocab_typevocab 
    DELETE FROM sentence_vocab_typevocab 
    WHERE word IN (SELECT word FROM vocab_typevocab 
                   WHERE cid IN (SELECT card_id FROM collection_card 
                                 WHERE collect_id = :OLD.id));

    -- xoa sentence - id - sentence_vocab_typevocab 
    DELETE FROM sentence 
    WHERE id IN (SELECT sid FROM sentence_vocab_typevocab 
                 WHERE word IN (SELECT word FROM vocab_typevocab 
                                WHERE cid IN (SELECT card_id FROM collection_card 
                                              WHERE collect_id = :OLD.id)));

    -- Tìm các card ko con nam trong bat ki collection nao r tien hanh delete het 
    FOR card_rec IN (SELECT c.id
                      FROM card c
                      LEFT JOIN collection_card cc ON c.id = cc.card_id
                      WHERE cc.card_id IS NULL) LOOP
        DELETE FROM flashcard WHERE cid = card_rec.id;
        DELETE FROM vocab_typevocab WHERE cid = card_rec.id;
        DELETE FROM card WHERE id = card_rec.id;
    END LOOP;
END;
/

