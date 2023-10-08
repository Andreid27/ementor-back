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

    drop table if exists chapters;
    create table chapters (
       id uuid not null default gen_random_uuid (),
       title varchar(200) not null,
       description varchar(300) null,
       created_by uuid null,
       creation timestamptz null,
       modified timestamptz null,
       expires timestamptz null,
       constraint chapters_pkey primary key (id)
    );


    drop table if exists quizzes;
    create table quizzes (
       id uuid not null default gen_random_uuid (),
       title varchar(200) not null,
       description varchar(300) null,
       component_type varchar(3) null,
       difficulty_level smallint null,
       max_time int4 null,
       created_by uuid null,
       creation timestamptz null,
       modified timestamptz null,
       expires timestamptz null,
       constraint quiz_pkey primary key (id)
    );

    drop table if exists quizzes_chapters;
    CREATE TABLE quizzes_chapters (
      quiz_id uuid NOT NULL,
      chapter_id uuid NOT NULL,
      CONSTRAINT quiz_chapter_chapter_id_fkey FOREIGN KEY (chapter_id) REFERENCES chapters(id),
      CONSTRAINT quiz_chapter_quiz_id_fkey FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
      PRIMARY KEY (quiz_id, chapter_id)
    );

    drop table if exists quizzes_questions;
    CREATE TABLE quizzes_questions (
      quiz_id uuid NOT NULL,
      question_id uuid NOT NULL,
      CONSTRAINT quiz_chapter_question_id_fkey FOREIGN KEY (question_id) REFERENCES questions(id),
      CONSTRAINT quiz_chapter_quiz_id_fkey2 FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
      PRIMARY KEY (quiz_id, question_id)
    );

END
$body$ language plpgsql;
