UPDATE ajvieira_isurvey_app.empresa SET
	id_sector_empresarial=${fld:id_sector_empresarial},
	nombre_empresa=${fld:nombre_empresa},
	telefono=${fld:telefono},
	tipo=${fld:tipo},
	rif=${fld:rif}
WHERE
	id_empresa=${fld:id}