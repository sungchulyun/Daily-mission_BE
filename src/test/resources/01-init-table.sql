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

CREATE TABLE `mission_rule` (
                             mission_rule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             SUNDAY BOOLEAN NOT NULL DEFAULT FALSE,
                             MONDAY BOOLEAN NOT NULL DEFAULT FALSE,
                             TUESDAY BOOLEAN NOT NULL DEFAULT FALSE,
                             WEDNESDAY BOOLEAN NOT NULL DEFAULT FALSE,
                             THURSDAY BOOLEAN NOT NULL DEFAULT FALSE,
                             FRIDAY BOOLEAN NOT NULL DEFAULT FALSE,
                             SATURDAY BOOLEAN NOT NULL DEFAULT FALSE,
                             DELETED BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE `mission` (
                           mission_id       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '미션 PK',
                           user_id          BIGINT,
                           title            VARCHAR(255) NULL COMMENT '미션 제목',
                           content          VARCHAR(255) NULL COMMENT '미션 내용',
                           image_url        VARCHAR(255) NULL COMMENT '미션 이미지',
                           credential       VARCHAR(255) NULL COMMENT '미션 참여코드',
                           hint             VARCHAR(255) NULL COMMENT '미션 참여힌트',
                           start_date       DATE NULL COMMENT '미션 시작일자',
                           end_date         DATE NULL COMMENT '미션 종료일자',
                           ended            BOOLEAN DEFAULT FALSE COMMENT '종료여부',
                           deleted          BOOLEAN DEFAULT FALSE COMMENT '삭제여부',
                           created_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
                           last_modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
                           mission_rule_id  BIGINT COMMENT '미션 규칙',

                           CONSTRAINT fk_mission_rule_id FOREIGN KEY (mission_rule_id) REFERENCES mission_rule(mission_rule_id) ON DELETE CASCADE,
                           CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);

CREATE TABLE `participant` (
                               participant_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '참여자 PK',
                               mission_id BIGINT COMMENT '참조하는 미션 ID',
                               user_id BIGINT COMMENT '참조하는 유저 ID',
                               banned BOOLEAN DEFAULT FALSE COMMENT '차단 여부',
                               created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
                               modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
                               created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               last_modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_participant_mission_id FOREIGN KEY (mission_id) REFERENCES mission(mission_id) ON DELETE CASCADE,
                               CONSTRAINT fk_participant_user_id FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);