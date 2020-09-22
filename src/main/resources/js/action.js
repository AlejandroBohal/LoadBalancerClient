let url = "/messages";
getMessages();
let btn = document.getElementById('add-message');
btn.addEventListener('click',() =>{
    let message = document.getElementById("message-input").value;
    let formData = {"mensaje":message,"fecha":""};
    axios.post(url, formData)
        .then(res => {
            getMessages();
        })
        .catch( error =>{
            console.log(error);
        })

});
function getMessages(){
    $("#messagesTable > tbody").empty();
    axios.get(url).then(res=>{
        res.data.map(row=>{
            console.log(row);
            $("#messagesTable > tbody").append(
                "<tr>" +
                " <td>" + row.mensaje + "</td>" +
                " <td>" + row.fecha + "</td>" +
                "</tr>"
            );
        })
    })
};