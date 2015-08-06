select i.*,
(select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento
		and estatus = 'Completa') as completado,

concat(round((select ((select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento
		and estatus = 'Completa')*100)/(select count(int_participante_instrumento.token_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento)))) as porcentaje_completado,

(select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento
		and estatus = 'Incompleta') as incompleto,

concat(round((select ((select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento
		and estatus = 'Incompleta')*100)/(select count(int_participante_instrumento.token_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento)))) as porcentaje_incompleto,

(select count(int_participante_instrumento.id_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento 
		and estatus = 'Sin Iniciar') as sin_iniciar,

concat(round((select ((select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento 
		and estatus = 'Sin Iniciar')*100)/(select count(int_participante_instrumento.token_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento = i.id_instrumento)))) as porcentaje_sin_iniciar
from ajvieira_isurvey_app.instrumento as i 
where id_estudio = ${fld:id_estudio}

order by fecha_registro