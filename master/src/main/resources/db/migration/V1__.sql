CREATE TABLE daemon_server
(
    id           UUID                        NOT NULL,
    endpoint     VARCHAR(255)                NOT NULL,
    access_token VARCHAR(255)                NOT NULL,
    enabled      BOOLEAN                     NOT NULL,
    mirror       BOOLEAN                     NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_daemon_server PRIMARY KEY (id)
);

CREATE TABLE object
(
    id         UUID                        NOT NULL,
    name       VARCHAR(255)                NOT NULL,
    size       BIGINT                      NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_object PRIMARY KEY (id)
);

CREATE TABLE object_daemon_servers
(
    daemon_server_id UUID NOT NULL,
    object_id        UUID NOT NULL,
    CONSTRAINT pk_object_daemon_servers PRIMARY KEY (daemon_server_id, object_id)
);

ALTER TABLE daemon_server
    ADD CONSTRAINT uc_daemon_server_endpoint UNIQUE (endpoint);

ALTER TABLE object
    ADD CONSTRAINT uc_object_name UNIQUE (name);

ALTER TABLE object_daemon_servers
    ADD CONSTRAINT fk_objdaeser_on_daemon_server FOREIGN KEY (daemon_server_id) REFERENCES daemon_server (id);

ALTER TABLE object_daemon_servers
    ADD CONSTRAINT fk_objdaeser_on_object FOREIGN KEY (object_id) REFERENCES object (id);

CREATE UNIQUE INDEX idx_daemon_server_object_id
    ON object_daemon_servers (object_id, daemon_server_id);
