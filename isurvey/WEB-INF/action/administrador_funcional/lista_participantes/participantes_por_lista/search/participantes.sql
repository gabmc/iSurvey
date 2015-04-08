select 
ajvieira_isurvey_app.participante.*, ${fld:id_lista_participantes} as "id_lista_participantes"
from
ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes, 
ajvieira_isurvey_app.lista_participantes
where
participante.id_empresa = (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')
and int_participante_lista_participantes.id_participante = participante.id_participante
and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes
and lista_participantes.id_lista_participantes = ${fld:id_lista_participantes}