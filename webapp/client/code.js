$(document).ready(function() {
    var baseUrl = "http://localhost:8080";

    $('#searchbox').keypress(function (event) {
      if ( event.which == 13 ) {
           $("#searchbutton").click();
        }
    }
    )

    $("#searchbutton").click(function() {
        console.log("Sending request to server.");
        $.ajax({
            method: "GET",
            url: baseUrl + "/search",
            data: {query: $('#searchbox').val()}
        }).success( function (data) {
            console.log("Received response " + data);
            $("#responsesize").html("<p>" + data.length + " websites retrieved</p>");
            var buffer = "<ul>\n";
            $.each(data, function(index, value) {
                buffer += "<li><a href=\"" + value.url + "\">" + value.title + "</a> - Sentiment score: "  + value.sentiment + "</li>\n";
            });
            buffer += "</ul>";
            $("#urllist").html(buffer);
        });
    });
});
