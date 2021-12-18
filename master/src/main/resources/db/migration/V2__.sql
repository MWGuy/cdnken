ALTER TABLE daemon_server
    ADD template VARCHAR(255);

ALTER TABLE daemon_server
    ALTER COLUMN template SET NOT NULL;
