do $body$
    declare
        level_country_id uuid;
        country_id uuid;
        level_capital_id uuid;
        level_sector_id uuid;
        county_id uuid;
        level_county_id uuid;

    begin

        insert into locations_levels (
            level_name,
            abbreviation,
            abbreviation_long,
            creation,
            code
        )values
             ('Country','Țară','Țară',current_timestamp,'COUNTRY'),
             ('County','Jud.','Județ',current_timestamp,'COUNTY'),
             ('Sector','Sc.','Sector',current_timestamp,'SECTOR'),
             ('Locality','Loc.','Localitate',current_timestamp,'LOCALITY'),
             ('Capital','Cap.','Capitala',current_timestamp,'CAPITAL');

        level_country_id :=(select ll.id from locations_levels ll where ll.level_name  = 'Country');

        insert into locations (
            location,
            location_level_id,
            creation
        )values
            ('ROMANIA',level_country_id,current_timestamp);

        country_id :=(select l.id from locations l where l.location = 'ROMANIA');

        level_capital_id :=(select ll.id from locations_levels ll where ll.level_name = 'Capital');

        insert into locations (
            location,
            location_level_id,
            parent_id,
            creation,
            short_code,
            legacy_id
        )values
            ('BUCURESTI', level_capital_id,country_id, current_timestamp, 'B', 1);

        level_sector_id :=(select ll.id from locations_levels ll where ll.level_name = 'Sector');

        county_id :=(select l.id from locations l where l.location = 'BUCURESTI');

        insert into locations (
            location,
            location_level_id,
            parent_id,
            creation
        )values
             ('SECTOR-1', level_sector_id, county_id, current_timestamp ),
             ('SECTOR-2', level_sector_id, county_id, current_timestamp ),
             ('SECTOR-3', level_sector_id, county_id, current_timestamp ),
             ('SECTOR-4', level_sector_id, county_id, current_timestamp ),
             ('SECTOR-5', level_sector_id, county_id, current_timestamp ),
             ('SECTOR-6', level_sector_id, county_id, current_timestamp );

        level_county_id :=(select ll.id from locations_levels ll where ll.level_name  = 'County');

        insert into locations (
            location,
            location_level_id,
            parent_id,
            creation,
            short_code,
            legacy_id
        )values
             ('ALBA',level_county_id,country_id,current_timestamp,'AB',2),
             ('ARAD',level_county_id,country_id,current_timestamp,'AR',3),
             ('ARGES',level_county_id,country_id,current_timestamp,'AG',4),
             ('BACAU',level_county_id,country_id,current_timestamp,'BC',5),
             ('BIHOR',level_county_id,country_id,current_timestamp,'BH',6),
             ('BISTRITA-NASAUD',level_county_id,country_id,current_timestamp,'BN',7),
             ('BOTOSANI',level_county_id,country_id,current_timestamp,'BT',8),
             ('BRASOV',level_county_id,country_id,current_timestamp,'BV',9),
             ('BRAILA',level_county_id,country_id,current_timestamp,'BR',10),
             ('BUZAU',level_county_id,country_id,current_timestamp,'BZ',11),
             ('CARAS-SEVERIN',level_county_id,country_id,current_timestamp,'CS',12),
             ('CALARASI',level_county_id,country_id,current_timestamp,'CL',13),
             ('CLUJ',level_county_id,country_id,current_timestamp,'CJ',14),
             ('CONSTANTA',level_county_id,country_id,current_timestamp,'CT',15),
             ('COVASNA',level_county_id,country_id,current_timestamp,'CV',16),
             ('DAMBOVITA',level_county_id,country_id,current_timestamp,'DB',17),
             ('DOLJ',level_county_id,country_id,current_timestamp,'DJ',18),
             ('GALATI',level_county_id,country_id,current_timestamp,'GL',19),
             ('GIURGIU',level_county_id,country_id,current_timestamp,'GR',20),
             ('GORJ',level_county_id,country_id,current_timestamp,'GJ',21),
             ('HARGHITA',level_county_id,country_id,current_timestamp,'HR',22),
             ('HUNEDOARA',level_county_id,country_id,current_timestamp,'HD',23),
             ('IALOMITA',level_county_id,country_id,current_timestamp,'IL',24),
             ('IASI',level_county_id,country_id,current_timestamp,'IS',25),
             ('ILFOV',level_county_id,country_id,current_timestamp,'IF',26),
             ('MARAMURES',level_county_id,country_id,current_timestamp,'MM',27),
             ('MEHEDINTI',level_county_id,country_id,current_timestamp,'MH',28),
             ('MURES',level_county_id,country_id,current_timestamp,'MS',29),
             ('NEAMT',level_county_id,country_id,current_timestamp,'NT',30),
             ('OLT',level_county_id,country_id,current_timestamp,'OT',31),
             ('PRAHOVA',level_county_id,country_id,current_timestamp,'PH',32),
             ('SATU MARE',level_county_id,country_id,current_timestamp,'SM',33),
             ('SALAJ',level_county_id,country_id,current_timestamp,'SJ',34),
             ('SIBIU',level_county_id,country_id,current_timestamp,'SB',35),
             ('SUCEAVA',level_county_id,country_id,current_timestamp,'SV',36),
             ('TELEORMAN',level_county_id,country_id,current_timestamp,'TR',37),
             ('TIMIS',level_county_id,country_id,current_timestamp,'TM',38),
             ('TULCEA',level_county_id,country_id,current_timestamp,'TL',39),
             ('VASLUI',level_county_id,country_id,current_timestamp,'VS',40),
             ('VALCEA',level_county_id,country_id,current_timestamp,'VL',41),
             ('VRANCEA',level_county_id,country_id,current_timestamp,'VN',42);

    end
$body$ language plpgsql;