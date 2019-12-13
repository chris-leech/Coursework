
function pageLoad() {


    let now = new Date()
    let myHTML = '<div style="text-align:center;">'
        + '<h1>Tracking your progress</h1>'
        + '<img src="/client/img/patpat.gif"  alt="Logo"/>'
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
            console.log(data);
            let obj = JSON.parse(JSON.stringify(data));
            console.log(obj);
            // Work with JSON data here
            console.log(obj.firstname)
            document.getElementById("crud").innerHTML = JSON.stringify(obj.email);
        })
}








