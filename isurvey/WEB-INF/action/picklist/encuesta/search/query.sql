select 
	sid as productid,
	sid as productname, 
	'no tiene d' as categoryname
from 
	ajvieira_isurvey_lime.surveys
where
	ajvieira_isurvey_lime.surveys.sid not in (select ajvieira_isurvey_app.instrumento.id_instrumento from ajvieira_isurvey_app.instrumento)	     
order by
	productid
