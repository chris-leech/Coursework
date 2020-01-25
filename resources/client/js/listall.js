let imageList = [];

let pageSize = 5;    // Change this as desired.


function pageLoad() {


    /* Add this to the end of pageLoad function if your function already has code in it! */


    fetch('/image/list', {method: 'get'},

    ).then(response => response.json()

    ).then(images => {

        if (images.hasOwnProperty('error')) {

            alert(images.error);

        } else {

            imageList = images;

            displayThumbnails(0);

        }

    });


}


function displayThumbnails(startIndex) {


    let imagesHTML = '';


    let counter = 0;

    for (let image of imageList) {

        if (counter >= startIndex && counter < startIndex + pageSize) {

            imagesHTML += `<div style="display: inline-block; width: 120px; text-align: center; border: solid 1px black; margin:10px; padding:10px;">`;

            imagesHTML += `<a href="/client/img/pdf/${image}" target="_blank">`;

            imagesHTML += `<img src="/client/img/pdf/${image}" width="100px" alt="${image}">`;

            imagesHTML += `</a>`;

            imagesHTML += `</div>`;

        }

        counter++;

    }


    imagesHTML += `<div style="margin-bottom: 32px; padding-bottom: 16px; border-bottom: solid 3px silver; text-align: center">Page `;


    let n  = 0;

    while (n < imageList.length) {

        let style = '';

        if (startIndex === n) {

            style = 'background-color: yellow';

        }

        imagesHTML += `<button style="${style}" onclick="displayThumbnails(${n})">${Math.floor(n/pageSize)+1}</button> `;

        n += pageSize;

    }


    imagesHTML += `</div>`;


    document.getElementById("images").innerHTML = imagesHTML;


}