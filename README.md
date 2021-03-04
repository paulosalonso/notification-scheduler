# Notification Scheduler

## Sobre o projeto

O projeto tem como propósito disponibilizar uma API através da qual seja possível enviar notificações em uma data/hora definida pelo cliente. A notificação pode ser entregue através de diferentes canais de comunicação: e-mail, sms, push e Whatsapp.
O canal de comunicação também é definido pelo cliente ao enviar a requisição.

## Arquitetura

O projeto foi construído utilizando [Clean Arch](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

## Qualidade
[![Automated Testing](https://github.com/paulosalonso/notification-scheduler/actions/workflows/automated-testing.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/automated-testing.yml)
[![Mutation Testing](https://github.com/paulosalonso/notification-scheduler/actions/workflows/mutation-testing.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/mutation-testing.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paulosalonso_notification-scheduler&metric=alert_status)](https://sonarcloud.io/dashboard?id=paulosalonso_notification-scheduler)

A qualidade da aplicação é garantida através dos testes unitários e integrados. Utiliza o [JaCoCo](https://www.jacoco.org/) para validação de cobertura mínima e o [PIT Mutation](https://pitest.org/) para testar mutações de código.
Também é realizada uma análise estática com o [SonarCloud](https://sonarcloud.io/dashboard?id=paulosalonso_research).

## Integração Contínua (CI)

![Docker Hub CI](https://github.com/paulosalonso/research/workflows/Docker%20Hub%20CI/badge.svg)

A cada entrega de código (push) os testes são executados e o novo código só é incorporado (merge) a branch main se os testes forem executados com sucesso.
A cada release é criada uma imagem Docker da versão no Docker Hub (paulosalonso/notification-scheduler).

## Entrega Contínua (CD)

[![Heroku CD](https://github.com/paulosalonso/notification-scheduler/actions/workflows/heroku-cd.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/heroku-cd.yml)

Para realizar um deploy no Heroku basta criar uma branch no padrão release-candidate-[0-9]+.[0-9]+.[0-9]+ (release-candidate-0.0.1, por exemplo)

## Banco de dados

A aplicação utiliza o bando de dados MySQL, inclusive nos testes integrados utilizando [Test Containers](https://www.testcontainers.org/).

### Configuração de conexão

Para configurar a URL de conexão crie uma variável de ambiente chamada __spring.datasource.url__ com a string de conexão adequada. Exemplo:

> export spring.datasource.url=jdbc:mysql://192.168.0.100:3306/notification-scheduler

## API

O root path da API é __/__

### Documentação

A API é documentada utilizando [OpenAPI](https://swagger.io/specification/) e pode ser acessada via navegador através do path __/swagger-ui/index.html__

[comment]: <> (### Segurança)

[comment]: <> (A API é protegida via autenticação com OAuth2. A autenticação deve ser feita com um Bearer Token &#40;JWT&#41;.)

[comment]: <> (#### JWT + Secret Key)

[comment]: <> (O profile padrão &#40;default&#41; espera por uma secret via variável de ambiente &#40;SECURITY_JWT_SIGNATURE_SECRET&#41; para decodificar o token.)

[comment]: <> (Nesse caso o algoritmo utilizado por padrão é o HmacSHA256, que também pode ser customizado via variável de ambiente &#40;SECURITY_JWT_SIGNATURE_ALGORITHM&#41;.)

[comment]: <> (#### JWT + JWK)

[comment]: <> (O profile "jwk" habilita a utlização de JWK &#40;JSON Web Key&#41; para decodificação do token. Nesse caso deve ser informada a URI para obtenção da chave pública via variável de ambiente &#40;SECURITY_JWT_SIGNATURE_JWK-SET-URI&#41;.)

## Execução

Veremos algumas formas para executar a aplicação. Para todas elas é importante observar a cofiguração das variáveis de ambiente citadas anteriormente.

### IDE

Para executar a aplicação na IDE basta importar o projeto e executar a classe com.github.paulosalonso.notification.application.NotificationSchedulerApplication como uma aplicação Java.

### Maven

> mvn spring-boot:run

### java -jar
> mvn clean package \
> java -jar target/research.jar

### Docker

Para rodar um container Docker da aplicação a partir de uma [imagem do Docker Hub](https://hub.docker.com/repository/docker/paulosalonso/notification-scheduler), acesse o diretório __.docker__ e rode o comando abaixo:

> docker-compose up

[comment]: <> (### Segurança)

[comment]: <> (Ao rodar a aplicação através do docker-compose disponibilizado no projeto, um container do Keycloak será executado também. O Keycloak já é iniciado com o realm "researh", o client "openapi" e secret "01a13864-0d17-441a-8721-a222bcf17842", e os usuários "adm" com a senha "123456" e "user" com a senha "123456".)

[comment]: <> (No diretório .postman há uma coleção com requests de autenticação, para criação de pesquisa e para responder uma pesquisa.)

[comment]: <> (Também é possível obter os tokens com os comandos curl abaixo:)

[comment]: <> (#### Admin token)

[comment]: <> (> curl --location --request POST 'http://localhost:8050/auth/realms/research/protocol/openid-connect/token' \)

[comment]: <> (> --header 'Authorization: Basic b3BlbmFwaTo4Y2RhMjJjYi0yN2EwLTRhZmItYTU5NC00ZGJiMGU5YWRmNmY=' \)

[comment]: <> (> --header 'Content-Type: application/x-www-form-urlencoded' \)

[comment]: <> (> --data-urlencode 'grant_type=password' \)

[comment]: <> (> --data-urlencode 'username=adm' \)

[comment]: <> (> --data-urlencode 'password=123456' \)

[comment]: <> (> --data-urlencode 'client_id=openapi')

[comment]: <> (Este usuário contém a authority "ADMIN" e pode realizar todas as operações: cadastrar e buscar pesquisas, cadastrar respostas e ver o resumo da pesquisa.)

[comment]: <> (#### User token)

[comment]: <> (> curl --location --request POST 'http://localhost:8050/auth/realms/research/protocol/openid-connect/token' \)

[comment]: <> (> --header 'Authorization: Basic b3BlbmFwaTowMWExMzg2NC0wZDE3LTQ0MWEtODcyMS1hMjIyYmNmMTc4NDI=' \)

[comment]: <> (> --header 'Content-Type: application/x-www-form-urlencoded' \)

[comment]: <> (> --data-urlencode 'grant_type=password' \)

[comment]: <> (> --data-urlencode 'username=user' \)

[comment]: <> (> --data-urlencode 'password=123456' \)

[comment]: <> (> --data-urlencode 'client_id=openapi')

[comment]: <> (Este usuário não contém nenhuma authority e pode buscar pesquisas, cadastrar respostas e ver o resumo de respostas da pesquisa.)

## Observabilidade

### Logs

Os logs são gerenciados pelo [SLF4J](http://www.slf4j.org/), e utiliza o [Logback](http://logback.qos.ch/) como implementação.

[comment]: <> (### Métricas)

[comment]: <> (A aplicação utiliza do [Spring Actuator]&#40;https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/&#41; para expor dados sobre sua execução.)

[comment]: <> (### Dashboard)

[comment]: <> (O docker-compose existente no projeto inclui o [Prometheus]&#40;https://prometheus.io/&#41; e o [Grafana]&#40;https://grafana.com/&#41;.)

[comment]: <> (O Grafana é exposto na porta 3000 com usuário __admin__ e senha __123456__. Ao logar, será exibido um dashboard preconfigurado que consome os dados fornecidos pelo Actuator ao Prometheus.)