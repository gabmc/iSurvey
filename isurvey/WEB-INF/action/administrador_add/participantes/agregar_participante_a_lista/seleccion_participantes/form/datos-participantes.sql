select 
        id_participante, nombre_participante, apellido_participante, email_participante, empresa, cargo, supervisor,
        fecha_nacimiento, fecha_ingreso, sexo, tipo_nomina, funcion, id_empresa, verificado
from
        ajvieira_isurvey_app.participante
where
		participante.id_empresa = ${fld:id_empresa}