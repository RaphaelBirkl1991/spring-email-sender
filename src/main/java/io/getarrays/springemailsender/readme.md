
1. UserServiceImpl -> uncomment wanted method

2. Delete from Tables if populated: 
DELETE FROM confirmations WHERE TRUE;
DELETE  FROM Users WHERE TRUE;

3. POST-Request; e.g. Postman 
{
   "name": "Raphael Birkl",
   "email": "raphael.birkl@gmail.com",
   "password": "123456",
   "enabled": true
}
4. check Email