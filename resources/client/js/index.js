function pageLoad() {


    let now = new Date()
    let myHTML = '<div style="text-align:center;">'
        + '<h1>Tracking your progress</h1>'
        + '<img src="/client/img/papat.gif"  alt="Logo"/>'
        + '<div style="font-style: italic;">'
        + 'Generated at ' + now.toLocaleTimeString()
        + '</div>'

        + '</div>';

    document.getElementById("testDiv").innerHTML = myHTML;

    fetch('/user/get/4')
        .then(response => {
            return response.json()
        })
        .then(data => {
            // Work with JSON data here

            document.getElementById("crud").innerHTML = JSON.stringify(data);
        })
}








