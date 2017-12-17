set ignorecase true;

drop table if exists airports;
create table airports(
    id	                int not null,
    ident	            varchar(255) not null,
    type1	            varchar(20),
    name                varchar(255),
    latitude_deg	    decimal(15,10),
    longitude_deg	    decimal(15,10),
    elevation_ft	    int,
    continent	        varchar(20),
    iso_country	        varchar(2),
    iso_region	        varchar(10),
    municipality	    varchar(255),
    scheduled_service   varchar(3),
    gps_code	        varchar(255),
    iata_code	        varchar(255),
    local_code	        varchar(255),
    home_link	        varchar(255),
    wikipedia_link	    varchar(500),
    keywords            varchar(500),
    constraint pk_airports primary key (id))
    as select
    id,
    ident,
    type,
    name,
    latitude_deg,
    longitude_deg,
    elevation_ft,
    continent,
    iso_country,
    iso_region,
    municipality,
    scheduled_service,
    gps_code,
    iata_code,
    local_code,
    home_link,
    wikipedia_link,
    keywords
    from CSVREAD('conf/csvs/airports.csv')
;

drop table if exists countries;
create table countries(
    id	                int not null,
    code	            varchar(2),
    name	            varchar(255),
    continent	        varchar(2),
    wikipedia_link	    varchar(500),
    keywords            varchar(500),
    constraint pk_countries primary key (id))
    as select
    id,
    code,
    name,
    continent,
    wikipedia_link,
    keywords
    from CSVREAD('conf/csvs/countries.csv')
;

drop table if exists runways;
create table runways(
    id	                int not null,
    airport_ref	        int,
    airport_ident	    varchar(20),
    length_ft	        int,
    width_ft	        int,
    surface	            varchar(200),
    lighted	            int,
    closed	            int,
    le_ident	        varchar(10),
    le_latitude_deg	    decimal(20,10),
    le_longitude_deg	decimal(20,10),
    le_elevation_ft	    decimal(20,10),
    le_heading_degT	    decimal(20,10),
    le_displaced_threshold_ft int,
    he_ident	        varchar(10),
    he_latitude_deg	    decimal(20,10),
    he_longitude_deg	decimal(20,10),
    he_elevation_ft	    decimal(20,10),
    he_heading_degT	    decimal(20,10),
    he_displaced_threshold_ft int,
    constraint pk_runways primary key(id),
    constraint fk_runways foreign key(airport_ref) references airports(id))
    as select
    id,
    airport_ref,
    airport_ident,
    length_ft,
    width_ft,
    surface,
    lighted,
    closed,
    le_ident,
    le_latitude_deg,
    le_longitude_deg,
    le_elevation_ft,
    le_heading_degT,
    le_displaced_threshold_ft,
    he_ident,
    he_latitude_deg,
    he_longitude_deg,
    he_elevation_ft,
    he_heading_degT,
    he_displaced_threshold_ft
    from CSVREAD('conf/csvs/runways.csv')
;


create sequence airports_seq start with 1000;

create sequence countries_seq start with 1000;

create sequence runways_seq start with 1000;

create index ix_countries on countries (name);



SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists countries;

drop table if exists runways;

drop table if exists airports;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists airports_seq;

drop sequence if exists countries_seq;

drop sequence if exists runways_seq;
