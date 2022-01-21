function myAccFunc() {
	var x = document.getElementById("demoAcc");
	if (x.className.indexOf("w3-show") == -1) {
		x.className += " w3-show";
	} else {
		x.className = x.className.replace(" w3-show", "");
	}
}

// Open and close sidebar
function w3_open() {
	document.getElementById("mySidebar").style.display = "block";
	document.getElementById("myOverlay").style.display = "block";
}

function w3_close() {
	document.getElementById("mySidebar").style.display = "none";
	document.getElementById("myOverlay").style.display = "none";
}
var slideIndex = 1;
showDivs(slideIndex);

function plusDivs(n) {
	showDivs(slideIndex += n);
}

function showDivs(n) {
	var i;
	var x = document.getElementsByClassName("mySlides");
	if (n > x.length) { slideIndex = 1 }
	if (n < 1) { slideIndex = x.length }
	for (i = 0; i < x.length; i++) {
		x[i].style.display = "none";
	}
	x[slideIndex - 1].style.display = "block";
}
$(document).ready(function() {
	$('#summernote').summernote({
		placeholder: '내용을 작성하세요',
		height: 400,
		maxHeight: 400
	});
});
function goWrite(frm) {
	var title = frm.title.value;
	var text = frm.text.value;

	if (title.trim() == '') {
		alert("제목을 입력해주세요");
		return false;
	}
	if (text.trim() == '') {
		alert("본문을 입력해주세요");
		return false;
	}
	frm.submit();
}