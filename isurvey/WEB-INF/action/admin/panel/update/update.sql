UPDATE ${schema}s_panel SET
	service_id=${fld:service_id},
	icon_path=${fld:icon_path},
	orden=${fld:orden},
	app_id=${fld:app_id}
WHERE
	panel_id=${fld:id}

