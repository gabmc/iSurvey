select	
	participante.id_participante,
	participante.id_empresa,
	estudio.id_estudio, 
	estudio.nombre_estudio as nombre_estudio_pl0,
	instrumento.*,
	estatus
from
	ajvieira_isurvey_app.participante,
	ajvieira_isurvey_app.estudio,
	ajvieira_isurvey_app.instrumento,
	ajvieira_isurvey_app.int_participante_instrumento 
where
	ajvieira_isurvey_app.estudio.id_estudio=ajvieira_isurvey_app.instrumento.id_estudio
and int_participante_instrumento.id_participante = ${fld:id_participante}
and int_participante_instrumento.id_instrumento = instrumento.id_instrumento
and participante.id_participante = ${fld:id_participante}
and participante.id_empresa = ${fld:id_empresa} 