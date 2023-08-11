do $body$
begin
	create extension if not exists pg_trgm;
	create extension if not exists pgcrypto;

    drop table if exists addresses;
    create table if not exists addresses(
       id uuid not null default gen_random_uuid(),
       county_id uuid not null,
       city varchar(100) not null,
       street varchar(200) not null,
       number varchar(10) not null,
       block varchar(50) null,
       staircase varchar(5) null,
       apartment int4 null,
       creation timestamptz not null,
       expires timestamptz null,
       modified timestamptz null,
       created_by uuid not null,
       modified_by uuid null,
       constraint addresses_pkey primary key (id),
       constraint county_fk foreign key (county_id) references locations (id)
    );

    drop table if exists images;
	create table images (
		id uuid not null default gen_random_uuid (),
        file_name varchar(80) not null,
        file_type varchar(10) not null,
        file_size int4 not null,
        file_data bytea not null,
        resolution varchar(30) null,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint images_pkey primary key (id)
	);

    drop table if exists professor_profile;
    create table professor_profile (
         id uuid not null default gen_random_uuid (),
         user_id uuid not null,
         picture uuid null,
         university varchar(80) not null,
         full_name varchar(80) not null,
         address_id uuid not null,
         creation timestamptz not null,
         modified timestamptz null,
         expires timestamptz null,
         constraint professor_pkey primary key (id),
         constraint address_fk1 foreign key (address_id) references addresses (id)
    );

    drop table if exists student_profile;
	create table student_profile (
		id uuid not null default gen_random_uuid (),
        user_id uuid not null,
        picture uuid null,
        desired_exam_date timestamptz not null,
        desired_university varchar(80) not null,
        school varchar(80) not null,
        address_id uuid not null,
        professor_id uuid null,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint student_pkey primary key (id),
	    constraint address_fk2 foreign key (address_id) references addresses (id)
	);

END
$body$ language plpgsql;
