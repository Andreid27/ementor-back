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

    drop table if exists universities;
    create table universities (
        id uuid not null default gen_random_uuid (),
        name varchar(80) not null,
        address_id uuid not null,
        phone varchar(15) null,
        exam_book varchar(50) null,
        creation timestamptz not null,
        modified timestamptz null,
        expires timestamptz null,
        constraint university_pkey primary key (id),
        constraint address_fk3 foreign key (address_id) references addresses (id)
    );


    drop table if exists specialities;
    create table specialities (
      id uuid not null default gen_random_uuid (),
      name varchar(80) not null,
      study_years int2 not null,
      about varchar(1000) null,
      creation timestamptz not null,
      modified timestamptz null,
      expires timestamptz null,
      constraint speciality_pkey primary key (id)
    );

    drop table if exists universities_specialities;
    create table universities_specialities (
      id uuid not null default gen_random_uuid (),
      university_id uuid not null,
      speciality_id uuid not null,
      difficulty int2 not null,
      about varchar(2000) null,
      creation timestamptz not null,
      modified timestamptz null,
      expires timestamptz null,
      constraint university_speciality_pkey primary key (id),
      constraint university_fk foreign key (university_id) references universities (id),
      constraint speciality_fk foreign key (speciality_id) references specialities (id)
    );

    drop table if exists professor_profile;
    create table professor_profile (
         id uuid not null default gen_random_uuid (),
         user_id uuid not null,
         picture uuid null,
         university_speciality_id uuid not null,
         full_name varchar(80) null,
         about varchar(3000) null,
         address_id uuid not null,
         creation timestamptz not null,
         modified timestamptz null,
         expires timestamptz null,
         constraint professor_pkey primary key (id),
         constraint address_fk1 foreign key (address_id) references addresses (id),
         constraint university_speciality_fk foreign key (university_speciality_id) references universities_specialities (id)
    );

    drop table if exists student_profiles;
	create table student_profiles (
		id uuid not null default gen_random_uuid (),
        user_id uuid not null,
        picture uuid null,
        desired_exam_date timestamptz not null,
        desired_university_speciality_id uuid not null,
        school varchar(80) not null,
        address_id uuid not null,
        professor_id uuid null,
		creation timestamptz not null,
		modified timestamptz null,
		expires timestamptz null,
		constraint student_pkey primary key (id),
	    constraint address_fk2 foreign key (address_id) references addresses (id),
	    constraint university_speciality_fk foreign key (desired_university_speciality_id) references universities_specialities (id)
	);

END
$body$ language plpgsql;
