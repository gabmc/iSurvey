select
	ajvieira_isurvey_app.lista_participantes.*
from
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_instrumento, ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio
where
        int_lista_participantes_instrumento.id_lista_participantes = lista_participantes.id_lista_participantes
        and int_lista_participantes_instrumento.id_instrumento = instrumento.id_instrumento
        and instrumento.id_estudio = estudio.id_estudio
        and estudio.id_empresa in (select ss.id_empresa  from ajvieira_isurvey_security.s_user as ss where ss.userlogin='${def:user}')
