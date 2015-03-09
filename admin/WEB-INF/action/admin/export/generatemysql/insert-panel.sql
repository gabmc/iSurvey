INSERT INTO ${schema}s_panel (orden, icon_path, service_id, app_id)

select ${fld:orden}, ${fld:icon_path}, s.service_id, @app_id
from 
${schema}s_service s 
where s.path = ${fld:path} and s.app_id = @app_id;
