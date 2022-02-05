## Local docker-compose stack
### Prerequisites
- docker engine installed (https://docs.docker.com/engine/install/)
- docker-compose tool installed (https://docs.docker.com/compose/install/)
  
### Setup local stack
- navigate to `${GIT_REPO}/local-stack`
- run `docker-compose build`
- run `docker-compose up -d`
- verify `docker-compose ps` 
- keycloak-init's state should be `Exit 0` after a while

### Components
- `keycloak` - opensource IDM provider, in-memory database
  - Default admin credentials: `admin`/`admin` (user/passwd)
- `keycloak-init` - keycloak component initializer with default test users/roles/etc
  - Users:
    - `admin`, password: `admin`
    - `luk`, password: `test`, role: `ADMIN`
    - `chuck.norris`, password: `test`, role: `CUSTOMER`
    - `barbra.streisand`, password: `test`, role: `CUSTOMER`
  - Realm roles:
    - `ACCOUNTS_ADMIN`, maps to application's `ADMIN` role
    - `ACCOUNTS_CUSTOMER`, maps to application's `CUSTOMER` role
  - Client:
    - `accounts-web-app`, dedicated to Angular SPA web client (**TODO**)
      - `client_id`: `accounts-web-app`
      - `client_secret`: `cede2a1f-3643-42ca-b6b2-bd26d11ed11a`
    - `accounts-api`, dedicated to API clients, like curl, Postman
      - `client_id`: `accounts-api`
      - `client_secret`: `cede2a1f-3643-42ca-b6b2-bd26d11ed11a`
- `accounts-service` - SpringBoot application. Authorization by JWT token 
- `tx-service` - SpringBoot application. No auth (**TODO**)
- `gateway` - Traefik reverse proxy. It routes all http requests to containers across stack`s internal network.
  - `/api/*` - routes to `accounts-service`
  - `/auth/*` - routes to `keycloak`

### Use case scenarios
#### Chuck Norris creates an account. He and the Admin (Luk) can access newly created resource, but Barbra Streisand can not.

- Get Chuck's auth token `./get_token.sh chuck.norris` (**$1** variable)
- Create new account (replace **$1** placeholder)
```
curl http://localhost/api/accounts -v -X POST \
  -H "Authorization: Bearer $1" \
  -H "Content-type: application/json" \
  -d '{"firstName":"Chuck", "lastName":"Norris", "currency":"PLN", "firstDepositAmount": 15.13}'
```
Sample output: `{"newAccountId":"af70decd-6e13-437f-908d-858d0f95ad2f","firstDeposit":true}`. Keep newAccountId (**$2**)
- Try to access the resource
```
curl http://localhost/api/accounts/$2 -v -X GET \
  -H "Authorization: Bearer $1" 
```
Sample output: 
```
{
  "accountId":"af70decd-6e13-437f-908d-858d0f95ad2f",
  "customerId":"06a62fb2-ea25-4a2f-b5f0-70e8e61cb8a9",
  "firstName":"Chuck",
  "lastName":"Norris",
  "balance":15.13,
  "currency":"PLN",
  "transactions":[{
    "id":"40d5d8c3-3aff-45a9-8d5c-aafe0f241d2e", 
    "amount":15.13,
    "orderId":1
    }]
}
```
- Get Barbra's token `./get_token.sh barbra.streisand` (**$3** variable)
- Try to access previously created resource
```
curl http://localhost/api/accounts/$2 -v -X GET \
  -H "Authorization: Bearer $3" 
```
Expected response code: 403 Forbidden
- Get Luk's (Admin) token `./get_token.sh luk` (**$4** variable)
- Access previously created resource
```
curl http://localhost/api/accounts/$2 -v -X GET \
  -H "Authorization: Bearer $4" 
```
Expected result is the same as Chuck's

#### Customer can create the account without initial deposit

- Get Chuck's auth token `./get_token.sh chuck.norris` (**$1** variable)
- Create new account (replace **$1** placeholder)
```
curl http://localhost/api/accounts -v -X POST \
  -H "Authorization: Bearer $1" \
  -H "Content-type: application/json" \
  -d '{"firstName":"Chuck", "lastName":"Norris", "currency":"PLN"}'
```
Sample output: `{"newAccountId":"af70decd-6e13-437f-908d-858d0f95ad2f","firstDeposit":false}`. Keep newAccountId (**$2**)
- Try to access the resource
```
curl http://localhost/api/accounts/$2 -v -X GET \
  -H "Authorization: Bearer $1" 
```
Sample output:
```
{
  "accountId":"af70decd-6e13-437f-908d-858d0f95ad2f",
  "customerId":"06a62fb2-ea25-4a2f-b5f0-70e8e61cb8a9",
  "firstName":"Chuck",
  "lastName":"Norris",
  "balance":15.13,
  "currency":"PLN",
  "transactions":[]
}
```

### The tx-service is down, the customer tries to create the account with initial deposit. The account is being created, but no initial deposit is made

- Put tx-service down: `docker-compose stop tx-service`
- Get Chuck's auth token `./get_token.sh chuck.norris` (**$1** variable)
- Create new account (replace **$1** placeholder)
```
curl http://localhost/api/accounts -v -X POST \
  -H "Authorization: Bearer $1" \
  -H "Content-type: application/json" \
  -d '{"firstName":"Chuck", "lastName":"Norris", "currency":"PLN", "firstDepositAmount": 15.15}'
```
Sample output: `{"newAccountId":"af70decd-6e13-437f-908d-858d0f95ad2f","firstDeposit":false}`.

#### The Admin cannot create the account

- Get Luks's auth token `./get_token.sh luk` (**$1** variable)
- Create new account (replace **$1** placeholder)
```
curl http://localhost/api/accounts -v -X POST \
  -H "Authorization: Bearer $1" \
  -H "Content-type: application/json" \
  -d '{"firstName":"Luk", "lastName":"Gaw", "currency":"PLN"}'
```
- Expected result: 403 Forbidden
