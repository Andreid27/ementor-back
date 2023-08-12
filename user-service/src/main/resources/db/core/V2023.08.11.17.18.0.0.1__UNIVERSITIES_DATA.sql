do $body$
    declare
        admin_id uuid;
        speciality1 uuid;
        speciality2 uuid;
        speciality3 uuid;
        address_id1 uuid;
        bucuresti_county_id uuid;
        address_id2 uuid;
        iasi_county_id uuid;
        university1 uuid;
        university2 uuid;

    begin

        speciality1 := gen_random_uuid();
        speciality2 := gen_random_uuid();
        speciality3 := gen_random_uuid();

        address_id1 := gen_random_uuid ();
        address_id2 := gen_random_uuid ();
        university1 := gen_random_uuid();
        university2 := gen_random_uuid();

        bucuresti_county_id :=(select l.id from locations l where l.location = 'BUCURESTI');
        iasi_county_id :=(select l.id from locations l where l.location = 'IASI');

        insert into specialities (id,"name",study_years,about,creation)values
             (speciality1,'Medicina Generala',6,'La medicina generala exista multiple ramuri de medicina pe care te poti specializa ulterior in rezidentiat.', now()),
             (speciality2,'Medicina Dentara',6,'La medicina dentara poti profesa atat cu un rezidentiat cat si fara acesta.', now()),
             (speciality3,'Farmiacie',4,'La famarcie studiezi efectul medicamentelor farmaceutice asupra corupului uman.',now());

        insert into addresses (id,street,number,city,county_id,creation,created_by)
        values (address_id1,'Bulevardul Eroii Sanitari','8','Sector 5',bucuresti_county_id,current_timestamp,gen_random_uuid()),
               (address_id2,'Strada Universității','16','Iași',iasi_county_id,current_timestamp,gen_random_uuid());

        insert into universities (id,"name",address_id,phone,exam_book,creation) values
            (university1,'Facultatea de Medicină - Universitatea de Medicină și Farmacie ''Carol Davila''',address_id1,'+40 21 318 0719','CORINT',now()),
            (university2,'Universitatea de Medicină și Farmacie ''Grigore T. Popa'' din Iași',address_id2,'+40 232 301 600','BARRONS',now());


        INSERT INTO universities_specialities (id,university_id,speciality_id,difficulty,about,creation) VALUES
        (gen_random_uuid(),university1,speciality1,5,'It is very hard here',now()),
        (gen_random_uuid(),university1,speciality2,4,'It is hard here',now()),
        (gen_random_uuid(),university1,speciality3,3,'It is ok here',now()),
        (gen_random_uuid(),university2,speciality1,4,'It is hard here',now()),
        (gen_random_uuid(),university2,speciality2,3,'It is ok here',now());

    end
$body$ language plpgsql;