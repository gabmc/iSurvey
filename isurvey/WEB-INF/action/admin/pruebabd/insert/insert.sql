INSERT INTO ajvieira_isurvey_app.sector_empresarial
(
	id_sector_empresarial,
	nombre_sector
)
VALUES
(
	${seq:nextval@ajvieira_isurvey_app.seq_sector_empresarial},
	${fld:nombre_sector}
)
