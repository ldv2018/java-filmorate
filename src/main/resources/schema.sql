DROP TABLE IF EXISTS
    "film",
    "film_like",
    "genre",
    "rating",
    "user",
    "film_to_genre",
    "friend" CASCADE;


CREATE TABLE "film" (
                        "film_id" int   NOT NULL auto_increment,
                        "name" text   NOT NULL,
                        "description" varchar(200)   NOT NULL,
                        "release_date" date   NOT NULL,
                        "duration" int   NOT NULL,
                        "rating_id" int   NOT NULL,
                        CONSTRAINT "pk_film" PRIMARY KEY (
                                                          "film_id"
                            )
);

CREATE TABLE "genre" (
                         "genre_id" int   NOT NULL auto_increment,
                         "name" text   NOT NULL,
                         CONSTRAINT "pk_genre" PRIMARY KEY (
                                                            "genre_id"
                             )
);

CREATE TABLE "rating" (
                          "rating_id" int   NOT NULL auto_increment,
                          "name" varchar(5)   NOT NULL,
                          CONSTRAINT "pk_rating" PRIMARY KEY (
                                                              "rating_id"
                              )
);

CREATE TABLE "film_to_genre" (
                                 "film_id" int   NOT NULL,
                                 "genre_id" int   NOT NULL
);

CREATE TABLE "film_like" (
                             "film_id" int   NOT NULL,
                             "user_id" int   NOT NULL
);

CREATE TABLE "user" (
                        "user_id" int   NOT NULL auto_increment,
                        "email" text   NOT NULL,
                        "login" text   NOT NULL,
                        "name" text   NOT NULL,
                        "birthday" date   NOT NULL,
                        CONSTRAINT "pk_user" PRIMARY KEY (
                                                          "user_id"
                            )
);

CREATE TABLE "friend" (
                          "user_id" int   NOT NULL,
                          "friend_id" int   NOT NULL,
                          "friend_status_id" boolean   NOT NULL
);

ALTER TABLE "film" ADD CONSTRAINT "fk_film_rating_id" FOREIGN KEY("rating_id")
    REFERENCES "rating" ("rating_id");

ALTER TABLE "film_to_genre" ADD CONSTRAINT "fk_film_to_genre_film_id" FOREIGN KEY("film_id")
    REFERENCES "film" ("film_id");

ALTER TABLE "film_to_genre" ADD CONSTRAINT "fk_film_to_genre_genre_id" FOREIGN KEY("genre_id")
    REFERENCES "genre" ("genre_id");

ALTER TABLE "film_like" ADD CONSTRAINT "fk_film_like_film_id" FOREIGN KEY("film_id")
    REFERENCES "film" ("film_id");

ALTER TABLE "film_like" ADD CONSTRAINT "fk_film_like_user_id" FOREIGN KEY("user_id")
    REFERENCES "user" ("user_id");

ALTER TABLE "friend" ADD CONSTRAINT "fk_friend_user_id" FOREIGN KEY("user_id")
    REFERENCES "user" ("user_id");

ALTER TABLE "friend" ADD CONSTRAINT "fk_friend_friend_id" FOREIGN KEY("friend_id")
    REFERENCES "user" ("user_id");

INSERT INTO "rating" ("name")
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO "genre" ("name")
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

