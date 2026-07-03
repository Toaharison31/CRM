/*Database name: gestion_crm */
CREATE DATABASE gestion_crm WITH ENCODING 'UTF8';


/*Client: 8 variables*/
CREATE TABLE IF NOT EXISTS client ( 
    id SERIAL PRIMARY KEY, 
    nom_complet VARCHAR(250) NOT NULL, 
    email VARCHAR(250) NOT NULL UNIQUE, 
    telephone VARCHAR(15) NOT NULL UNIQUE, 
    entreprise VARCHAR(50) NOT NULL, 
    adresse VARCHAR(100) NOT NULL, 
    statut VARCHAR(10) CHECK (statut IN ('actif','inactif')), 
    cree_le TIMESTAMP DEFAULT NOW() ); 

/*Utilisateur: 6 variables*/
CREATE TABLE IF NOT EXISTS utilisateur ( 
    id SERIAL PRIMARY KEY, 
    nom VARCHAR(100), 
    email VARCHAR(100), 
    mot_de_passe VARCHAR(255),
    role VARCHAR(20) CHECK (role IN ('admin','commercial','manager')), 
    cree_le TIMESTAMP DEFAULT NOW() );

 
 /*Contact : 7 variables */
CREATE TABLE IF NOT EXISTS contact ( 
    id SERIAL PRIMARY KEY, 
    id_client INTEGER REFERENCES client(id), 
    type_contact VARCHAR(10) CHECK (type_contact IN ('appel','email','réunion')), 
    notes TEXT, 
    date_contact DATE, 
    cree_par INTEGER REFERENCES utilisateur(id) ); 
 
 
 /* Opportunité : 7 variables */
CREATE TABLE IF NOT EXISTS opportunite ( 
    id SERIAL PRIMARY KEY, 
    id_client INTEGER REFERENCES client(id), 
    titre VARCHAR(100), 
    valeur DECIMAL(15,2) DEFAULT 0.00, 
    etape VARCHAR(30) CHECK (etape IN ('prospect','qualifié','proposition','gagné','perdu')), 
    date_cloture DATE, 
    cree_le TIMESTAMP DEFAULT NOW() 
    cree_par INTEGER REFERENCES utilisateur(id)); 
 
 
 /*Activité : 8 variables */
CREATE TABLE IF NOT EXISTS activite ( 
    id SERIAL PRIMARY KEY, 
    id_utilisateur INTEGER REFERENCES utilisateur(id), 
    id_opportunite INTEGER REFERENCES opportunite(id), 
    type_activite VARCHAR(20) CHECK (type_activite IN ('appel','tâche','note')), 
    description TEXT, 
    date_echeance DATE, 
    statut VARCHAR(20) CHECK (statut IN ('en attente','terminé','annulé')), 
    cree_le TIMESTAMP DEFAULT NOW() );
  
  
 /*Rapport : 5 variables */
CREATE TABLE IF NOT EXISTS rapport ( 
    id SERIAL PRIMARY KEY, 
    type_rapport VARCHAR(20) CHECK (type_rapport IN ('hebdomadaire','mensuel','par commercial')), 
    genere_par INTEGER REFERENCES utilisateur(id), 
    donnees JSON, 
    cree_le TIMESTAMP DEFAULT NOW() );