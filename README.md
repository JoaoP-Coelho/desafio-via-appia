# desafio-via-appia
Repositório utilizado para disponibilização dos arquivos referentes ao desafio da Via Appia

## Variáveis de ambiente

Para rodar o projeto são necessárias as seguintes variáveis de ambiente

1. `POSTGRES_USER`
2. `POSTGRES_PASSWORD`
3. `POSTGRES_DB`
4. `JWT_SECRET`: chave Base64 com pelo menos 256 bits para assinatura HS256

É possível achar um arquivo .env.example na raiz do projeto.

Rode o comando: cp .env.example .env para criar o .env.

E para geração da chave JWT utilize o site https://jwtsecretkeygenerator.com gerando um Standard Secret Key, com configuração padrão.

Por fim, pode ser executado o `docker-compose up --build` para rodar o projeto.

Seeds gerados via script SQL:

- `writer` (perfil de escrita)
- `reader` (perfil de listagem)

## URLs do projeto

1. Frontend: http://localhost:4200
2. Backend: http://localhost:8080
3. Swagger: http://localhost:8080/swagger-ui/index.html

# Swagger

Para utilização de métodos autenticados pelo swagger, deve-se gerar o JWT usando /api/login e colalo no Authorize.
