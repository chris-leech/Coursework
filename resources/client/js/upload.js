function pageLoad() {


    /* Add this to the end of pageLoad function if your function already has code in it! */


    document.getElementById("imageUploadForm").addEventListener("submit", uploadImage);


}


function uploadImage(event) {


    event.preventDefault();


    const imageUploadForm = document.getElementById('imageUploadForm');


    if (document.getElementById('file').value !== '') {


    //    imageUploadForm.style.display = 'none';

        document.getElementById('uploading').style.display = 'block';


        let fileData = new FormData(imageUploadForm);


        fetch('/image/upload', {method: 'post', body: fileData},

        ).then(response => response.json()

        ).then(data => {


                if (data.hasOwnProperty('error')) {

                    alert(data.error);

                } else {

                    document.getElementById('file').value = '';

                }

                imageUploadForm.style.display = 'block';


                document.getElementById('uploading').innerHTML = "Uploaded!";

            }

        );


    } else {


        alert('No file specified');


    }


}