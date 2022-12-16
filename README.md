# java-filmorate
## Description
Service for working with movies and user ratings

## API Endpoints
| HTTP Verbs | Endpoints | Action |
| --- | --- | --- |
| POST | /films | To add new film |
| POST | /films/{id}/like/{userId} | To add to film with {id} like by user with {userId} |
| PUT | /films | To update film |
| GET | /films | To retrieve all films in app memory |
| GET | /films/{id} | To retrieve film with {id} |
| GET | popular?count={count} | To retrieve {count} popular films. Optional. By default count = 10 |
| POST | /users | To add new user |
| PUT | /users | To update user |
| PUT | /users/{id}/friends/{friendId} | To mark users with {id} and {friendId} as friends |
| GET | /users | To retrieve all users in app memory |
| GET | /users/{id} | To retrieve iser with {id} |
| GET | /users/{id}/friends | To retrieve all friends of a user with {id} |
| GET | /users/{id}/friends/common/{otherId} | To get common friends of users with {id} and {otherId} |


## ER Diagram

![ER Diagram](https://github.com/ldv2018/java-filmorate/blob/main/QuickDBD-Filmorate%20Diagram.png)