CREATE DATABASE boilerplate;
USE boilerplate;

CREATE TABLE app_users (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name NVARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(255),
    bio NVARCHAR(255),
    is_active BIT NOT NULL,
    is_verified BIT NOT NULL,
    created_at DATETIME2 NOT NULL,
    last_login DATETIME2
);


CREATE TABLE roles (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE user_roles (
    role_id INT NOT NULL REFERENCES roles(id),
    user_id UNIQUEIDENTIFIER NOT NULL REFERENCES app_users(id),
    PRIMARY KEY (role_id, user_id)
);

CREATE TABLE devices (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    user_id UNIQUEIDENTIFIER NOT NULL REFERENCES app_users(id),
    device_name NVARCHAR(100) NOT NULL,
    device_id VARCHAR(255) NOT NULL,
    push_token VARCHAR(255),
    is_active BIT NOT NULL,
    last_login DATETIME2 NOT NULL
);

CREATE TABLE notifications (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    user_id UNIQUEIDENTIFIER NOT NULL REFERENCES app_users(id),
    [type] NVARCHAR(50) NOT NULL,
    link VARCHAR(255),
    message NVARCHAR(255) NOT NULL,
    is_read BIT NOT NULL,
    created_at DATETIME2 NOT NULL
);

CREATE TABLE chats (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    chat_name NVARCHAR(100),
    is_group BIT NOT NULL,
    created_at DATETIME2 NOT NULL,
    created_by UNIQUEIDENTIFIER REFERENCES app_users(id)
);


CREATE TABLE messages (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    chat_id UNIQUEIDENTIFIER NOT NULL REFERENCES chats(id),
    sender_id UNIQUEIDENTIFIER NOT NULL REFERENCES app_users(id),
    content NVARCHAR(500),
    is_read BIT NOT NULL,
    created_at DATETIME2 NOT NULL
);


CREATE TABLE chat_users (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    chat_id UNIQUEIDENTIFIER NOT NULL REFERENCES chats(id),
    user_id UNIQUEIDENTIFIER NOT NULL REFERENCES app_users(id),
    is_admin BIT NOT NULL,
    UNIQUE (chat_id, user_id)
);


CREATE TABLE message_attachments (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    message_id UNIQUEIDENTIFIER NOT NULL REFERENCES messages(id),
    file_type NVARCHAR(50),
    file_url VARCHAR(255) NOT NULL
);
