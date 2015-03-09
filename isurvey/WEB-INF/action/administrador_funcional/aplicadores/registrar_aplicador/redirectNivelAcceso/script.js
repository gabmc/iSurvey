var a = "${fld:esvisualizador}";
var b = "${fld:nombrevisualizador}"
var c = "${fld:empresaid}"
//clearSession();
if (a!=0){
  //alert (a+"------"+b);
  //alert('${def:context}/action/mod_administrador_add/cuenta/registrar/nivelAcceso?userid='+a+"&fname="+b+"&empresaid="+c);
  window.location='${def:context}/action/mod_administrador_add/cuenta/registrar/nivelAcceso?userid='+a+"&fname="+b+"&empresaid="+c;
}else {
    window.location='${def:context}/action/security/home/welcome';
}


