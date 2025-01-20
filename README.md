# InternetBanking

InternetBanking — это система интернет-банкинга, предназначенная для удобного и безопасного управления банковскими счетами, картами и транзакциями. В рамках данного проекта пользователи могут выполнять операции перевода средств между счетами, пополнение счетов, а также отслеживать баланс и историю транзакций. Администраторы получают доступ к управлению пользователями и настройке системы.

## Возможности

- Авторизация и регистрация пользователей (клиентов и администраторов)
- Просмотр и управление картами и счетами (оформление, удаление, произведение операций пополнения и перевода средств)
- Разграничение доступа для пользователей и администраторов

## Требования

- Java 17 
- Spring Boot 3.3.5 
- PostgreSQL
- Flyway (для миграций) 
- Thymeleaf

## Установка и запуск

1. Склонируйте репозиторий:

    ```sh
    git clone https://github.com/dmitriyvechorko/InternetBanking.git
    cd InternetBanking
    ```

2. Настройте базу данных PostgreSQL:

    - Создайте базу данных `internetbank`.
    - Обновите `application.properties` с вашими настройками подключения к базе данных.

   **Пример `application.properties`:**

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/internetbank
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   # Настройка Flyway 
   spring.flyway.enabled=true 
   spring.flyway.locations=classpath:db/migration
    ```

3. Установите зависимости и запустите приложение:

    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

4. Приложение будет доступно по адресу `http://localhost:8080`.

## Структура проекта

- **controller**: содержит контроллеры для обработки запросов.
- **entity**: содержит сущности для базы данных.
- **repository**: содержит репозитории для взаимодействия с БД.
- **service**: содержит бизнес-логику приложения.
- **exception**: содержит исключения для избавленяи от ошибок.
- **security**: содержит логику безопасности приложения.
- **templates**: содержит шаблоны Thymeleaf для фронтенда.
- **db/migration**: содержит файлы миграций Flyway.

# Примеры использования

## Авторизация и регистрация

- Перейдите на страницу http://localhost:8080/login для авторизации.

- Перейдите на страницу http://localhost:8080/register для регистрации нового пользователя.

## Управление банкингом

- Пользователи могут просматривать свои карты и счета на соответствующих страницах: http://localhost:8080/cards и http://localhost:8080/accounts.



**Этот проект является частью учебного задания и не предназначен для коммерческого использования.**
