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

    drop table if exists quizzes_students;
    CREATE TABLE quizzes_students (
        id uuid not null,
        quiz_id uuid NOT NULL,
        user_id uuid NOT NULL,
        created_by uuid null,
        start_after timestamptz null,
        started_at timestamptz null,
        end_time timestamptz null,
        ended_time timestamptz null,
        correct_answers int4 null,
        creation timestamptz null,
        modified timestamptz null,
        expires timestamptz null,
       CONSTRAINT quiz_student_quiz_id_fkey2 FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
       PRIMARY KEY (id)
    );

    drop table if exists users_answers;
    CREATE TABLE users_answers (
      id uuid not null,
      user_id uuid NOT NULL,
      quizzes_students_id uuid NOT NULL,
      question_id uuid NOT NULL,
      answer smallint not null,
      correct_answer smallint not null,
      creation timestamptz null,
      modified timestamptz null,
      expires timestamptz null,
      CONSTRAINT users_answers_quiz_students_id_fkey1 FOREIGN KEY (quizzes_students_id) REFERENCES quizzes_students(id),
      PRIMARY KEY (id)
    );


END
$body$ language plpgsql;
