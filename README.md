# Marketplace

Веб-приложение маркетплейса на Spring Boot.

## Технологии

* Java 17
* Spring Boot 4.1.1
* Spring Security
* PostgreSQL
* Thymeleaf
* Bootstrap 5

## Возможности

* Регистрация и авторизация пользователей
* Разграничение ролей: `ADMIN`, `SELLER`, `BUYER`
* Просмотр товаров с пагинацией
* Поиск товаров
* Фильтрация по категориям
* Корзина — в разработке

## Запуск

### 1. Создать базу данных

Создайте базу данных `marketplace` в PostgreSQL.

### 2. Запустить приложение

Запустите класс:

```text
MarketplaceApplication.java
```

### 3. Открыть приложение

Перейдите в браузере:

```text
http://localhost:8080
```

## Тестовые аккаунты

| Роль   | Email                    | Пароль      |
| ------ | ------------------------ | ----------- |
| Admin  | `admin@marketplace.com`  | `admin123`  |
| Seller | `seller@marketplace.com` | `seller123` |
| Buyer  | `buyer@marketplace.com`  | `buyer123`  |

## Статус проекта

В разработке.
