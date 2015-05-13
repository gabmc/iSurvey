select x.*
from ajvieira_isurvey_app.estudio as x
where 0 = (select count(*) from ajvieira_isurvey_app.estudio as m, ajvieira_isurvey_app.instrumento as n
			where m.tipo = 'Abierto-Identificado'
			and m.id_estudio = n.id_estudio
			and x.id_estudio = m.id_estudio)
	${filter}