function limitText(limitField, limitNum) {
    if (limitField.value.length > limitNum) {
        limitField.value = limitField.value.substring(0, limitNum);
    }
}

function shortDateString(milliseconds) {
    var months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
    var date = new Date(milliseconds);
    var str = months[date.getMonth()]
    str = str.concat(" ", date.getDate(), ", ", date.getFullYear());
    return str;
}