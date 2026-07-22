# JAVAAPIRESTFUL

# Login
``` bash
curl -X POST http://localhost:8080/users/login -H "Content-Type: application/json" -d '{"email":"admin@demo.com","password":"admin123"}'

```
# To Access protected root
``` bash
curl -i http://localhost:8080/users -H "Authorization: Bearer COLE_O_TOKEN_AQUI"

```
# To register new user

``` bash
curl -X POST http://localhost:8080/users/register -H "Content-Type: application/json" -d '{"email":"EMAIL","password":"PASSWORD"}'
```



