DELETE FROM users;
DELETE FROM chats;
DELETE FROM chat_users;
DELETE FROM messages;
DELETE FROM message_attachments;

-- USERS
INSERT INTO users (id, username, email, password, full_name, avatar_url, bio, created_at, is_active, is_verified, role)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'admin', 'admin@email.com',
     '$2a$12$.NALdJeDlmXUugMWI2AniO5CbhgsPWm9gDkWxZPA4nj/t118ieHRS',
     N'La Hung Admin', NULL, N'Chào mọi người!', GETDATE(), 1, 1, 'ADMIN'),

    ('22222222-2222-2222-2222-222222222222', 'user', 'user@email.com',
     '$2a$12$.NALdJeDlmXUugMWI2AniO5CbhgsPWm9gDkWxZPA4nj/t118ieHRS',
     N'La Hung User', NULL, N'Yêu du lịch', GETDATE(), 1, 1, 'USER');

-- CHATS
INSERT INTO chats (id, chat_name, is_group, created_by, created_at)
VALUES
    ('33333333-3333-3333-3333-333333333333', NULL, 1, '11111111-1111-1111-1111-111111111111', GETDATE());

-- CHAT_USERS
INSERT INTO chat_users (id, chat_id, user_id, is_admin)
VALUES
    ('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 1),
    ('88888888-8888-8888-8888-888888888888', '33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', 0);

-- -- MESSAGES
-- INSERT INTO messages (id, chat_id, sender_id, content, created_at, is_read)
-- VALUES
--     ('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', N'Hello user!', GETDATE(), 1),
--     ('88888888-8888-8888-8888-888888888888', '33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', N'Chào admin!', GETDATE(), 1);
--
-- -- MESSAGE_ATTACHMENTS
-- INSERT INTO message_attachments (id, message_id, file_url, file_type)
-- VALUES
--     ('99999999-9999-9999-9999-999999999999', '77777777-7777-7777-7777-777777777777', 'chat_img1.jpg', 'image'),
--     ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '88888888-8888-8888-8888-888888888888', 'tailieu.pdf', 'pdf');
--
-- -- DEVICES
-- INSERT INTO devices (id, user_id, device_token, device_name, last_login, is_active)
-- VALUES
--     ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'token1', N'iPhone 13', GETDATE(), 1),
--     ('cccccccc-cccc-cccc-cccc-cccccccccccc', '22222222-2222-2222-2222-222222222222', 'token2', N'Galaxy S21', GETDATE(), 1);
--
-- -- NOTIFICATIONS
-- INSERT INTO notifications (id, user_id, type, message, link, is_read, created_at)
-- VALUES
--     ('dddddddd-dddd-dddd-dddd-dddddddddddd', '11111111-1111-1111-1111-111111111111', 'like', N'User đã thích bài viết của bạn', NULL, 0, GETDATE()),
--     ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '22222222-2222-2222-2222-222222222222', 'comment', N'Admin đã bình luận bài viết của bạn', NULL, 0, GETDATE());
--
-- -- USER_BEHAVIORS
-- INSERT INTO user_behaviors (id, user_id, action, target_id, created_at)
-- VALUES
--     ('ffffffff-ffff-ffff-ffff-ffffffffffff', '11111111-1111-1111-1111-111111111111', 'view_post', '22222222-2222-2222-2222-222222222222', GETDATE()),
--     ('11111111-aaaa-aaaa-aaaa-111111111111', '22222222-2222-2222-2222-222222222222', 'like_post', '11111111-1111-1111-1111-111111111111', GETDATE());
--
