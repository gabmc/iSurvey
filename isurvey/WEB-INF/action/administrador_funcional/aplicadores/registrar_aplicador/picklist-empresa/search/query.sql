select
	e.id_empresa as productid,
	e.nombre_empresa as productname
from
	empresa as e
where
	upper (nombre_empresa) like upper (${fld:name})

order by
	nombre_empresa