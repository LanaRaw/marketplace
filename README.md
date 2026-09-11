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
* Детальная страница товара
* Главная страница с последними товарами
* Корзина (добавление, удаление, изменение количества)
* Сохранение корзины между сессиями
* Оформление заказа
* История заказов

## Запуск

### 1. Создать базу данных

Создайте базу данных `marketplace` в PostgreSQL.

### 2. Настроить подключение

В файле `application.yml` укажите свои данные:
```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/marketplace
    username: postgres
    password: ваш_пароль
```
### 3. Запустить приложение
Запустите класс:
```
MarketplaceApplication.java
```

### 4. Открыть приложение
Перейдите в браузере:
```
http://localhost:8080
```

## Тестовые аккаунты

| Роль   | Email                    | Пароль      |
| ------ | ------------------------ | ----------- |
| Admin  | `admin@marketplace.com`  | `admin123`  |
| Seller | `seller@marketplace.com` | `seller123` |
| Buyer  | `buyer@marketplace.com`  | `buyer123`  |

## Структура проекта
```
src/
├── main/
│   ├── java/com/marketplace/
│   │   ├── config/         # Конфигурации (Security, DataLoader)
│   │   ├── controller/     # Контроллеры
│   │   ├── dto/            # DTO-классы
│   │   ├── model/          # Сущности (User, Product, Order, Cart...)
│   │   ├── repository/     # Репозитории JPA
│   │   └── service/        # Сервисы
│   └── resources/
│       ├── static/         # CSS, JS, картинки
│       └── templates/      # HTML-шаблоны (Thymeleaf)
└── pom.xml
```

## Статус проекта

Спринт 0: Настройка проекта — ✅

Спринт 1: Модели данных — ✅

Спринт 2: Безопасность — ✅

Спринт 3: Витрина — ✅

Спринт 3.5: Рефакторинг — ✅

Спринт 4: Корзина — ✅

Спринт 5: Оформление заказа — ✅

Спринт 5.5: Регистрация с выбором роли — ⏳ в разработке
