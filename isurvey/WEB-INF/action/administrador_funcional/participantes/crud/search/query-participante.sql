select
	id_participante, nombre_participante, apellido_participante, email_participante, empresa, cargo,
	supervisor, fecha_nacimiento, fecha_ingreso, sexo, tipo_nomina, funcion
from 
	ajvieira_isurvey_app.participante
where
	participante.id_empresa in (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')
order by 
	id_participante
