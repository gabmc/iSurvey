select 
	s.service_id,
	s.path,
	s.description,
	s.app_id
from
	${schema}s_service s,
	${schema}s_application a
where
	s.app_id=a.app_id and
	a.app_alias = '${alias}'