do $body$
    declare

    begin
        DROP VIEW IF EXISTS quizzes_students_view;

        CREATE VIEW quizzes_students_view AS
        SELECT
            qv.*,
            qs.user_id,
            qs.id as attempt_id
        FROM
            quizzes_students qs
                LEFT JOIN
            quizzes_view qv ON qs.quiz_id = qv.quiz_id;
    end
$body$ language plpgsql;