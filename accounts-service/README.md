### Accounts API
## Create Account
- Required role: `CUSTOMER`
- Path `/api/accounts`
- Method `POST`
- Produces `application/json`
- Authorization `JWT` Bearer token
- Request body:
```
{
    "firstName": string, required
    "lastName": string, required
    "firstDepositAmount": decimal (,2)
    "currency": string, pattern: $[A-Z]{3}^
}
```
- Response output
```
    "newAccountId": uuid
    "firstDeposit": boolean
```

## Get Account 
- Required role: `CUSTOMER` and resource ownership
- Required role: `ADMIN`
- Path `/api/accounts/{id}`
- Method `GET`
- Params: `size=integer, optional, default=20`
  - For `CUSTOMER` forbidden
  - for `ADMIN` any positive number
- Produces `application/json`
- Authorization `JWT` Bearer token
- Response output
```
    "accountId": uuid,
    "customerId": uuid,
    "firstName": string,
    "lastName": string,
    "balance": decimal,
    "currency": varchar(3),
    "": [{
        "id":  uuid
        "amount": decimal
        "orderId": integer
    }]
```