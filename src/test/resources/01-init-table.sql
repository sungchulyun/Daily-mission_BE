CREATE TABLE `user` (
                        user_id            bigint       NOT NULL AUTO_INCREMENT COMMENT '유저 PK',
                        username           varchar(255) NOT NULL COMMENT '유저 고유이름',
                        name               varchar(255) NOT NULL COMMENT '유저 이름',
                        nickname           varchar(255) NULL COMMENT '유저 닉네임',
                        email              varchar(255) NULL COMMENT '유저 이메일',
                        image_url           varchar(255) NULL COMMENT '유저 프로필 사진',
                        created_time       timestamp    NULL COMMENT '최초 생성 시간',
                        last_modified_time timestamp    NULL COMMENT '마지막 수정시간',
                        role               enum ('GUEST','USER') NOT NULL COMMENT '권한',
                        primary key (user_id)
);