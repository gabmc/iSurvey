alert ('La Lista ha sido removida del Estudio');
addNew();
search(${fld:id_empresa});
document.getElementById('id_empresa_search').value = ${fld:id_empresa}
document.getElementById('id_empresa_name_search').value = "${fld:nombre_empresa}"