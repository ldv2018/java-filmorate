# java-filmorate
## Description
Service for working with movies and user ratings

## API Endpoints
| HTTP Verbs | Endpoints | Action |
| --- | --- | --- |
| POST | /films | To add new film |
| GET | /films | To retrieve all films in app memory |
| PUT | /films | To update film |
| POST | /users | To add new user |
| GET | /users | To retrieve all users in app memory |
| PUT | /users | To update user |

## Models

### Film
```
int         id
String      name
String      description
LocalDate   releaseDate
int         duration
```

### User
```
int         id
String      email
String      login
String      name
LocalDate   birthday
```