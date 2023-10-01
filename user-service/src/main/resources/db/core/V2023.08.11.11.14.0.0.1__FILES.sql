do $body$
    declare
    begin
        create extension if not exists pg_trgm;
        create extension if not exists pgcrypto;

        drop table if exists files;
        create table files (
            id uuid not null default gen_random_uuid (),
            file_name varchar(80) not null,
            file_type varchar(10) not null,
            file_size int4 not null,
            file_data bytea not null,
            creation timestamptz not null,
            modified timestamptz null,
            expires timestamptz null,
            constraint file_pkey primary key (id)
        );
END
$body$ language plpgsql;