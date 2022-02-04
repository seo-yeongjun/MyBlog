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
var $item = document.getElementById("num");
var a = $item.getAttribute(':num');
showDivs(slideIndex);

for (var i = 0; i < a; i++) {
	plusDivs(1);
}

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

function showDetails() {
	var detail = document.getElementById('myDetails');
	if (detail.open) {
		detail.open = false;
	} else {
		detail.open = true;
	}

}
function showDetailsMobile() {
	var detail = document.getElementById('myDetailsMoblie');
	detail.open = true;
	w3_open();
}

$(function() {
	$("[data-confirm-delete]").click(function() {
		return confirm("삭제한 내용은 복구 할 수 없습니다.\n삭제하시겠습니까?");
	});
});
function categoryDel() {
	return confirm("카테고리를 삭제하면 포함된 모든 글이 삭제됩니다.\n정말 삭제하시겠습니까?");
}
function show_delete() {
	var a = document.getElementsByClassName("myDelete");
	if (a[0].style.display == "block") {
		for (i = 0; i < a.length; i++) {
			a[i].style.display = "none";
		}
	}
	else {
		for (i = 0; i < a.length; i++) {
			a[i].style.display = "block";
		}
	}
}
