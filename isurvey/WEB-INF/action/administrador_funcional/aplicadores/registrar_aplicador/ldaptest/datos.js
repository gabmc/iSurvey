var flag = "${fld:found}";
if (flag=="Y") {
	document.form1.lname.value = "${fld:lname@js}";
	document.form1.fname.value = "${fld:fname@js}";
	document.form1.email.value = "${fld:email@js}";
	document.form1.control.value = "${fld:found@js}";
	document.form1.dn.value = "${fld:dn@js}";
} else {
	alert("No existe ese login en el directorio LDAP.");
	document.form1.userlogin.focus();
}
