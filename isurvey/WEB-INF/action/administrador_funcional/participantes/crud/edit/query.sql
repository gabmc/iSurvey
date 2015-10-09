select	
	nombre_participante, apellido_participante, id_participante, email_participante, sexo, area, cargo, telefono,
	supervisor, fecha_nacimiento, fecha_ingreso, tipo_nomina, funcion
from 
	ajvieira_isurvey_app.participante
where 
	id_participante = ${fld:id}
	and id_empresa = (select id_empresa from ajvieira_isurvey_security.s_user where userlogin='${def:user}')


