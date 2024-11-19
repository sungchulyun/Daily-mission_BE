INSERT INTO `user` (`user_id`, `name`, `username`, `nickname`, `image_url`, `email`)
VALUES
    (1, '윤성철', 'naver ldoqdcDgsM0fZ-Q9zI44qCCg2lF4Ugno3k70nGIYmfA', '윤성철', 'https://phinf.pstatic.net/contact/20240627_12/1719462406630ECMIs_PNG/avatar_profile.png', 'proattacker@naver.com'),
    (2, '윤성철', 'google 106088487779653945150', 'sungchul', 'https://lh3.googleusercontent.com/a/ACg8ocLuIomy21grZAe-_HDhHm7HDPbL6R9_5a1JY_i3o4-KutpPdw=s96-c', 'proattacker641@gmail.com');

INSERT INTO `mission_rule` (`mission_rule_id`, `SUNDAY`, `MONDAY`, `TUESDAY`, `WEDNESDAY`, `THURSDAY`, `FRIDAY`, `SATURDAY`, `DELETED`)
VALUES
    (1, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE),
    (2, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE);

INSERT INTO `mission` (`mission_id`, `mission_rule_id`, `user_id`, `title`, `content`, `image_url`, `hint`, `credential`, `start_date`, `end_date`, `ended`, `deleted`)
VALUES
    (1, 1, 1, 'TITLE', 'CONTENT', 'THUMBNAIL.jpg', 'HINT', 'CREDENTIAL', LOCALTIME, '2024-12-30', FALSE, FALSE),
    (2, 2, 2, 'TITLE', 'CONTENT', 'THUMBNAIL.jpg', 'HINT', 'CREDENTIAL', LOCALTIME, '2024-12-30', FALSE, FALSE);

INSERT INTO `participant` (`participant_id`, `mission_id`, `user_id`, `banned`)
VALUES
    (1, 1, 1, FALSE);

INSERT INTO `post` (`post_id`, `mission_id`, `user_id`, `title`, `content`, `image_url`)
VALUES
    (1, 1, 1, 'TITLE', 'CONTENT', 'IMAGE_URL'),
    (2, 1, 1, 'TITLE', 'CONTENT', 'IMAGE_URL');