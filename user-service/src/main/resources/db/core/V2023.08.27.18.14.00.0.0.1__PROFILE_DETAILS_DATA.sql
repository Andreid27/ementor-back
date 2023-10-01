drop table if exists profile_picture;
create table profile_picture (
                                 id uuid not null default gen_random_uuid (),
                                 file_name varchar(80) not null,
                                 file_type varchar(100) not null,
                                 file_size int4 not null,
                                 file_data oid not null,
                                 created_by uuid unique not null,
                                 resolution varchar(30) null,
                                 creation timestamptz not null,
                                 modified timestamptz null,
                                 expires timestamptz null,
                                 constraint images_pkey primary key (id)
);

COMMENT ON COLUMN profile_picture.file_size IS 'File size in bytes. Kb divide by 1000 and so on.';