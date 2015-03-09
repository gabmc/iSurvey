select app_alias from ${schema}s_application
where app_id <> ${fld:app_id}
and app_alias = ${fld:app_alias}
