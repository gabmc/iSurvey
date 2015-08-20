select 
	sid as productid,
	sid as productname, 
	'no tiene d' as categoryname,
	surveyls_title as nombre
from 
	ajvieira_isurvey_lime.surveys, ajvieira_isurvey_lime.surveys_languagesettings
where
	surveys.sid not in (select ajvieira_isurvey_app.instrumento.id_instrumento from ajvieira_isurvey_app.instrumento)
	and surveys.sid = surveys_languagesettings.surveyls_survey_id	     
order by
	productid
