# desafio-via-appia
Repositório utilizado para disponibilização dos arquivos referentes ao desafio da Via Appia

## Variáveis de ambiente

Para rodar o projeto são necessárias as seguintes variáveis de ambiente

1. `POSTGRES_USER`
2. `POSTGRES_PASSWORD`
3. `POSTGRES_DB`
4. `JWT_SECRET`: chave Base64 com pelo menos 256 bits para assinatura HS256

É possível achar um arquivo .env.example na raiz do projeto.

E para geração da chave JWT utilize o site https://jwtsecretkeygenerator.com gerando um Standard Secret Key, com configuração padrão.

Seeds gerados via script SQL:

- `writer` (perfil de escrita)
- `reader` (perfil de listagem)
