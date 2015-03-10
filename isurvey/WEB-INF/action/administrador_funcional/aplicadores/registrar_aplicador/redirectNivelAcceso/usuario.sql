select case
          when
           (select
	   role_id
           from ${schema}s_user_role, ${schema}s_user
           where
           s_user.userlogin = ${fld:userlogin} and
           s_user.user_id =  s_user_role.user_id and
           s_user_role.role_id= 504 )  is not null
          then (select user_id from ajvieira_isurvey_security.s_user where userlogin = ${fld:userlogin})
          when
           (select
	   role_id
           from ${schema}s_user_role, ${schema}s_user
           where 
           s_user.userlogin = ${fld:userlogin} and
           s_user.user_id = s_user_role.user_id and
           s_user_role.role_id= 504)  is  null
           then '0' end as esvisualizador,

       case
          when
           (select
	   role_id
           from ${schema}s_user_role, ${schema}s_user
           where
           s_user.userlogin = ${fld:userlogin} and
           s_user.user_id =  s_user_role.user_id and
           s_user_role.role_id= 504 )  is not null
          then (select  lname||' '||fname from ajvieira_isurvey_security.s_user where userlogin = ${fld:userlogin})
          when
           (select
	   role_id
           from ${schema}s_user_role, ${schema}s_user
           where
           s_user.userlogin = ${fld:userlogin} and
           s_user.user_id = s_user_role.user_id and
           s_user_role.role_id= 504)  is  null
           then '' end as nombrevisualizador,

     case
          when
           (select
	   role_id
           from ${schema}s_user_role, ${schema}s_user
           where
           s_user.userlogin = ${fld:userlogin} and
           s_user.user_id =  s_user_role.user_id and
           s_user_role.role_id= 504 )  is not null
          then (select  id_empresa from ajvieira_isurvey_security.s_user , usuario_empresa 
                                   where s_user.userlogin = ${fld:userlogin} and
                                         s_user.user_id = usuario_empresa.user_id )
          when
           (select
	   role_id
           from ${schema}s_user_role, ${schema}s_user
           where
           s_user.userlogin = ${fld:userlogin} and
           s_user.user_id = s_user_role.user_id and
           s_user_role.role_id= 504)  is  null
           then '0' end as empresaid