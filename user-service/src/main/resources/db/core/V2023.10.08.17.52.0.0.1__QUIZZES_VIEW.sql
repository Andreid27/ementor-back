do $body$
    declare

    begin
        drop view if exists quizzes_view;
        CREATE VIEW quizzes_view AS
        SELECT
            q.id AS quiz_id,
            q.title AS quiz_title,
            q.description AS quiz_description,
            q.component_type as component_type,
            STRING_AGG(DISTINCT c.title, ', ') AS chapter_titles,
            COUNT(DISTINCT qq.question_id) AS question_count,
            MAX(q.max_time) AS max_time,
            MAX(q.difficulty_level) AS difficulty_level,
            q.created_by as created_by

        FROM
            quizzes q
                LEFT JOIN
            quizzes_chapters qc ON q.id = qc.quiz_id
                LEFT JOIN
            chapters c ON qc.chapter_id = c.id
                LEFT JOIN
            quizzes_questions qq ON q.id = qq.quiz_id
        WHERE
            q.expires IS NULL OR q.expires > NOW()
        GROUP BY
            q.id, q.title, q.description;
    end
$body$ language plpgsql;