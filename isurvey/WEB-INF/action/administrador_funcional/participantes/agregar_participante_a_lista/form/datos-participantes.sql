select 
        *
from
        ajvieira_isurvey_app.participante
where
		participante.id_empresa in (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')