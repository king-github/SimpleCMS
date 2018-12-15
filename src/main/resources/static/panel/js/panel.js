$('#formDeleteArticles .deleteCheckbox').click(function(){

    let sum=0;
    $('#formDeleteArticles .deleteCheckbox').each(function(){

        if ($( this ).is(":checked")) {
               sum++;
        }
    })
    $('#numOfDeleteArticles').text(' '+sum+' ');

    $('#deleteSubmit').attr("disabled", sum==0);

});