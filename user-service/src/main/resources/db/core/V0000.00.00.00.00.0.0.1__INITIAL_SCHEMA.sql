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
		login varchar(100) not null,
		first_name varchar(100) not null,
		last_name varchar(100) not null,
		company_name varchar(100) not null,
		password varchar(96) not null,
		disabled bool not null,
		active bool not null,
		owner bool not null default false,
		lang varchar(2) not null,
		timezone varchar(32) not null default 'Etc/UTC' ::character varying,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint password_length_check check ((char_length((password)::text) = 96)),
		constraint user_login_unique unique (login),
		constraint users_pkey primary key (id)
	);

	create table roles (
		id uuid not null default gen_random_uuid (),
		code varchar(100) not null,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		display varchar(25) not null,
		constraint roles_pkey primary key (id)
	);

	create table user_roles (
		id uuid not null default gen_random_uuid (),
		user_id uuid not null,
		role_id uuid not null,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint user_roles_pkey primary key (id),
		constraint user_role_role_fk foreign key (role_id) references roles (id),
		constraint user_role_user_fk foreign key (user_id) references users (id)
	);

	create table session_keys (
		id uuid not null default gen_random_uuid (),
		user_id uuid not null,
		creation timestamptz not null,
		modified timestamptz not null,
		expiration timestamptz null,
		constraint session_keys_pkey primary key (id),
		constraint session_key_user_fk foreign key (user_id) references users (id)
	);	

	create or replace view users_view as
		SELECT u.id,
			u.login,
			u.first_name,
			u.last_name,
			u.company_name,
			r.code AS role,
			u.disabled,
			u.creation,
			u.modified
		FROM users u
			JOIN user_roles ur ON u.id = ur.user_id
			JOIN roles r ON r.id = ur.role_id;
	
	
	role_admin_id := gen_random_uuid ();	
	role_applicant_id := gen_random_uuid ();
	role_validator_id := gen_random_uuid ();
	insert into roles (id, code, display, creation)
		values (role_admin_id, 'ROLE_ADMIN', 'admin', now());
	insert into roles (id, code, display, creation)
		values (role_applicant_id, 'ROLE_APPLICANT', 'applicant', now());
	insert into roles (id, code, display, creation)
		values (role_validator_id, 'ROLE_VALIDATOR', 'validator', now());

	root_user_id := gen_random_uuid ();
	insert into users (id, login, first_name, last_name, company_name, creation, password, lang, active, disabled, owner, timezone)
		values (root_user_id, 'admin@test.com', 'admin', 'admin', 'N/A', now(), '$argon2id$v=19$m=4096,t=3,p=1$cWPi8S3aCP1K3XeZoOkkgw$GGjLv7Q754iR7dEJb0sEpFWbvtuY17aa4oWccH+GOM0', 'ro', true, false, true, 'Europe/Bucharest');
	insert into user_roles (id, user_id, role_id, creation)
		values (gen_random_uuid (), root_user_id, role_admin_id, now());

END
$body$ language plpgsql;
