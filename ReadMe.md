
## Структура
* discovery-service - Сервис Eureka, где "регистрируются" все запущенные микросервисы
* config-service - Сервис откуда все микросервисы подтягивают настройки
* authentication-service - Сервис аунтификации пользователя (передает данные о пользователе в user-service для регистрации/записи в бд), создает accessToken и refreshToken для пользователя
* user-service - Сервис для сохранения пользователей в бд; логин пользователя (сопоставление хэша пароля с захэшированным паролем в бд)
* gateway-service - Api gateway, через который проходят все запросы. При этом проверяется accessToken на валидность и, чтобы в дальнейшем каждому из микросервисов не нужно было парсить токен заново, добавляет необходимые поля (name и role) к запросу


## Аутентификация пользователя (т.е. для доступа к сервису необходимо сперва залогиниться)

Для регистрации:
```http request
POST http://localhost:8765/auth/register
```

Для логина:
```http request
POST http://localhost:8765/auth/login
```

Тело запроса в обоих случаях:<br/>

``` json
{
    "password": "password",
    "name": "name"
}
```

В ответ пользователь получает <br/>

``` json
{
    "accessToken": "данные1",
    "refreshToken": "данные2"
}
```

В дальнейшем пользователю нужно в Headers POST запроса отправлять свой access token <br/>

```http request
POST http://localhost:8765/*
Headers: 
"key":"Authorization"
"value":"данные1"
```

При отсутствии Headers "key":"Authorization", "value":"данные1"
будет возвращаться ошибка 401 






