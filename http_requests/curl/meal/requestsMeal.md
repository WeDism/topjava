- GET all meals
```shell
curl -v -H "Accept: application/json" http://localhost:8080/topjava/rest/meals
```

- GET by id
```shell
curl -v -H "Accept: application/json" http://localhost:8080/topjava/rest/meals/100004
```

- DELETE by id
```shell
curl -v -X "DELETE" -H "Accept: application/json" http://localhost:8080/topjava/rest/meals/100004
```

- POST create by id
```shell
curl -v -H "Content-Type: application/json" http://localhost:8080/topjava/rest/meals ^
-d "{ \"dateTime\": \"2020-01-31T14:00:00\", \"description\": \"Обед2\", \"calories\": 751 }"
```

- PUT update by id
```shell
curl -v -X PUT -H "Content-Type: application/json" http://localhost:8080/topjava/rest/meals/100007 ^
-d "{ \"dateTime\": \"2020-01-31T10:00\", \"description\": \"Завтрак\", \"calories\": 55 }"
```

- GET with a filter
```shell
curl -v -H "Accept: application/json" ^
"http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=10:30&endDate=2020-01-31&endTime=17:30"
```

- GET with a filter nulls values
```shell 
curl -v -H "Accept: application/json" ^
"http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=10:30"
```
