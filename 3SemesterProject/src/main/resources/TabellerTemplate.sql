
CREATE TABLE production_type(
    id serial PRIMARY KEY,
    type VARCHAR(100)
);

CREATE TABLE production_name(
    id serial PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE language(
    id serial PRIMARY KEY,
    language VARCHAR(100)
);

CREATE TABLE production_company (
    id serial PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(100),
    phone INTEGER,
    email VARCHAR(100),
    country VARCHAR(100)
);

CREATE TABLE genre(
    id serial PRIMARY KEY,
    genre VARCHAR(100)
);

CREATE TABLE credit_name (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    address VARCHAR(100),
    phone INTEGER,
    email VARCHAR(10
);

CREATE TABLE credit_type(
    id SERIAL PRIMARY KEY,
    type VARCHAR(100)
);

CREATE TABLE production (
    id SERIAL PRIMARY KEY,
    season INTEGER,
    episode INTEGER,
    release_date TIMESTAMP NOT NULL,
    length INTEGER,
    subtitle BOOLEAN,
    sign_language BOOLEAN,
    active BOOLEAN,
    validated BOOLEAN,
    production_reference VARCHAR(100),
    production_bio VARCHAR(2000);

production_company_id INTEGER NOT NULL REFERENCES production_company(id),
    production_type_id INTEGER NOT NULL REFERENCES production_type(id),
    language_id INTEGER NOT NULL REFERENCES language(id),
    production_name_id INTEGER NOT NULL REFERENCES production_name(id)
);

CREATE TABLE genres_production_association(
    production_id INTEGER NOT NULL REFERENCES production(id),
    genre_id INTEGER NOT NULL REFERENCES genre(id)
);

CREATE TABLE credit (
    id SERIAL PRIMARY KEY,
    role VARCHAR(100),
    validated BOOLEAN,
    production_id INTEGER NOT NULL REFERENCES production(id)
);

CREATE TABLE credit_name_credit_type_association (
    credit_name_id INTEGER NOT NULL REFERENCES credit_name(id),
    credit_type_id INTEGER NOT NULL REFERENCES credit_type(id),
    credit_id INTEGER NOT NULL REFERENCES credit(id)
);

INSERT INTO credit_type (type) VALUES ('Billedkunstner');
INSERT INTO credit_type (type) VALUES ('Billed og Lydredigering');
INSERT INTO credit_type (type) VALUES ('Casting');
INSERT INTO credit_type (type) VALUES ('Colourgrading');
INSERT INTO credit_type (type) VALUES ('Dirigenter');
INSERT INTO credit_type (type) VALUES ('Drone');
INSERT INTO credit_type (type) VALUES ('Dukkefører');
INSERT INTO credit_type (type) VALUES ('Dukkeskaber');
INSERT INTO credit_type (type) VALUES ('Fortæller');
INSERT INTO credit_type (type) VALUES ('Fotograf');
INSERT INTO credit_type (type) VALUES ('Forlæg');
INSERT INTO credit_type (type) VALUES ('Grafisk Designer');
INSERT INTO credit_type (type) VALUES ('Indtaler');
INSERT INTO credit_type (type) VALUES ('Kapelmester');
INSERT INTO credit_type (type) VALUES ('Klipper');
INSERT INTO credit_type (type) VALUES ('Programkoncept');
INSERT INTO credit_type (type) VALUES ('Konsulent');
INSERT INTO credit_type (type) VALUES ('Kor');
INSERT INTO credit_type (type) VALUES ('Koreografi');
INSERT INTO credit_type (type) VALUES ('Lyd');
INSERT INTO credit_type (type) VALUES ('Tonemester');
INSERT INTO credit_type (type) VALUES ('Ledredigering');
INSERT INTO credit_type (type) VALUES ('Lys');
INSERT INTO credit_type (type) VALUES ('Medvirkende');
INSERT INTO credit_type (type) VALUES ('Musiker');
INSERT INTO credit_type (type) VALUES ('Musikalsk Arrangement');
INSERT INTO credit_type (type) VALUES ('Orkester');
INSERT INTO credit_type (type) VALUES ('Band');
INSERT INTO credit_type (type) VALUES ('Oversætter');
INSERT INTO credit_type (type) VALUES ('Producent');
INSERT INTO credit_type (type) VALUES ('Producer');
INSERT INTO credit_type (type) VALUES ('Produktionskoordinator');
INSERT INTO credit_type (type) VALUES ('Produktionsleder');
INSERT INTO credit_type (type) VALUES ('Programansvarlig');
INSERT INTO credit_type (type) VALUES ('Redaktion');
INSERT INTO credit_type (type) VALUES ('Tilrettelæggelse')
INSERT INTO credit_type (type) VALUES ('Redaktør');
INSERT INTO credit_type (type) VALUES ('Rekvisitør');
INSERT INTO credit_type (type) VALUES ('Scenograf');
INSERT INTO credit_type (type) VALUES ('Scripter');
INSERT INTO credit_type (type) VALUES ('Producerassistent');
INSERT INTO credit_type (type) VALUES ('SpecialEffects');
INSERT INTO credit_type (type) VALUES ('Sponsor');
INSERT INTO credit_type (type) VALUES ('Tegnefilm');
INSERT INTO credit_type (type) VALUES ('Animation');
INSERT INTO credit_type (type) VALUES ('Tekster');
INSERT INTO credit_type (type) VALUES ('Tekst og musik');
INSERT INTO credit_type (type) VALUES ('Uhonoreret ekstraordinær indsats');

INSERT INTO genre (genre) VALUES ('Drama');
INSERT INTO genre (genre) VALUES ('Komedie');
INSERT INTO genre (genre) VALUES ('Animation');
INSERT INTO genre (genre) VALUES ('Thriller');
INSERT INTO genre (genre) VALUES ('Action');
INSERT INTO genre (genre) VALUES ('Fantasy');
INSERT INTO genre (genre) VALUES ('Gyser');
INSERT INTO genre (genre) VALUES ('Romantisk');
INSERT INTO genre (genre) VALUES ('Western');
INSERT INTO genre (genre) VALUES ('Sci-fi');
INSERT INTO genre (genre) VALUES ('Krig');
INSERT INTO genre (genre) VALUES ('Biografi');
INSERT INTO genre (genre) VALUES ('Dokumentar');
INSERT INTO genre (genre) VALUES ('Reality');
INSERT INTO genre (genre) VALUES ('Andet');

INSERT INTO language (language) VALUES ('Dansk');
INSERT INTO language (language) VALUES ('Svensk');
INSERT INTO language (language) VALUES ('Norsk');
INSERT INTO language (language) VALUES ('Engelsk');
INSERT INTO language (language) VALUES ('Tysk');
INSERT INTO language (language) VALUES ('Spansk');
INSERT INTO language (language) VALUES ('Italiensk');
INSERT INTO language (language) VALUES ('Andet');

INSERT INTO production_type (type) VALUES ('Film');
INSERT INTO production_type (type) VALUES ('Serie');


INSERT INTO production_company (name, address, phone, email, country) VALUES ('SF Film Production ApS', 'filmbyen 1, 5000 Oense', 62856381, 'sffilm@badhotellet.dk', 'Denmark');

INSERT INTO production_name (name) VALUES ('Badehotellet');

INSERT INTO production (season, episode, release_date, length, subtitle, sign_language, active, validated, production_reference, production_company_id, production_type_id, language_id, production_name_id) VALUES (1, 2, '1978-06-23 00:00:00.000000', 43, true, false, true, true, 'SF102', 1, 2, 1, 1);
UPDATE production SET production_bio='badehotellet er en helt fantastisk serie om et badehotel' WHERE production.id = 1;

-- indsætter skuespillere i credit_name
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Rosalinde', 'Mynster', 'Poulstrupvej 8', 80997397, 'rosalinde@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Bodil', 'Jørgensen', 'Vær Bakkevej 50', 74786567, 'bodil@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Morten', 'Hemmingsen', 'Kræn Hansensvej 8', 31136419, 'morten@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Ena', 'Spottag', 'Jens Otto Krags Plads 24', 21446244, 'ena@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Ulla', 'Vejby', 'Bymarken 38', 72789301, 'ulla@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Merete', 'Damgaard Mærkedahl', 'Sneglevænget 3', 17962672, 'merete@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Lars', 'Ranthe', 'Bredager Mark 39', 22525248, 'lars@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Anne', 'Louise Hassing', 'Studiestrædet 46', 77623013, 'anne@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Amalie', 'Dollerup', 'Minervavej 36', 70944066, 'amalie@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Peter', 'Hesse Overgaard', 'Krogerupvej 14', 81914964, 'peter@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Cecilie', 'Stenspil', 'Anemonevej 49', 90585762, 'cecilie@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Jens', 'Jacob Tychsen', 'Skovbrynet 42', 20332722, 'jens@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Bjarne', 'Henriksen', 'Rødkælkevej 32', 32358841, 'bjarne@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Anette', 'Støvelbæk', 'Enghavevej 51', 32641190, 'anette@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Birthe', 'Neumann', 'Malervej 12', 13206378, 'birthe@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Sonja', 'Oppenhagen', 'Sylvestervej 46', 30356155, 'sonja@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Anders', 'Juul', 'Grimsbyvej 46', 54870278, 'anders@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Sigurd', 'Holmen Le Dous', 'Kirsebærvej 34', 15438710, 'sigurd@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Mads', 'Wille', 'Limfjordsvej 44', 43380785, 'mads@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Alberte', 'Blichfeldt', 'Mejlsvej 44', 74723710, 'alberte@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Liva', 'Kristoffersen', 'Frydendalsvej 24', 16819432, 'liva@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Peter', 'Gilsfort', 'Håndværkervej 25', 43285932, 'peter@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Lukas', 'Helt', 'Fasanvej 33', 59213472, 'lukas@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Lykke', 'Scheuer', 'Thoustrupvej 22', 97208476, 'lykke@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Jeppe', 'Koudal Frostholm', 'Nordre Kåstrupvej 50', 14839387, 'jeppe@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Waage', 'Sandø', 'Jedsted Møllevej 26', 77654413, 'waage@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Robert', 'Hansen', 'Ågårdsvej 17', 45516785, 'robert@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Benedikte', 'Hansen', 'Provst Petersensvej 35', 45415148, 'benedikte@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Ole', 'Dupont', 'Daugårdvej 4', 64121838, 'ole@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Martin', 'Greisen', 'Vrouevej 51', 42487591, 'martin@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Stig', 'Hoffmeyer', 'Norgaardsvej 39', 45000251, 'stig@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Jesper', 'Riefensthal', 'Østertorv 5', 70993077, 'jesper@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Ole', 'Thestrup', 'Mejsevej 39', 34691438, 'ole@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Sverrir', 'Gudnason', 'Tjørnevej 35', 17931121, 'sverrir@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Hanne', 'Windfeld', 'Ø. Børstingvej 44', 29632276, 'hanne@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Ida', 'Dwinger', 'Tågelundvej 33', 72044566, 'ida@credits.dk');
INSERT INTO credit_name (first_name, last_name, address, phone, email) VALUES ('Henrik', 'Birch', 'Vesterkærsvej 7', 16686398, 'henrik@credits.dk');

-- indsætter krediteringer i credit
INSERT INTO credit (role, validated, production_id) VALUES ('Fie', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Molly Andersen', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Morten Enevoldsen', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Martha, kokkepige', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Edith, stuepige', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Otilia, stuepige', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Grosserer Georg Madsen', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Therese Madsen', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Amanda Berggren (f. Madsen)', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Kontorchef Hjalmar Aurland', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Helene Aurland', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Skuespiller Edward Weyse', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Fabrikant Otto Frigh', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Alice Frigh', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Fru (Olga) Fjeldsø', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Lydia Vetterstrøm (Fjeldsøs søster)', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Arkitekt Max Berggren', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Reklametegner Philip Dupont', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Grev Ditmar', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Vera', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Bertha', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Alfred Jensen', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Leslie', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Frk. Malling', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Postbud', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Grev Valdemar', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Verner', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Grevinde Anne-Grethe', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Bargæst', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Poul', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Hr. Gottlieb', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Ingeniør Rønn', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Julius Andersen', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Bremer', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Fru Damgaard', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Fru Skibsreder Holm', true, 1);
INSERT INTO credit (role, validated, production_id) VALUES ('Arne Kokholm', true, 1);

-- indsætter foreign keys i credit_name_credit_type_association
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (1, 23, 1);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (2, 23, 2);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (3, 23, 3);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (4, 23, 4);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (5, 23, 5);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (6, 23, 6);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (7, 23, 7);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (8, 23, 8);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (9, 23, 9);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (10, 23, 10);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (11, 23, 11);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (12, 23, 12);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (13, 23, 13);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (14, 23, 14);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (15, 23, 15);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (16, 23, 16);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (17, 23, 17);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (18, 23, 18);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (19, 23, 19);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (20, 23, 20);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (21, 23, 21);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (22, 23, 22);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (23, 23, 23);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (24, 23, 24);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (25, 23, 25);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (26, 23, 26);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (27, 23, 27);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (28, 23, 28);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (29, 23, 29);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (30, 23, 30);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (31, 23, 31);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (32, 23, 32);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (33, 23, 33);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (34, 23, 34);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (35, 23, 35);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (36, 23, 36);
INSERT INTO credit_name_credit_type_association (credit_name_id, credit_type_id, credit_id) VALUES (37, 23, 37);




