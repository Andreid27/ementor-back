do $body$
declare
	root_user_id uuid;
begin
	create extension if not exists pg_trgm;
	create extension if not exists pgcrypto;


    drop table if exists questions;
    create table questions (
                           id uuid not null default gen_random_uuid (),
                           content varchar(500) not null,
                           answer1 varchar(300) null,
                           answer2 varchar(300) null,
                           answer3 varchar(300) null,
                           answer4 varchar(300) null,
                           answer5 varchar(300) null,
                           correct_answer smallint null,
                           source varchar(300) null,
                           source_page int4 null,
                           difficulty_level smallint null,
                           hint varchar(300) null,
                           created_by uuid null,
                           creation timestamptz null,
                           modified timestamptz null,
                           expires timestamptz null,
                           constraint questions_pkey primary key (id)
    );
END
$body$ language plpgsql;
