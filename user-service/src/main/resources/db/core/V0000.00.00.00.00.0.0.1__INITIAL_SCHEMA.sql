do $body$
declare
	root_user_id uuid;
	role_admin_id uuid;
	role_applicant_id uuid;
	role_validator_id uuid;
begin
	create extension if not exists pg_trgm;
	create extension if not exists pgcrypto;

	create table users (
		id uuid not null default gen_random_uuid (),
		email varchar(100) not null,
		first_name varchar(100) not null,
		last_name varchar(100) not null,
		password varchar(96) not null,
		phone varchar(15) not null,
		user_role varchar(14) not null,
		disabled bool not null,
		active bool not null,
		timezone varchar(32) not null default 'Etc/UTC' ::character varying,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint user_login_unique unique (email),
        CONSTRAINT user_role_check CHECK (((user_role)::text = ANY ('{ADMIN,PROFESSOR,STUDENT}'::text[]))),
		constraint users_pkey primary key (id)
	);

	root_user_id := gen_random_uuid ();
	insert into users (id, email, first_name, last_name, creation, password,phone ,user_role, active, disabled,  timezone)
		values (root_user_id, 'admin@test.com', 'admin', 'admin', now(), '$2a$10$P0zFzsVuduPyn0K9KubDDO6lOWN1Eu/WCI5Msm6.ypqdUVdnRLS..','0774688660','ADMIN', true, false, 'Europe/Bucharest');

END
$body$ language plpgsql;
