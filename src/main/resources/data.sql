--
-- DELETE FROM user_roles;
-- -- DELETE FROM devices;
-- -- DELETE FROM notifications;
-- DELETE FROM roles;
-- DELETE FROM app_users;
-- DELETE FROM message_attachments;
-- DELETE FROM messages;
-- DELETE FROM chat_users;
-- DELETE FROM chats;
INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('USER');

INSERT INTO app_users (id, username, email, password, full_name, avatar_url, bio, created_at, is_active, is_verified)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'admin', 'admin@email.com',
     '$2a$12$.NALdJeDlmXUugMWI2AniO5CbhgsPWm9gDkWxZPA4nj/t118ieHRS',
     N'La Hung Admin', NULL, N'Chào mọi người!', GETDATE(), 1, 1),
    ('22222222-2222-2222-2222-222222222222', 'user', 'user@email.com',
     '$2a$12$.NALdJeDlmXUugMWI2AniO5CbhgsPWm9gDkWxZPA4nj/t118ieHRS',
     N'La Hung User', NULL, N'Yêu du lịch', GETDATE(), 1, 1);

INSERT INTO user_roles (id, user_id, role_id) VALUES
                                                  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 1), -- admin có role ADMIN
                                                  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 2), -- admin cũng có role USER
                                                  ('cccccccc-cccc-cccc-cccc-cccccccccccc', '22222222-2222-2222-2222-222222222222', 2); -- user có role USER

-- CHATS
INSERT INTO chats (id, chat_name, is_group, created_by, created_at)
VALUES
    ('33333333-3333-3333-3333-333333333333', NULL, 1, '11111111-1111-1111-1111-111111111111', GETDATE());

-- CHAT_USERS
INSERT INTO chat_users (id, chat_id, user_id, is_admin)
VALUES
    ('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 1),
    ('88888888-8888-8888-8888-888888888888', '33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', 0);



-- -- postgre
-- DELETE FROM app_user;
-- DELETE FROM chats;
-- DELETE FROM chat_users;
-- DELETE FROM messages;
-- DELETE FROM message_attachments;
-- DELETE FROM devices;
-- DELETE FROM notifications;
--
-- -- USERS
-- INSERT INTO app_user (id, username, email, password, full_name, avatar_url, bio, created_at, is_active, is_verified, role)
-- VALUES
--     ('11111111-1111-1111-1111-111111111111', 'admin', 'admin@email.com',
--      '$2a$12$.NALdJeDlmXUugMWI2AniO5CbhgsPWm9gDkWxZPA4nj/t118ieHRS',
--      'La Hung Admin', NULL, 'Chào mọi người!', NOW(), TRUE, TRUE, 'ADMIN'),
--     ('22222222-2222-2222-2222-222222222222', 'user', 'user@email.com',
--      '$2a$12$.NALdJeDlmXUugMWI2AniO5CbhgsPWm9gDkWxZPA4nj/t118ieHRS',
--      'La Hung User', NULL, 'Yêu du lịch', NOW(), TRUE, TRUE, 'USER');
--
-- -- CHATS
-- INSERT INTO chats (id, chat_name, is_group, created_by, created_at)
-- VALUES
--     ('33333333-3333-3333-3333-333333333333', NULL, TRUE, '11111111-1111-1111-1111-111111111111', NOW());
--
-- -- CHAT_USERS
-- INSERT INTO chat_users (id, chat_id, user_id, is_admin)
-- VALUES
--     ('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', TRUE),
--     ('88888888-8888-8888-8888-888888888888', '33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', FALSE);
--

