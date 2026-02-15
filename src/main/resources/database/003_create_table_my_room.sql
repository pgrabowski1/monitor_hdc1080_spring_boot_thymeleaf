CREATE TABLE chata_database.my_room
(
    id          bigserial NOT NULL,
    humidity    float4    NOT NULL,
    temperature float4    NOT NULL,
    "time"      timestamp NOT NULL,
    CONSTRAINT my_room_pkey PRIMARY KEY (id)
);