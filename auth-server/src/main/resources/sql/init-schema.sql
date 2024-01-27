/*
 * Copyright © 2024 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
 * This file is part of AAccardo Personal WebSite.
 *
 * AAccardo Personal WebSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AAccardo Personal WebSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AAccardo Personal WebSite.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 * Copyright © 2024 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
 * This file is part of AAccardo Personal WebSite.
 *
 * AAccardo Personal WebSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AAccardo Personal WebSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AAccardo Personal WebSite.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 * Copyright © 2024 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
 * This file is part of AAccardo Personal WebSite.
 *
 * AAccardo Personal WebSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AAccardo Personal WebSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AAccardo Personal WebSite.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 * Copyright © 2024 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
 * This file is part of AAccardo Personal WebSite.
 *
 * AAccardo Personal WebSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AAccardo Personal WebSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AAccardo Personal WebSite.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

CREATE TABLE module_profile
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    code             VARCHAR(32)                             NOT NULL,
    description      VARCHAR(150),
    last_modified    TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    last_modified_by VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_module_profile PRIMARY KEY (id)
);

CREATE TABLE module_profile_role
(
    id_module_profile BIGINT       NOT NULL,
    id_role           VARCHAR(255) NOT NULL,
    CONSTRAINT pk_module_profile_role PRIMARY KEY (id_module_profile, id_role)
);

CREATE TABLE oauth2_client_details
(
    id                            VARCHAR(36) NOT NULL,
    client_id                     VARCHAR(255),
    client_secret                 VARCHAR(255),
    scopes                        VARCHAR(255),
    authorization_grant_types     VARCHAR(255),
    redirect_uris                 VARCHAR(255),
    authorization_code_validity   BIGINT,
    access_token_validity         BIGINT,
    refresh_token_validity        BIGINT,
    require_consent               BOOLEAN,
    client_id_issued_at           TIMESTAMP WITHOUT TIME ZONE,
    client_secret_expires_at      TIMESTAMP WITHOUT TIME ZONE,
    client_name                   VARCHAR(255),
    client_authentication_methods VARCHAR(255),
    post_logout_redirect_uris     VARCHAR(255),
    CONSTRAINT pk_oauth2_client_details PRIMARY KEY (id)
);

CREATE TABLE oauth2_client_role
(
    last_modified    TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by VARCHAR(255),
    client_id        VARCHAR(255) NOT NULL,
    role_code        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_oauth2_client_service_role PRIMARY KEY (client_id, role_code)
);

CREATE TABLE role
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    code             VARCHAR(50),
    description      VARCHAR(150),
    last_modified    TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    last_modified_by VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name                VARCHAR(255),
    surname             VARCHAR(255),
    email_address       VARCHAR(255),
    telephone_number    VARCHAR(255),
    username            VARCHAR(255),
    password            VARCHAR(255),
    reset_password      INTEGER,
    temporary_token     VARCHAR(255),
    token_creation_date TIMESTAMP WITHOUT TIME ZONE,
    last_modified       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by    VARCHAR(255),
    enabled             BOOLEAN,
    locked              BOOLEAN,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_module_profile
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    id_user           BIGINT                                  NOT NULL,
    id_module_profile BIGINT                                  NOT NULL,
    last_modified     TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    last_modified_by  VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_user_module_profile PRIMARY KEY (id)
);

CREATE TABLE user_session_log
(
    uid_user_session_log     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username                 VARCHAR(255)                            NOT NULL,
    failed_attempts_num      BIGINT,
    last_failed_attempt_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user_session_log PRIMARY KEY (uid_user_session_log)
);

ALTER TABLE module_profile_role
    ADD CONSTRAINT FK_MODULE_PROFILE_ROLE_ON_ID_MODULE_PROFILE FOREIGN KEY (id_module_profile) REFERENCES module_profile (id);

ALTER TABLE user_module_profile
    ADD CONSTRAINT FK_USER_MODULE_PROFILE_ON_ID_MODULE_PROFILE FOREIGN KEY (id_module_profile) REFERENCES module_profile (id);

ALTER TABLE user_module_profile
    ADD CONSTRAINT FK_USER_MODULE_PROFILE_ON_ID_USER FOREIGN KEY (id_user) REFERENCES "user" (id);

CREATE TABLE module_profile
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    code             VARCHAR(32)                             NOT NULL,
    description      VARCHAR(150),
    last_modified    TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    last_modified_by VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_module_profile PRIMARY KEY (id)
);

CREATE TABLE module_profile_role
(
    id_module_profile BIGINT       NOT NULL,
    id_role           VARCHAR(255) NOT NULL,
    CONSTRAINT pk_module_profile_role PRIMARY KEY (id_module_profile, id_role)
);

CREATE TABLE oauth2_client_details
(
    id                            VARCHAR(36) NOT NULL,
    client_id                     VARCHAR(255),
    client_secret                 VARCHAR(255),
    scopes                        VARCHAR(255),
    authorization_grant_types     VARCHAR(255),
    redirect_uris                 VARCHAR(255),
    authorization_code_validity   BIGINT,
    access_token_validity         BIGINT,
    refresh_token_validity        BIGINT,
    require_consent               BOOLEAN,
    client_id_issued_at           TIMESTAMP WITHOUT TIME ZONE,
    client_secret_expires_at      TIMESTAMP WITHOUT TIME ZONE,
    client_name                   VARCHAR(255),
    client_authentication_methods VARCHAR(255),
    post_logout_redirect_uris     VARCHAR(255),
    CONSTRAINT pk_oauth2_client_details PRIMARY KEY (id)
);

CREATE TABLE oauth2_client_role
(
    last_modified    TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by VARCHAR(255),
    client_id        VARCHAR(255) NOT NULL,
    role_code        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_oauth2_client_role PRIMARY KEY (client_id, role_code)
);

CREATE TABLE role
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    code             VARCHAR(50),
    description      VARCHAR(150),
    last_modified    TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    last_modified_by VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name                VARCHAR(255),
    surname             VARCHAR(255),
    email_address       VARCHAR(255),
    telephone_number    VARCHAR(255),
    username            VARCHAR(255),
    password            VARCHAR(255),
    reset_password      INTEGER,
    temporary_token     VARCHAR(255),
    token_creation_date TIMESTAMP WITHOUT TIME ZONE,
    last_modified       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by    VARCHAR(255),
    enabled             BOOLEAN,
    locked              BOOLEAN,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_client
(
    client_id        VARCHAR(255) NOT NULL,
    username         VARCHAR(255) NOT NULL,
    last_modified    TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by VARCHAR(255),
    CONSTRAINT pk_user_client PRIMARY KEY (client_id, username)
);

CREATE TABLE user_module_profile
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    id_user           BIGINT                                  NOT NULL,
    id_module_profile BIGINT                                  NOT NULL,
    last_modified     TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    last_modified_by  VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_user_module_profile PRIMARY KEY (id)
);

CREATE TABLE user_session_log
(
    uid_user_session_log     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username                 VARCHAR(255)                            NOT NULL,
    failed_attempts_num      BIGINT,
    last_failed_attempt_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user_session_log PRIMARY KEY (uid_user_session_log)
);

ALTER TABLE module_profile_role
    ADD CONSTRAINT FK_MODULE_PROFILE_ROLE_ON_ID_MODULE_PROFILE FOREIGN KEY (id_module_profile) REFERENCES module_profile (id);

ALTER TABLE user_module_profile
    ADD CONSTRAINT FK_USER_MODULE_PROFILE_ON_ID_MODULE_PROFILE FOREIGN KEY (id_module_profile) REFERENCES module_profile (id);

ALTER TABLE user_module_profile
    ADD CONSTRAINT FK_USER_MODULE_PROFILE_ON_ID_USER FOREIGN KEY (id_user) REFERENCES "user" (id);
