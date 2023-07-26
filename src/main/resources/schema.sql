-- init SQL
-- create schema habiters-db;
-- use habiters-db;

DROP TABLE IF EXISTS vote_item;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS bookmark_folder;
DROP TABLE IF EXISTS bookmark;
DROP TABLE IF EXISTS emoji;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS post;

DROP TABLE IF EXISTS habit_check;
DROP TABLE IF EXISTS habbit;
DROP TABLE IF EXISTS diary;
DROP TABLE IF EXISTS member;

create table member
(
    id              bigint auto_increment primary key,
    email           varchar(255) null,
    nick_name       varchar(255) null,
    oauth_id        varchar(255) null,
    profile_img_url varchar(255) null,
    provider        varchar(255) null,
    created_date    datetime(6)  null     default CURRENT_TIMESTAMP(6),
    updated_date    datetime(6)  null     default CURRENT_TIMESTAMP(6),
    deleted         boolean      not null default false
);

create table diary
(
    id              bigint auto_increment primary key,
    content         varchar(255) null,
    end_update_date datetime(6)  null,
    member_id       bigint       null,
    created_date    datetime(6)  null     default CURRENT_TIMESTAMP(6),
    updated_date    datetime(6)  null     default CURRENT_TIMESTAMP(6),
    deleted         bit          not null default false,
    constraint FKbyluyva0mxnf5jitf297oxlxd
        foreign key (member_id) references member (id)
);

create table habbit
(
    id           bigint auto_increment primary key,
    content      varchar(255) null,
    member_id    bigint       null,
    created_date datetime(6)  null     default CURRENT_TIMESTAMP(6),
    updated_date datetime(6)  null     default CURRENT_TIMESTAMP(6),
    deleted      bit          not null default false,
    constraint FK2tlodega5v0fas0uv1ksxu8ci
        foreign key (member_id) references member (id)
);

create table habit_check
(
    id           bigint auto_increment primary key,
    checked      bit         not null,
    habit_id     bigint      null,
    created_date datetime(6) null     default CURRENT_TIMESTAMP(6),
    updated_date datetime(6) null     default CURRENT_TIMESTAMP(6),
    deleted      bit         not null default false,
    constraint FK6exrsx5hm05v3ur6c3ttiqopd
        foreign key (habit_id) references habbit (id)
);

create table post
(
    id           bigint auto_increment primary key,
    title        varchar(255) not null,
    content      varchar(255) not null,
    category     varchar(255) not null,
    views        bigint       not null default 0,
    member_id    bigint       null,
    created_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    updated_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    deleted      boolean      not null default false,

    FULLTEXT INDEX post_title (title),
    FULLTEXT INDEX post_content (content)
);

create table comment
(
    id           bigint auto_increment primary key,
    content      varchar(255) not null,
    member_id    bigint       not null,
    post_id      bigint       not null,
    parent_id    bigint       null,
    created_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    updated_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    deleted      boolean      not null default false
);

create table emoji
(
    id           bigint auto_increment primary key,
    domain       varchar(30) not null,
    type         varchar(30) not null,
    member_id    bigint      not null,
    domain_id    bigint      not null,
    created_date datetime(6) not null default CURRENT_TIMESTAMP(6),
    updated_date datetime(6) not null default CURRENT_TIMESTAMP(6),
    deleted      boolean     not null default false,

    unique index idx_emoji_unique (domain, domain_id, member_id),
    index idx_emoji_domain (domain, domain_id),
    index idx_emoji_member_id (member_id)
);

create table bookmark
(
    id           bigint auto_increment primary key,
    title        varchar(255) not null,
    description  varchar(500) not null,
    member_id    bigint       not null,
    created_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    updated_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    deleted      boolean      not null default false
);

create table bookmark_folder
(
    bookmark_id bigint not null,
    posts_id    bigint null
);

create table vote
(
    id           bigint auto_increment primary key,
    title        varchar(255) not null,
    deadline     datetime(6)  not null,
    created_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    updated_date datetime(6)  not null default CURRENT_TIMESTAMP(6),
    deleted      boolean      not null default false
);

create table vote_item
(
    id      bigint auto_increment primary key,
    content varchar(255) not null,
    count   int          not null default 0,
    seq     int          not null default 1,
    vote_id bigint       not null,

    constraint vote_item_vote_id_fk
        foreign key (vote_id) references vote (id)
);