update ${schema}s_menu set 

	app_id = ${ses:app_id},
	title = ${fld:title},
	position = ${fld:position}
where
	menu_id = ${fld:menu_id}
