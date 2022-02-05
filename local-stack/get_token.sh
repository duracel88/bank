curl -X POST 'http://bank.lukgaw.com/auth/realms/bank/protocol/openid-connect/token' \
	-H 'Content-Type: application/x-www-form-urlencoded'  \
	--data-urlencode 'client_id=accounts-api' \
	--data-urlencode 'grant_type=password' \
	--data-urlencode 'client_secret=cede2a1f-3643-42ca-b6b2-bd26d11ed11a' \
	--data-urlencode 'scope=openid' \
	--data-urlencode "username=$1" \
	--data-urlencode 'password=test' 2>/dev/null | jq -r .access_token
