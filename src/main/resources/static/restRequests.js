let summaryForm = document.forms.namedItem("summaryForm");
summaryForm.addEventListener('submit', function (e) {
    let url = this.getAttribute("action");
    let method = this.getAttribute("method").toUpperCase();
    let formData = new FormData(this);

    $.ajax({
        url: url,
        type: method,
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function (response) {
            let result = "";
            result += "Classic essay:<br>";
            result += "-source: " + response.classicEssay.source + '<br>';
            result += "-essay : " + response.classicEssay.essay + '<br>';

            result += "Key Words Essay:<br>";
            for (let i = 0; i < response.keyWordsEssay.length; i++) {
                let word = response.keyWordsEssay[i];
                result += "-word: " + word.text + '<br>';
            }

            result += "Response time : " + response.responseTime + 'sec' + '<br>';

            document.getElementById('result').innerHTML = result;
        },
        error: function (message) {
            // TODO: add handling error
        }
    });
    e.preventDefault();
}, false);