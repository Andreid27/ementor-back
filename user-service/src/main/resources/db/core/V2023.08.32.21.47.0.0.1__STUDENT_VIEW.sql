do $body$
    begin
        drop view if exists student_profile_view;
        create or replace view student_profile_view as
        select
            sp.user_id,
            sp.professor_id,
            sp.school,
            sp.desired_university_id,
            sp.desired_speciality_id
        from student_profiles sp
        where sp.expires is null or sp.expires > current_timestamp;
    end
$body$ language plpgsql;