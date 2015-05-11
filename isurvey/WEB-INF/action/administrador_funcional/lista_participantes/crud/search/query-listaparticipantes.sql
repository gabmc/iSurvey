select
	lista_participantes.*
from 
	ajvieira_isurvey_app.lista_participantes
where
	id_empresa in (select id_empresa from ajvieira_isurvey_security.s_user where userlogin='${def:user}')
	and oculta = 'No'
order by 
	id_lista_participantes
