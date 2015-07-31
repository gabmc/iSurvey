select qid, type, question
from ajvieira_isurvey_lime.questions
where sid = ${fld:id}
order by qid