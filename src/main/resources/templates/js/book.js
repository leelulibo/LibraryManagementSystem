
document.addEventListener("DOMContentLoaded", function() {
    var fileUpload = document.getElementById("fileUpload");
    var profilePicture = document.getElementById("profilePicture");

    fileUpload.addEventListener("change", function(event) {
        var file = event.target.files[0];
        var reader = new FileReader();

        reader.onload = function(e) {
            profilePicture.src = e.target.result;
        };

        reader.readAsDataURL(file);
    });
});


        function searchBooks() {
            var input = document.getElementById("searchBox");
            var filter = input.value.toLowerCase();
            var table = document.getElementById("booksTable");
            var tr = table.getElementsByTagName("tr");

            for (var i = 1; i < tr.length; i++) {
                tr[i].classList.remove("highlight");
                var td = tr[i].getElementsByTagName("td");
                var matched = false;
                for (var j = 0; j < td.length; j++) {
                    if (td[j]) {
                        if (td[j].innerText.toLowerCase().indexOf(filter) > -1) {
                            matched = true;
                        }
                    }
                }
                if (matched) {
                    tr[i].style.display = "";
                    tr[i].classList.add("highlight");
                } else {
                    tr[i].style.display = "none";
                }
            }
        }