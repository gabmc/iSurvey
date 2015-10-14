select 0 as id_empresa, 
		empresa.nombre_empresa as nombre_empresa_pl0, 
		est.*, 
		"--" as instrumento
from ajvieira_isurvey_app.estudio as est,
ajvieira_isurvey_app.empresa
where 0 = 
(select count(*) from ajvieira_isurvey_app.instrumento where instrumento.id_estudio = est.id_estudio)
and est.id_empresa = empresa.id_empresa

UNION

select distinct 
	empresa.id_empresa, 
	empresa.nombre_empresa as nombre_empresa_pl0,
	est.*,
	case (select count(instrumento.id_instrumento) 
			from ajvieira_isurvey_app.instrumento 
			where id_estudio = est.id_estudio)
	when 1 and est.tipo = "Abierto-Identificado" then
		concat("<a target='_blank' title='Enlace a Estudio' 
href='http://www.compensa.com.ve/isurvey/action/estudio/abierto/identificado/form?id=",inst.id_instrumento,
"'> <img src='${def:context}/images/clipboard.png' alt='estudio' width='18' height='18'></a>")
	when 1 and est.tipo = "Abierto-Anonimo" then
		concat("<a target='_blank' title='Enlace a Estudio' 
href='http://www.compensa.com.ve/isurvey/action/estudio/abierto/anonimo/form?id=",inst.id_instrumento,
"'> <img src='${def:context}/images/clipboard.png' alt='estudio' width='18' height='18'></a>")
	else
		"--"
	end as instrumento
from
	ajvieira_isurvey_app.empresa,
	ajvieira_isurvey_app.estudio as est,
	ajvieira_isurvey_app.instrumento as inst
where
	empresa.id_empresa=est.id_empresa
	and inst.id_estudio = est.id_estudio 

order by 
	id_estudio desc