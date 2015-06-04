INSERT INTO ajvieira_isurvey_lime.tokens_{{id_encuesta}}
(firstname, lastname, email, emailstatus, token, language, 
	sent, remindersent, remindercount, completed, usesleft)
VALUES
('{{firstname}}', '{{lastname}}', '{{email}}', 'OK', '{{token}}', 'es', 'N',
'N', '0', 'N', 999)