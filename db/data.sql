INSERT INTO cat_contract(
            id, name, value)
    VALUES (1, 'cc1', 'contract category #1');

INSERT INTO cat_contract(
            id, name, value)
    VALUES (2, 'cc2', 'contract category #2');

INSERT INTO cat_contract(
            id, name, value)
    VALUES (3, 'cc3', 'contract category #3');

INSERT INTO cat_contract_status(
            id,  code, value)
    VALUES (1, 'NEW' , 'new');

INSERT INTO cat_contract_status(
            id, code,  value)
    VALUES (2,  'CANCELLED', 'cancelled');

INSERT INTO cat_contract_status(
            id, code, value)
    VALUES (3, 'ACCEPTED' , 'accept');

INSERT INTO cat_contract_status(
            id, code, value)
    VALUES (4, 'BEGIN' , 'begin');

INSERT INTO locale(
            entity, id, locale, name, value, cc_id)
    VALUES ('CatContractLocaleEntity', 1, 'ru', 'категория1', 'категория контрактов №1', 1);

INSERT INTO locale(
            entity, id, locale, name, value, cc_id)
    VALUES ('CatContractLocaleEntity', 2, 'ru', 'категория2', 'категория контрактов №2', 2);

INSERT INTO locale(
            entity, id, locale, name, value, cc_id)
    VALUES ('CatContractLocaleEntity', 3, 'en', 'category1', 'contract category №1', 1);

INSERT INTO locale(
            entity, id, locale, name, value, cc_id)
    VALUES ('CatContractLocaleEntity', 4, 'en', 'category2', 'contract category №2', 2);

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 5, 1, 'en',  'NEW');

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 6, 1, 'ru',  'НОВЫЙ');

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 7, 2, 'en',  'CANCELLED');

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 8, 2, 'ru',  'ОТМЕНЕН');

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 9, 3, 'en',  'ACCEPT');

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 10, 3, 'ru',  'ПРИНЯТ');

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 11, 4, 'en',  'BEGIN');

INSERT INTO locale(
            entity, id, ccs_id, locale, value)
    VALUES ('CatContractStatusLocaleEntity', 12, 4, 'ru',  'НАЧАЛЬНЫЙ');

INSERT INTO partner(
            id, value)
    VALUES (1, 'Партнер№1');

INSERT INTO partner(
            id, value)
    VALUES (2, 'Партнер№2');

INSERT INTO user_roles(
            id, authority)
    VALUES (1, 'ROLE_ANONYMOUS');

INSERT INTO user_roles(
            id, authority)
    VALUES (2, 'ROLE_USER_PARTNER');

INSERT INTO user_roles(
            id, authority)
    VALUES (3, 'ROLE_ADMIN_PARTNER');

INSERT INTO user_roles(
            id, authority)
    VALUES (4, 'ROLE_USER');

INSERT INTO user_roles(
            id, authority)
    VALUES (5, 'ROLE_ADMIN');

INSERT INTO users(
            id, credentials_expired, enabled, expired, locked, password,
            username, partner_id)
    VALUES (1, false, true, false, false, '1111',
            'user1', 1);

INSERT INTO users_authorities(
            user_id, auth_id)
    VALUES (1, 4), (1,2);


INSERT INTO postgres.partner_cat_contract(
            partner_id, cat_contract_id)
    VALUES (1, 1), (1, 2);


INSERT INTO risk(
            id, value)
    VALUES (1, 'risk1');

INSERT INTO cat_contract_risk(
            id, rate, cat_contract_id, risk_id)
    VALUES (1, 0.5, 1, 1);
