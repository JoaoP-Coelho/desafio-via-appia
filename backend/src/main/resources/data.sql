INSERT INTO users (id, nome, email, login, senha, role)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'Writer', 'writer@example.com', 'writer', '$2a$12$10u7rppx8i5tsAO3wJ5j7.MqaQBE0yNJSytQMTfQupT6zJCQQGPXW', 'WRITER'),
    ('00000000-0000-0000-0000-000000000002', 'Reader', 'reader@example.com', 'reader', '$2a$12$5Ri2yIIlAWBo8BDnnAEdkuS/.HkZzKuSMlKlxRVsjYgj3MZA5QCl6', 'READ_ONLY')
ON CONFLICT DO NOTHING;

INSERT INTO incident (
    id, titulo, descricao, priority, status, autor_id, responsavel_email,
    data_abertura, data_atualizacao
)
VALUES
    (
        '10000000-0000-0000-0000-000000000001',
        'Falha no acesso ao portal',
        'Usuários relatam erro ao acessar o portal corporativo.',
        'ALTA',
        'ABERTA',
        '00000000-0000-0000-0000-000000000001',
        'suporte@example.com',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        '10000000-0000-0000-0000-000000000002',
        'Lentidão no relatório mensal',
        'A geração do relatório mensal está acima do tempo esperado.',
        'MEDIA',
        'EM_ANDAMENTO',
        '00000000-0000-0000-0000-000000000001',
        'dados@example.com',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    )
ON CONFLICT DO NOTHING;

INSERT INTO incident_tags (incident_id, tag)
SELECT '10000000-0000-0000-0000-000000000001', 'acesso'
WHERE NOT EXISTS (
    SELECT 1 FROM incident_tags
    WHERE incident_id = '10000000-0000-0000-0000-000000000001' AND tag = 'acesso'
);

INSERT INTO incident_tags (incident_id, tag)
SELECT '10000000-0000-0000-0000-000000000001', 'portal'
WHERE NOT EXISTS (
    SELECT 1 FROM incident_tags
    WHERE incident_id = '10000000-0000-0000-0000-000000000001' AND tag = 'portal'
);

INSERT INTO incident_tags (incident_id, tag)
SELECT '10000000-0000-0000-0000-000000000002', 'relatorio'
WHERE NOT EXISTS (
    SELECT 1 FROM incident_tags
    WHERE incident_id = '10000000-0000-0000-0000-000000000002' AND tag = 'relatorio'
);

INSERT INTO incident_tags (incident_id, tag)
SELECT '10000000-0000-0000-0000-000000000002', 'performance'
WHERE NOT EXISTS (
    SELECT 1 FROM incident_tags
    WHERE incident_id = '10000000-0000-0000-0000-000000000002' AND tag = 'performance'
);

INSERT INTO comments (id, incident_id, autor_id, mensagem, data_criacao)
VALUES
    (
        '20000000-0000-0000-0000-000000000001',
        '10000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000001',
        'Incidente reproduzido e encaminhado para a equipe de infraestrutura.',
        CURRENT_TIMESTAMP
    ),
    (
        '20000000-0000-0000-0000-000000000002',
        '10000000-0000-0000-0000-000000000002',
        '00000000-0000-0000-0000-000000000002',
        'A análise inicial indica uma consulta lenta no banco de dados.',
        CURRENT_TIMESTAMP
    )
ON CONFLICT DO NOTHING;
