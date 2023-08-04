do $body$
    begin
        drop table if exists locations_levels;
        create table if not exists locations_levels (
                                                        id uuid not null default gen_random_uuid(),
                                                        level_name varchar(100) not null,
                                                        abbreviation varchar(4) not null,
                                                        abbreviation_long varchar(100) not null,
                                                        creation timestamptz not null,
                                                        expires timestamptz null,
                                                        modified timestamptz null,
                                                        code varchar(50) null,
                                                        constraint location_level_code_unique unique (code),
                                                        constraint location_level_pkey primary key (id)
        );

        drop table if exists locations;
        create table if not exists locations (
                                                 id uuid not null default gen_random_uuid(),
                                                 "location" varchar(100) not null,
                                                 location_level_id uuid not null,
                                                 parent_id uuid null,
                                                 creation timestamptz not null,
                                                 expires timestamptz null,
                                                 modified timestamptz null,
                                                 short_code varchar(100) null,
                                                 legacy_id varchar(100) null,
                                                 constraint location_pkey primary key (id),
                                                 constraint location_level_fk foreign key (location_level_id) references locations_levels(id),
                                                 constraint parent_fk foreign key (parent_id) references locations(id)
        );

    end
$body$ language plpgsql;

