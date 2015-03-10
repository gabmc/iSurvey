insert into ${schema}s_menu  
(
	app_id,
	title,
	position
)
values 
(
	@app_id,
	${fld:title},
	${fld:position}
);

