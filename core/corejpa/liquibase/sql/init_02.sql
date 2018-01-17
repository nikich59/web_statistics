CREATE TABLE constants
(
  web_data_type_json_string                VARCHAR(2048),
  web_data_type_xml_string                 VARCHAR(2048),

  statistics_value_data_type_int_string    VARCHAR(2048),
  statistics_value_data_type_string_string VARCHAR(2048),

  headline_part_mode_sleuth_string         VARCHAR(2048),
  headline_part_mode_statister_string      VARCHAR(2048),
  headline_part_mode_raw_string            VARCHAR(2048)

);

INSERT INTO constants (web_data_type_json_string, web_data_type_xml_string)
VALUES ('json', 'xml');

INSERT INTO constants (statistics_value_data_type_int_string, statistics_value_data_type_string_string)
VALUES ('int', 'string');

INSERT INTO constants (headline_part_mode_sleuth_string, headline_part_mode_statister_string, headline_part_mode_raw_string)
VALUES ('sleuth', 'statister', 'raw');


INSERT INTO dict_web_data_types (name)
  SELECT web_data_type_json_string
  FROM constants
  WHERE web_data_type_json_string IS NOT NULL;

INSERT INTO dict_web_data_types (name)
  SELECT web_data_type_xml_string
  FROM constants
  WHERE web_data_type_xml_string IS NOT NULL;


INSERT INTO dict_statistics_value_data_types (name)
  SELECT statistics_value_data_type_int_string
  FROM constants
  WHERE statistics_value_data_type_int_string IS NOT NULL;

INSERT INTO dict_statistics_value_data_types (name)
  SELECT statistics_value_data_type_string_string
  FROM constants
  WHERE statistics_value_data_type_string_string IS NOT NULL;


INSERT INTO dict_headline_part_modes (name)
  SELECT headline_part_mode_sleuth_string
  FROM constants
  WHERE headline_part_mode_sleuth_string IS NOT NULL;

INSERT INTO dict_headline_part_modes (name)
  SELECT headline_part_mode_statister_string
  FROM constants
  WHERE headline_part_mode_statister_string IS NOT NULL;

INSERT INTO dict_headline_part_modes (name)
  SELECT headline_part_mode_raw_string
  FROM constants
  WHERE headline_part_mode_raw_string IS NOT NULL;



