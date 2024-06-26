CREATE TABLE Student(
   cip VARCHAR(8),
   lastname VARCHAR(50),
   firstname VARCHAR(50),
   email VARCHAR(50),
   PRIMARY KEY(cip)
);

CREATE TABLE Project(
   id_project SERIAL,
   max_member INTEGER,
   PRIMARY KEY(id_project)
);

CREATE TABLE Event(
   id_event VARCHAR(50),
   cip VARCHAR(50),
   date_event timestamp,
   id_project INTEGER,
   PRIMARY KEY(id_event),
   FOREIGN KEY(id_project) REFERENCES Project(id_project)
);

CREATE TABLE File(
   id_file VARCHAR(50),
   name VARCHAR(50),
   size BIGINT,
   last_change timestamp,
   id_project INTEGER NOT NULL,
   PRIMARY KEY(id_file),
   FOREIGN KEY(id_project) REFERENCES Project(id_project)
);

CREATE TABLE Tutor(
   cip VARCHAR(8),
   firstname VARCHAR(50),
   lastname VARCHAR(50),
   email VARCHAR(50),
   PRIMARY KEY(cip)
);

CREATE TABLE Course(
   sigle VARCHAR(50),
   name VARCHAR(50),
   team_size INTEGER,
   remise DATE,
   PRIMARY KEY(sigle)
);

CREATE TABLE Student_Project(
   cip VARCHAR(8),
   id_project INTEGER,
   PRIMARY KEY(cip, id_project),
   FOREIGN KEY(cip) REFERENCES Student(cip),
   FOREIGN KEY(id_project) REFERENCES Project(id_project)
);

CREATE TABLE Student_Course(
   cip VARCHAR(8),
   sigle VARCHAR(50),
   PRIMARY KEY(cip, sigle),
   FOREIGN KEY (cip) REFERENCES Student(cip),
   FOREIGN KEY (sigle) REFERENCES Course(sigle)
);

CREATE TABLE Tutor_Course(
   cip VARCHAR(8),
   sigle VARCHAR(50),
   PRIMARY KEY(cip, sigle),
   FOREIGN KEY(cip) REFERENCES Tutor(cip),
   FOREIGN KEY(sigle) REFERENCES Course(sigle)
);

CREATE TABLE Course_Project(
   sigle VARCHAR(50),
   id_project INTEGER,
   PRIMARY KEY(sigle, id_project),
   FOREIGN KEY(sigle) REFERENCES Course(sigle),
   FOREIGN KEY(id_project) REFERENCES Project(id_project)
);


INSERT INTO Student VALUES ('brol1606', 'brochu', 'laurent', 'brol1606@usherbrooke.ca');
INSERT INTO Student VALUES ('aubm1811', 'aubin', 'maxime', 'aubm1811@usherbrooke.ca');
INSERT INTO Student VALUES ('belj1922', 'belval', 'jean-philippe', 'belj1922@usherbrooke.ca');
INSERT INTO Student VALUES ('lepl1501', 'lépine', 'luka', 'lepl1501@usherbrooke.ca');
INSERT INTO Student VALUES ('ouej2018', 'ouellet', 'jérôme', 'ouej2018@usherbrooke.ca');
INSERT INTO Tutor VALUES ('maif1401', 'mailhot', 'frédéric', 'maif1401@usherbrooke.ca');
INSERT INTO Tutor (cip, lastname, firstname, email) VALUES ('beab1802', 'Beaulieu', 'Bernard', 'beab1802@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('mona3503', 'Mongelos Toledo', 'Anahi Michelle', 'mona3503@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('bakk1001', 'Bakayoko', 'Kanvali', 'bakk1001@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('pald2501', 'Palao Munoz', 'Domingo', 'pald2501@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('malm1708', 'Malahi', 'Mohammed Saâd', 'malm1708@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('simw2402', 'Simard', 'William', 'simw2402@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('wars1601', 'Wardani', 'Sarah', 'wars1601@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('stjm1101', 'St-Jean', 'Médérick', 'stjm1101@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('cotg1816', 'Côté', 'Gabriel-Antoine', 'cotg1816@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('lers0601', 'Leroux', 'Simon', 'lers0601@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('chaj1737', 'Charette', 'Jordan', 'chaj1737@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('turs1507', 'Turcotte', 'Sarah-Maude', 'turs1507@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('lefj1811', 'Lefebvre', 'Jacob', 'lefj1811@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('diaa2222', 'Dia', 'Abibatou', 'diaa2222@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('lamv1904', 'Lamy', 'Vincent', 'lamv1904@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('begf1702', 'Bégin', 'Félix-Antoine', 'begf1702@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('sims3007', 'Simard', 'Samuel', 'sims3007@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('hasa3302', 'Hassoun', 'Ali', 'hasa3302@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('gagm1108', 'Gagné', 'Marie-Joëlle', 'gagm1108@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('rodv1001', 'Rodrigue-Senécal', 'Victor', 'rodv1001@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('egom1401', 'Egolf', 'Maxime', 'egom1401@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('pelc1105', 'Pelchat', 'Cédrick', 'pelc1105@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('hofm2702', 'Hofheinz', 'Max', 'hofm2702@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('mara2040', 'Martin', 'Antoine', 'mara2040@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('merm1504', 'Mercier', 'Mathieu', 'merm1504@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('forv1401', 'Fortier', 'Vincent', 'forv1401@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('lavv1901', 'Lavallée', 'Vincent', 'lavv1901@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('cusj3102', 'Cusson', 'Jérémy', 'cusj3102@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('meda4684', 'Meddeb', 'Aref', 'meda4684@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('mahf0901', 'Maheux', 'François', 'mahf0901@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('graw0801', 'Gravel-Tremblay', 'William', 'graw0801@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('gosj2008', 'Gosselin', 'Jean-Nicolas', 'gosj2008@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('pary1101', 'Paradis', 'Yoan', 'pary1101@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('simv2104', 'Simard-Schmidt', 'Vincent', 'simv2104@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('fora1819', 'Fortin', 'Arthur-Olivier', 'fora1819@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('blam1614', 'Blais', 'Marianne', 'blam1614@usherbrooke.ca');
INSERT INTO Student (cip, lastname, firstname, email) VALUES ('nicm1501', 'Nicol', 'Mathieu', 'nicm1501@usherbrooke.ca');

INSERT INTO Course VALUES ('gif333', 'math', 2, '2024-07-08');
INSERT INTO Tutor_Course VALUES ('maif1401', 'gif333');
INSERT INTO Project VALUES ('1');
INSERT INTO Project VALUES ('2');
INSERT INTO Course_Project VALUES ('gif333', '1');
INSERT INTO Course_Project VALUES ('gif333', '2');
INSERT INTO Student_Course VALUES ('lepl1501', 'gif333');
INSERT INTO Student_Course VALUES ('brol1606', 'gif333');
INSERT INTO Student_Course VALUES ('aubm1811', 'gif333');
INSERT INTO Student_Course VALUES ('belj1922', 'gif333');