CREATE TABLE dict_web_data_types
(
  id   SERIAL        NOT NULL PRIMARY KEY,
  name VARCHAR(2048) NOT NULL UNIQUE
);

CREATE TABLE statistics
(
  id                           SERIAL        NOT NULL PRIMARY KEY,
  url                          VARCHAR(2048) NOT NULL UNIQUE,
  initial_date_time            TIMESTAMP     NOT NULL,
  initial_date_time_offset     INT           NOT NULL,
  headline                     VARCHAR(2048),
  period_in_millis             BIGINT        NOT NULL,
  web_data_type_id             INT           NOT NULL,
  expiration_period_in_minutes BIGINT        NOT NULL,
  is_finished                  BOOLEAN       NOT NULL,

  FOREIGN KEY (web_data_type_id) REFERENCES dict_web_data_types (id)
);

CREATE TABLE dict_statistics_value_data_types
(
  id   SERIAL       NOT NULL PRIMARY KEY,
  name VARCHAR(256) NOT NULL UNIQUE
);

CREATE TABLE statistics_value_descriptions
(
  id                 SERIAL        NOT NULL PRIMARY KEY,
  query              VARCHAR(2048) NOT NULL,
  name               VARCHAR(2048) NOT NULL,
  value_data_type_id INT           NOT NULL,
  statistics_id      INT           NOT NULL,

  FOREIGN KEY (value_data_type_id) REFERENCES dict_statistics_value_data_types (id),
  FOREIGN KEY (statistics_id) REFERENCES statistics (id)
);

CREATE TABLE statistics_expiration_checkers
(
  id                       SERIAL        NOT NULL PRIMARY KEY,
  target_period_in_minutes BIGINT        NOT NULL,
  target_value             VARCHAR(2048) NOT NULL,
  target_value_description VARCHAR(2048) NOT NULL
);

CREATE TABLE statistics_data_points
(
  id                SERIAL    NOT NULL PRIMARY KEY,
  time_stamp        TIMESTAMP NOT NULL,
  time_stamp_offset INT       NOT NULL,
  statistics_id     INT       NOT NULL,

  FOREIGN KEY (statistics_id) REFERENCES statistics (id)
);

CREATE TABLE link_expiration_checker_to_statistics
(
  id                    SERIAL NOT NULL PRIMARY KEY,
  expiration_checker_id INT    NOT NULL,
  statistics_id         INT    NOT NULL,

  FOREIGN KEY (expiration_checker_id) REFERENCES statistics_expiration_checkers (id),
  FOREIGN KEY (statistics_id) REFERENCES statistics (id)
);

CREATE TABLE statistics_values
(
  id                   SERIAL        NOT NULL PRIMARY KEY,
  data_point_id        INT           NOT NULL,
  value                VARCHAR(2048) NOT NULL,
  value_description_id INT           NOT NULL,

  FOREIGN KEY (data_point_id) REFERENCES statistics_data_points (id),
  FOREIGN KEY (value_description_id) REFERENCES statistics_value_descriptions (id)
);

CREATE TABLE sleuth_new_url_acquirers
(
  id           SERIAL        NOT NULL PRIMARY KEY,
  query        VARCHAR(2048) NOT NULL,
  prefix       VARCHAR(2048) NOT NULL,
  postfix      VARCHAR(2048) NOT NULL,
  select_regex VARCHAR(2048) NOT NULL
);

CREATE TABLE sleuths
(
  id                             SERIAL        NOT NULL PRIMARY KEY,
  url                            VARCHAR(2048) NOT NULL,
  new_url_acquirer_id            INT           NOT NULL,
  web_data_type_id               INT           NOT NULL,
  description                    VARCHAR(2048),
  period_in_millis               BIGINT        NOT NULL,
  target_prototype_statistics_id INT           NOT NULL,

  FOREIGN KEY (new_url_acquirer_id) REFERENCES sleuth_new_url_acquirers (id),
  FOREIGN KEY (web_data_type_id) REFERENCES dict_web_data_types (id),
  FOREIGN KEY (target_prototype_statistics_id) REFERENCES statistics (id)
);

CREATE TABLE dict_headline_part_modes
(
  id   SERIAL        NOT NULL PRIMARY KEY,
  name VARCHAR(2048) NOT NULL UNIQUE
);

CREATE TABLE headline_parts
(
  id      SERIAL       NOT NULL PRIMARY KEY,
  mode_id INT          NOT NULL,
  query   VARCHAR(256) NOT NULL,
  prefix  VARCHAR(2048),
  postfix VARCHAR(2048),

  FOREIGN KEY (mode_id) REFERENCES dict_headline_part_modes (id)
);

CREATE TABLE link_headline_part_to_sleuth
(
  id               SERIAL NOT NULL PRIMARY KEY,
  headline_part_id INT    NOT NULL,
  sleuth_id        INT    NOT NULL,

  FOREIGN KEY (headline_part_id) REFERENCES headline_parts (id),
  FOREIGN KEY (sleuth_id) REFERENCES sleuths (id)
);












