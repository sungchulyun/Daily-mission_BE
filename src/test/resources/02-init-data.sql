INSERT INTO `user` ( `name`, `username`, `nickname`, `image_url`, `email`)
VALUES
    ('윤성철', 'naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA', '윤성철', 'https://phinf.pstatic.net/contact/20240627_12/1719462406630ECMIs_PNG/avatar_profile.png', 'proattacker@naver.com'),
    ('윤성철', 'google 106088487779653945150', 'sungchul', 'https://lh3.googleusercontent.com/a/ACg8ocLuIomy21grZAe-_HDhHm7HDPbL6R9_5a1JY_i3o4-KutpPdw=s96-c', 'proattacker641@gmail.com');

INSERT INTO `mission_rule` ( `SUNDAY`, `MONDAY`, `TUESDAY`, `WEDNESDAY`, `THURSDAY`, `FRIDAY`, `SATURDAY`, `DELETED`)
VALUES
    (TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE),
    (TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE);

INSERT INTO `mission` (`mission_rule_id`, `user_id`, `title`, `content`, `image_url`, `hint`, `credential`, `start_date`, `end_date`, `ended`, `deleted`)
VALUES
    (1, 1, 'TITLE', 'CONTENT', 'THUMBNAIL.jpg', 'HINT', 'CREDENTIAL', LOCALTIME, '2024-12-30', FALSE, FALSE),
    (2, 2,  'TITLE', 'CONTENT', 'THUMBNAIL.jpg', 'HINT', 'CREDENTIAL', LOCALTIME, '2024-12-30', FALSE, FALSE);

INSERT INTO `participant` (`mission_id`, `user_id`, `banned`)
VALUES
    (1, 1, FALSE);

INSERT INTO `post` (`mission_id`, `user_id`, `title`, `content`, `image_url`)
VALUES
    (1, 1, 'TITLE', 'CONTENT', 'IMAGE_URL'),
    (2, 1, 'TITLE', 'CONTENT', 'IMAGE_URL');