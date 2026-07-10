# JAVAAPIRESTFUL

# Login
curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"EMAIL","password":"PASSWORD"}'

# To Access protected root
curl -i http://localhost:8080/users \
  -H "Authorization: Bearer COLE_O_TOKEN_AQUI"

# To register new user
curl -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@teste.com","password":"12345678"}'
