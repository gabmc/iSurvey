select
	s.*,
	a.description as app_description
from
	${schema}s_service s,
	${schema}s_application a
where
	s.service_id not in (select sr.service_id from ${schema}s_service_role sr) and
	s.app_id =  a.app_id