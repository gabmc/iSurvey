update ${schema}s_service set 

	path = ${fld:path},
	description = ${fld:description},
	app_id = ${fld:app_id}

where
	service_id = ${fld:service_id}
