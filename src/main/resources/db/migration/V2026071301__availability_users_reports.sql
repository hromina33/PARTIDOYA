alter table users
    add column status varchar(30) not null default 'ACTIVE',
    add column suspension_reason varchar(300),
    add column suspended_until date,
    add column last_administrative_action_at datetime(6),
    add column last_administrative_action_by bigint;

create table if not exists user_administrative_actions (
    id bigint not null auto_increment,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    admin_id bigint not null,
    target_user_id bigint not null,
    action varchar(40) not null,
    reason varchar(300),
    action_created_at datetime(6) not null,
    primary key (id)
);

create table if not exists court_availability (
    id bigint not null auto_increment,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    court_id bigint not null,
    availability_date date not null,
    all_day bit not null,
    start_time time not null,
    end_time time not null,
    type varchar(20) not null,
    reason varchar(200),
    availability_created_at datetime(6) not null,
    primary key (id)
);

create index idx_court_availability_court_date
    on court_availability (court_id, availability_date);

create index idx_user_admin_actions_target
    on user_administrative_actions (target_user_id);
