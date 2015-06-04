select
	lote_excel_id,
	userlogin, 
	fecha, 
	hora, 
	nombre_archivo, 
	total_registros
from
	demo.lote_excel
order by lote_excel_id desc