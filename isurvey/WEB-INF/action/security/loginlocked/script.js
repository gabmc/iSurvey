alert("${lbl:s_loginlock_title}\n${lbl:s_loginlock_msg1}\n${lbl:s_loginlock_msg2}");
setFormErrorMsg("form_error", "${lbl:s_loginlock_title}<br>${lbl:s_loginlock_msg1}<br>${lbl:s_loginlock_msg2}");
document.getElementById("loginbutton").disabled=false;