create table users
(
    id          serial,
    first_name  text,
    last_name   text,
    phone       text,
    password    text
);

create table sessions
(
    id          serial,
    user_id     text,
    date        text,
    time        text,
    ip          text
);

create table images
(
    id          serial,
    user_id     text,
    filename    text,
    size        text,
    mime        text
);