# Notification Scheduler

## Sobre o projeto

O projeto tem como propósito disponibilizar uma API através da qual seja possível enviar notificações em uma data/hora definida pelo cliente. A notificação pode ser entregue através de diferentes canais de comunicação: e-mail, sms, push e Whatsapp.  
O canal de comunicação também é definido pelo cliente ao enviar a requisição.

## Arquitetura

O projeto foi construído baseado no conceito de  [Arquitetura Limpa](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

## Qualidade
[![Automated Testing](https://github.com/paulosalonso/notification-scheduler/actions/workflows/automated-testing.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/automated-testing.yml)
[![Mutation Testing](https://github.com/paulosalonso/notification-scheduler/actions/workflows/mutation-testing.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/mutation-testing.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paulosalonso_notification-scheduler&metric=alert_status)](https://sonarcloud.io/dashboard?id=paulosalonso_notification-scheduler)

A qualidade da aplicação é garantida através dos testes unitários e integrados. Utiliza o [JaCoCo](https://www.jacoco.org/) para validação de cobertura mínima e o [PIT Mutation](https://pitest.org/) para testar mutações de código.
Também é realizada uma análise estática com o [SonarCloud](https://sonarcloud.io/dashboard?id=paulosalonso_research).

## Banco de dados

A aplicação utiliza o bando de dados MySQL, inclusive nos testes integrados utilizando [Test Containers](https://www.testcontainers.org/).

### Configuração de conexão

Para configurar a URL de conexão crie uma variável de ambiente como abaixo com a string de conexão adequada:

> SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/notification

## API

O root path da API é __/__

### Documentação

A API é documentada utilizando [OpenAPI](https://swagger.io/specification/) e pode ser acessada via navegador através do path __/swagger-ui/index.html__

### Segurança

Por padrão a API é totalmente aberta. Existem dois profiles que ativam segurança via JWT: "secure-api" e "secure-api-jwk".

#### JWT + Secret Key

Ativando o profile "secure-api" a API passa a ser protegida por autenticação via JWT com assinatura simétrica via chave secreta.  
Para ativar o profile é necessária a variável de ambiente abaixo:

> SPRING_PROFILES_ACTIVE=secure-api

Para verificar a validade do token é necessário informar a chave secreta utilizada para assinar o JWT. Ela deve ser configurada através da variável de ambiente abaixo:

> SECURITY_JWT_SIGNATURE_SECRET=\<my-secret\>

O algoritmo utilizado por padrão é o HmacSHA256 e pode ser customizado através da variável de ambiente abaixo:

> SECURITY_JWT_SIGNATURE_ALGORITHM=\<signature-algorithm\>

#### JWT + JWK

Ativando o profile "secure-api-jwk" a api passa a ser protegida por autenticação via JWT com assinatura assimétrica (par de chaves pública/privada).
Para ativar o profile é necessária a variável de ambiente abaixo:

> SPRING_PROFILES_ACTIVE=secure-api-jwk

Para verificar a validade do token é necessário informar a URI para obtenção das chaves públicas do authorization server (JWK). Ela deve ser configurada através da variável de ambiente abaixo:

> SECURITY_JWT_SIGNATURE_JWK-SET-URI=\<jwk-set-uri\>

## Execução

Veremos algumas formas para executar a aplicação. Para todas elas é importante observar as configurações via variável de ambiente.

### IDE

Para executar a aplicação na IDE basta importar o projeto e executar a classe com.github.paulosalonso.notification.application.NotificationSchedulerApplication como uma aplicação Java.

### Maven

> mvn spring-boot:run

### java -jar
> mvn clean package \
> java -jar target/research.jar

### Docker

Para rodar um container Docker da aplicação a partir da última versão disponível da [imagem no Docker Hub](https://hub.docker.com/repository/docker/paulosalonso/notification-scheduler), acesse o diretório __.docker__ e rode o comando abaixo:

> docker-compose up -d

#### Segurança

O docker-compose padrão não ativa a segurança da API. Para subir a API protegida, use o comando abaixo:

> docker-compose -f docker-compose-secure-api.yml up -d

##### Authorization Server

Para fazer a autenticação é utilizado o [Keycloak](https://www.keycloak.org/). Um container será executado e já é iniciado com as seguintes configurações:

* realm: notification-scheduler
* client: openapi
* client secret: 8cda22cb-27a0-4afb-a594-4dbb0e9adf6f
* usuário: adm
* senha: 123456

É possível obter um token com o comando curl abaixo:

> curl --location --request POST 'http://localhost:8050/auth/realms/notification-scheduler/protocol/openid-connect/token' \\ \
> --header 'Authorization: Basic b3BlbmFwaTo4Y2RhMjJjYi0yN2EwLTRhZmItYTU5NC00ZGJiMGU5YWRmNmY=' \\ \
> --header 'Content-Type: application/x-www-form-urlencoded' \\ \
> --data-urlencode 'grant_type=password' \\ \
> --data-urlencode 'username=adm' \\ \
> --data-urlencode 'password=123456' \\ \
> --data-urlencode 'client_id=openapi'

## Consumo

### Postman

Importe a coleção e os ambientes do postman presentes no projeto para consumir a API.  
Há requisições com e sem autenticação. Também há a requisição para obtenção do token no Keycloak local (Docker).  
O ambiente local é configurado de acordo com as configurações do docker-compose.

[Collection](https://github.com/paulosalonso/notification-scheduler/blob/main/.postman/Notification%20Scheduler.postman_collection.json)  
[Local Environment](https://github.com/paulosalonso/notification-scheduler/blob/main/.postman/Notification%20Scheduler%20-%20Local.postman_environment.json)  
[Heroku Environment](https://github.com/paulosalonso/notification-scheduler/blob/main/.postman/Notification%20Scheduler%20-%20Heroku.postman_environment.json)

### Swagger

Também é possível consumir a API via Swagger.

Swagger local (baseado no docker-compose): http://localhost:8080/swager-ui/index.html  
[Swagger no Heroku - Homologação](https://notification-scheduler-hom.herokuapp.com/swager-ui/index.html)  
[Swagger no Heroku - Produção](https://notification-scheduler-prod.herokuapp.com/swager-ui/index.html)

## Observabilidade

### Logs

Os logs são gerenciados pelo [SLF4J](http://www.slf4j.org/), e utiliza o [Logback](http://logback.qos.ch/) como implementação.

Localmente os logs podem ser visualizados pelo terminal, com o comando abaixo:

> docker-compose logs notification-scheduler

### Métricas

A aplicação utiliza do [Spring Actuator](https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/) para expor dados sobre sua execução.

### Dashboard

O docker-compose existente no projeto inclui o [Prometheus](https://prometheus.io/) e o [Grafana](https://grafana.com/).  
O Prometheus consome os dados fornecidos pela aplicação (actuator), e o Grafana consome esses mesmos dados do Prometheus.  
O Grafana é exposto na porta 3000 com usuário __admin__ e senha __123456__. Ao logar, será exibido um dashboard com as métricas da aplicação.

## Integração Contínua

[![Automated Testing](https://github.com/paulosalonso/notification-scheduler/actions/workflows/automated-testing.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/automated-testing.yml)
[![Mutation Testing](https://github.com/paulosalonso/notification-scheduler/actions/workflows/mutation-testing.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/mutation-testing.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paulosalonso_notification-scheduler&metric=alert_status)](https://sonarcloud.io/dashboard?id=paulosalonso_notification-scheduler)

A cada entrega de código (push) os testes são executados e a verificação estática de código é realizada.  
O novo código só é incorporado (merge) a branch main se tudo for realizado com sucesso.

## Entrega Contínua

![Docker Hub CI](https://github.com/paulosalonso/research/workflows/Docker%20Hub%20CI/badge.svg)

A cada release é criada uma imagem Docker da versão no [Docker Hub](https://hub.docker.com/repository/docker/paulosalonso/notification-scheduler).

## Deploy Contínuo

[![Heroku Homologation Environment Deploy](https://github.com/paulosalonso/notification-scheduler/actions/workflows/heroku-hom.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/heroku-hom.yml)
[![Heroku Production Environment Deploy](https://github.com/paulosalonso/notification-scheduler/actions/workflows/heroku-prod.yml/badge.svg)](https://github.com/paulosalonso/notification-scheduler/actions/workflows/heroku-prod.yml)

Para realizar um deploy no ambiente de homologação do Heroku basta criar uma pre-release ou uma branch no padrão "release-candidate-[0-9]+.[0-9]+.[0-9]+" (release-candidate-0.0.1, por exemplo).  
O deploy no ambiente de produção do Heroku é realizado ao criar uma release.

URL de homologação: https://notification-scheduler-hom.herokuapp.com
URL de produção: https://notification-scheduler-prod.herokuapp.com