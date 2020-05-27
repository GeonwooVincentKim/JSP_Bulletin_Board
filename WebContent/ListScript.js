/**
 * 
 */
function list(){
	document.listFrm.action = "list.jsp";
	document.listFrm.submit();
}

function pageing(page){
	document.readFrm.nowPage.value = page;
	document.readFrm.submit();
}

function read(num){
	document.readFrm.num.value = num;
	document.readFrm.action="read.jsp";
	document.readFrm.submit();
}

function check(){
	if(document.searchFrm.keyWord.value == ""){
		alert("검색어를 입력하세요!!");
		document.searchFrm.keyWord.focus();
		return;
	}
	document.searchFrm.submit();
}