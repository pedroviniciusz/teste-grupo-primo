INSERT INTO CONTA (numero, saldo, criado_em, atualizado_em, excluido, version)
VALUES (777, 100.00, NOW(), NULL, FALSE, 0);

INSERT INTO CONTA (numero, saldo, criado_em, atualizado_em, excluido, version)
VALUES (888, 0.00, NOW(), NULL, FALSE, 0);

INSERT INTO CONTA (numero, saldo, criado_em, atualizado_em, excluido, version)
VALUES (999, 0.00, NOW(), NULL, FALSE, 0);

INSERT INTO TRANSACAO (conta_id, tipo, valor, criado_em, atualizado_em, excluido, version)
VALUES (1, 'DEPOSITO', 70.00, NOW(), NULL, FALSE, 0);