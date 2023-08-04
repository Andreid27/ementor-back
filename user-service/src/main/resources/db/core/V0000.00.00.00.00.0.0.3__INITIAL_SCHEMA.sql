do $body$
begin
	create extension if not exists pg_trgm;
	create extension if not exists pgcrypto;

	create table images (
		id uuid not null default gen_random_uuid (),
        file_name varchar(80) not null,
        file_type varchar(10) not null,
        size int4 not null,
        file_data bytea not null,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint images_pkey primary key (id)
	);
	create table student_profile (
		id uuid not null default gen_random_uuid (),
        user_id uuid not null,
        profile_picture uuid null,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint student_pkey primary key (id)
	);

END
$body$ language plpgsql;
