# Centric Software Product API 
[![CI](https://github.com/zt1983811/centric-software-product-api/workflows/CI/badge.svg)](https://github.com/zt1983811/centric-software-product-api/actions)

Quiz centric-software-product-api

### System Requirements:
* Java 11
* Maven > 3

### Quick Run Command
1. Execute following line
```
 mvn spring-boot:run
```
2. Visit API doc 
http://localhost:8080/swagger-ui.html

3. Try to add product with following payload
```$xslt
{
    "name": "Red Shirt",
    "description": "Red hugo boss shirt",
    "brand": "Hugo Boss",
    "tags": [
        "red",
        "shirt",
        "slim fit"
    ],
    “category”: “apparel”
}
```
4. Check if data in database by 
http://localhost:8080/h2-console/login.jsp
