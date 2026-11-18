-- Script de création de la base de données pour la gestion des absences
-- Base de données: absence_db

CREATE DATABASE IF NOT EXISTS absence_db;
USE absence_db;

-- Table Classe
CREATE TABLE IF NOT EXISTS Classe (
    id_classe INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL,
    niveau VARCHAR(50),
    filiere VARCHAR(100)
);

-- Table Etudiant
CREATE TABLE IF NOT EXISTS Etudiant (
    id_etudiant INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    id_classe INT,
    FOREIGN KEY (id_classe) REFERENCES Classe(id_classe) ON DELETE SET NULL
);

-- Table Enseignant
CREATE TABLE IF NOT EXISTS Enseignant (
    id_enseignant INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    pwd VARCHAR(255) NOT NULL
);

-- Table Responsable
CREATE TABLE IF NOT EXISTS Responsable (
    id_responsable INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    pwd VARCHAR(255) NOT NULL
);

-- Table Matiere
CREATE TABLE IF NOT EXISTS Matiere (
    id_matiere INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL
);

-- Table Correspondance (enseignant-matière-classe)
CREATE TABLE IF NOT EXISTS Correspondance (
    id_correspondance INT PRIMARY KEY AUTO_INCREMENT,
    id_enseignant INT NOT NULL,
    id_matiere INT NOT NULL,
    id_classe INT NOT NULL,
    FOREIGN KEY (id_enseignant) REFERENCES Enseignant(id_enseignant) ON DELETE CASCADE,
    FOREIGN KEY (id_matiere) REFERENCES Matiere(id_matiere) ON DELETE CASCADE,
    FOREIGN KEY (id_classe) REFERENCES Classe(id_classe) ON DELETE CASCADE,
    UNIQUE KEY unique_correspondance (id_enseignant, id_matiere, id_classe)
);

-- Table Absence
CREATE TABLE IF NOT EXISTS Absence (
    id_absence INT PRIMARY KEY AUTO_INCREMENT,
    id_etudiant INT NOT NULL,
    id_enseignant INT NOT NULL,
    id_matiere INT NOT NULL,
    numseance INT,
    date DATE NOT NULL,
    justification BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_etudiant) REFERENCES Etudiant(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_enseignant) REFERENCES Enseignant(id_enseignant) ON DELETE CASCADE,
    FOREIGN KEY (id_matiere) REFERENCES Matiere(id_matiere) ON DELETE CASCADE
);

-- Insertion de données d'exemple

-- Classes
INSERT INTO Classe (libelle, niveau, filiere) VALUES
('L1 Info', 'L1', 'Informatique'),
('L2 Info', 'L2', 'Informatique'),
('L3 Info', 'L3', 'Informatique'),
('M1 Info', 'M1', 'Informatique'),
('L1 Math', 'L1', 'Mathématiques'),
('L2 Math', 'L2', 'Mathématiques');

-- Matières
INSERT INTO Matiere (libelle) VALUES
('Programmation Java'), -- 1
('Base de données'),    -- 2
('Algorithmique'),      -- 3
('Réseaux'),            -- 4
('Mathématiques'),      -- 5
('Statistiques');       -- 6

-- Enseignants
INSERT INTO Enseignant (nom, prenom, login, pwd) VALUES
('Enseignant', 'Demo 1', 'enseignant1', SHA2('demo123', 256)),
('Enseignant', 'Demo 2', 'enseignant2', SHA2('demo123', 256)),
('Enseignant', 'Demo 3', 'enseignant3', SHA2('demo123', 256)),
('Enseignant', 'Demo 4', 'enseignant4', SHA2('demo123', 256)),
('Enseignant', 'Demo 5', 'enseignant5', SHA2('demo123', 256));

-- Responsable
INSERT INTO Responsable (nom, prenom, login, pwd) VALUES
('Administrateur', 'Demo', 'admin', SHA2('demo123', 256));

-- Etudiants
INSERT INTO Etudiant (nom, prenom, login, pwd, id_classe) VALUES
('Etudiant', 'Demo 1', 'etudiant1', SHA2('demo123', 256), 1),
('Etudiant', 'Demo 2', 'etudiant2', SHA2('demo123', 256), 1),
('Etudiant', 'Demo 3', 'etudiant3', SHA2('demo123', 256), 1),
('Etudiant', 'Demo 4', 'etudiant4', SHA2('demo123', 256), 2),
('Etudiant', 'Demo 5', 'etudiant5', SHA2('demo123', 256), 1),
('Etudiant', 'Demo 6', 'etudiant6', SHA2('demo123', 256), 2),
('Etudiant', 'Demo 7', 'etudiant7', SHA2('demo123', 256), 2),
('Etudiant', 'Demo 8', 'etudiant8', SHA2('demo123', 256), 4),
('Etudiant', 'Demo 9', 'etudiant9', SHA2('demo123', 256), 1),
('Etudiant', 'Demo 10', 'etudiant10', SHA2('demo123', 256), 5),
('Etudiant', 'Demo 11', 'etudiant11', SHA2('demo123', 256), 6),
('Etudiant', 'Demo 12', 'etudiant12', SHA2('demo123', 256), 2),
('Etudiant', 'Demo 13', 'etudiant13', SHA2('demo123', 256), 3),
('Etudiant', 'Demo 14', 'etudiant14', SHA2('demo123', 256), 1);

-- Correspondances
INSERT INTO Correspondance (id_enseignant, id_matiere, id_classe) VALUES
(1, 1, 1),
(1, 1, 2),
(2, 2, 1),
(2, 2, 2),
(3, 6, 2),
(3, 6, 1),
(4, 4, 1),
(4, 4, 2),
(5, 3, 1);

-- Absences

INSERT INTO Absence (id_etudiant, id_enseignant, id_matiere, numseance, date, justification) VALUES
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant5'), 1, 1, 1, '2024-10-01', FALSE),
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant5'), 1, 1, 2, '2024-10-08', TRUE),
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant5'), 2, 2, 1, '2024-10-02', FALSE);


INSERT INTO Absence (id_etudiant, id_enseignant, id_matiere, numseance, date, justification) VALUES
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant6'), 1, 1, 3, '2024-10-15', FALSE);


INSERT INTO Absence (id_etudiant, id_enseignant, id_matiere, numseance, date, justification) VALUES
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant7'), 5, 3, 1, '2024-11-01', FALSE),
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant7'), 5, 3, 2, '2024-11-08', FALSE),
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant7'), 5, 3, 3, '2024-11-15', TRUE),
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant7'), 5, 3, 4, '2024-11-22', FALSE);


INSERT INTO Absence (id_etudiant, id_enseignant, id_matiere, numseance, date, justification) VALUES
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant9'), 1, 1, 5, '2024-12-01', TRUE),
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant10'), 2, 2, 1, '2024-09-15', FALSE),

((SELECT id_etudiant FROM Etudiant WHERE login='etudiant11'), 2, 2, 2, '2024-09-22', FALSE),
((SELECT id_etudiant FROM Etudiant WHERE login='etudiant14'), 1, 1, 10, '2025-01-10', FALSE);
