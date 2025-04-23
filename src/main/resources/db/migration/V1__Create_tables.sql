-- =========================================
-- Table USERS
-- =========================================
CREATE TABLE users (
                       user_id              SERIAL PRIMARY KEY,
                       user_name            VARCHAR(50)  UNIQUE NOT NULL,
                       user_email           VARCHAR(100) UNIQUE NOT NULL,
                       user_password        VARCHAR(255) NOT NULL,  -- À hasher côté application
                       user_phone           VARCHAR(30),            -- Pour gérer des formats internationaux
                       user_created_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
                       user_email_verified  BOOLEAN     DEFAULT FALSE,  -- Pour savoir si l'e-mail a été validé
                       verification_token   VARCHAR(255),              -- Pour stocker le jeton de validation
                       user_status          VARCHAR(20) DEFAULT 'actif' -- Peut servir à bannir/suspendre un compte
);

-- =========================================
-- Table LOTS
-- =========================================
CREATE TABLE lots (
                      lot_id               SERIAL PRIMARY KEY,
                      lot_name             VARCHAR(100) NOT NULL,
                      lot_description      TEXT,
                      lot_starting_price   NUMERIC(10,2) NOT NULL
    -- Vous pouvez ajouter un created_at si besoin
);

-- =========================================
-- Table AUCTIONS
-- =========================================
CREATE TABLE auctions (
                          auc_id               SERIAL PRIMARY KEY,
                          auc_start_time       TIMESTAMP   NOT NULL,
                          auc_end_time         TIMESTAMP   NOT NULL,
                          auc_current_bid      NUMERIC(10,2) DEFAULT 0,   -- Montant de la dernière enchère
                          auc_last_bidder_id   INTEGER,                  -- Référence vers l'utilisateur dernier enchérisseur
                          auc_final_price      NUMERIC(10,2),            -- Le prix final au moment de la clôture
                          auc_status           VARCHAR(20)   DEFAULT 'en cours',
    -- Exemples de statuts possibles : 'en cours', 'terminee', 'annulee', etc.
                          user_id              INTEGER NOT NULL,         -- Lien vers le créateur/propriétaire de l'enchère
                          lot_id               INTEGER NOT NULL,         -- Le lot associé
                          FOREIGN KEY (user_id) REFERENCES users(user_id),
                          FOREIGN KEY (lot_id) REFERENCES lots(lot_id),
                          FOREIGN KEY (auc_last_bidder_id) REFERENCES users(user_id)
);

-- =========================================
-- Table BIDS
-- =========================================
CREATE TABLE bids (
                      bid_id               SERIAL PRIMARY KEY,
                      bid_amount           NUMERIC(10,2) NOT NULL,
                      bid_time             TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                      auc_id               INTEGER       NOT NULL,  -- Référence vers l'enchère
                      user_id              INTEGER       NOT NULL,  -- L'utilisateur qui place la mise
                      FOREIGN KEY (auc_id) REFERENCES auctions(auc_id),
                      FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- =========================================
-- Table NOTIFICATIONS
-- =========================================
CREATE TABLE notifications (
                               not_id               SERIAL PRIMARY KEY,
                               not_read             BOOLEAN     DEFAULT FALSE,           -- true une fois que l'utilisateur l'a vue
                               not_message          TEXT,
                               not_type             VARCHAR(20),                         -- BID_PLACED, AUCTION_ENDING, etc.
                               not_sent_at          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
                               auc_id               INTEGER     NOT NULL,
                               user_id              INTEGER     NOT NULL,
                               FOREIGN KEY (auc_id) REFERENCES auctions(auc_id),
                               FOREIGN KEY (user_id) REFERENCES users(user_id)
);