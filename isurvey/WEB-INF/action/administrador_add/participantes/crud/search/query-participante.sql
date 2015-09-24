select
	id_participante, nombre_participante, apellido_participante, email_participante, telefono, empresa, 
	sector_empresa, area, cargo, supervisor, fecha_nacimiento, fecha_ingreso, sexo, tipo_nomina, funcion, id_empresa
from 
	ajvieira_isurvey_app.participante
where
	participante.id_empresa = ${fld:id_empresa}
order by 
	id_participante
