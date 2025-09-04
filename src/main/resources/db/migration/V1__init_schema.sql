-- V1__init_schema.sql

CREATE TABLE roles (
                       id INT IDENTITY(1,1) PRIMARY KEY,
                       name NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE app_users (
                           id UNIQUEIDENTIFIER PRIMARY KEY,
                           username VARCHAR(50) NOT NULL UNIQUE,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           full_name NVARCHAR(100) NOT NULL,
                           avatar_url VARCHAR(255),
                           bio NVARCHAR(255),
                           created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                           last_login DATETIME2,
                           is_active BIT NOT NULL,
                           is_verified BIT NOT NULL
);

CREATE TABLE user_roles (
                            id UNIQUEIDENTIFIER PRIMARY KEY,
                            user_id UNIQUEIDENTIFIER NOT NULL,
                            role_id INT NOT NULL,
                            CONSTRAINT fk_userroles_user FOREIGN KEY (user_id) REFERENCES app_users(id),
                            CONSTRAINT fk_userroles_role FOREIGN KEY (role_id) REFERENCES roles(id),
                            CONSTRAINT uq_userroles UNIQUE (user_id, role_id)
);

CREATE TABLE refresh_tokens (
                                id UNIQUEIDENTIFIER PRIMARY KEY,
                                user_id UNIQUEIDENTIFIER NOT NULL,
                                token VARCHAR(255) NOT NULL UNIQUE,
                                created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                                expires_at DATETIME2 NOT NULL,
                                revoked BIT NOT NULL DEFAULT 0,
                                CONSTRAINT fk_refreshtokens_user FOREIGN KEY (user_id) REFERENCES app_users(id)
);

CREATE TABLE notifications (
                               id UNIQUEIDENTIFIER PRIMARY KEY,
                               user_id UNIQUEIDENTIFIER NOT NULL,
                               type NVARCHAR(50) NOT NULL,
                               message NVARCHAR(255) NOT NULL,
                               link VARCHAR(255),
                               is_read BIT NOT NULL DEFAULT 0,
                               created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                               CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES app_users(id)
);

CREATE TABLE devices (
                         id UNIQUEIDENTIFIER PRIMARY KEY,
                         user_id UNIQUEIDENTIFIER NOT NULL,
                         device_id VARCHAR(255) NOT NULL,
                         device_name NVARCHAR(100) NOT NULL,
                         last_login DATETIME2 NOT NULL,
                         is_active BIT NOT NULL,
                         push_token VARCHAR(255),
                         CONSTRAINT fk_devices_user FOREIGN KEY (user_id) REFERENCES app_users(id)
);

CREATE TABLE chats (
                       id UNIQUEIDENTIFIER PRIMARY KEY,
                       chat_name NVARCHAR(100),
                       is_group BIT NOT NULL,
                       created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                       created_by UNIQUEIDENTIFIER,
                       CONSTRAINT fk_chats_user FOREIGN KEY (created_by) REFERENCES app_users(id)
);

CREATE TABLE chat_users (
                            id UNIQUEIDENTIFIER PRIMARY KEY,
                            chat_id UNIQUEIDENTIFIER NOT NULL,
                            user_id UNIQUEIDENTIFIER NOT NULL,
                            is_admin BIT NOT NULL,
                            CONSTRAINT fk_chatusers_chat FOREIGN KEY (chat_id) REFERENCES chats(id),
                            CONSTRAINT fk_chatusers_user FOREIGN KEY (user_id) REFERENCES app_users(id),
                            CONSTRAINT uq_chatusers UNIQUE (chat_id, user_id)
);

CREATE TABLE messages (
                          id UNIQUEIDENTIFIER PRIMARY KEY,
                          chat_id UNIQUEIDENTIFIER NOT NULL,
                          sender_id UNIQUEIDENTIFIER NOT NULL,
                          content NVARCHAR(500),
                          created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                          is_read BIT NOT NULL DEFAULT 0,
                          CONSTRAINT fk_messages_chat FOREIGN KEY (chat_id) REFERENCES chats(id),
                          CONSTRAINT fk_messages_user FOREIGN KEY (sender_id) REFERENCES app_users(id)
);

CREATE TABLE message_attachments (
                                     id UNIQUEIDENTIFIER PRIMARY KEY,
                                     message_id UNIQUEIDENTIFIER NOT NULL,
                                     file_url VARCHAR(255) NOT NULL,
                                     file_type NVARCHAR(50),
                                     CONSTRAINT fk_attachments_message FOREIGN KEY (message_id) REFERENCES messages(id)
);
