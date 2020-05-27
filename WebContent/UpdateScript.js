/**
 * 
 */
function Passcheck(){
	alert("TEST");
	if(document.updateFrm.pass.value == ""){
		alert("수정을 위해 패스워드를 입력하세요");
		document.updateFrm.pass.focus();
		return false;
	}
	document.updateFrm.submit();
}