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


let lastTr;

function clearFormErrors() {
    $(".form-control.is-invalid").removeClass('is-invalid');
}

function revertEditForm() {

    if (lastTr) {
        trForm = $('#editFormRow');
        lastTr.insertAfter(trForm);
        trForm.insertAfter(lastTr.parent().find('tr').last())

        let tdsForm = trForm.find('td');
        tdsForm.eq(0).html("");
        tdsForm.eq(1).html("");
        tdsForm.eq(2).find('input:text').val("");
        tdsForm.eq(2).find('input[type=hidden]').val("");
        tdsForm.eq(3).html("0");

        $('#cancelSectionBtn').addClass('invisible');
        lastTr.show();
        lastTr = null;
    }
}

function showEditForm(idRow, noOverWriteName) {

console.log(idRow);

    lastTr = $(idRow);

    let tdsForm = $('#editFormRow').find('td');
    let tds = lastTr.find('td');

    let name = tds.eq(2).html();
    let id = idRow.substring(3);

    tdsForm.eq(0).html(tds.eq(0).html());
    tdsForm.eq(1).html(tds.eq(1).html());
    if (!noOverWriteName)
        tdsForm.eq(2).find('input:text').val(name);
    tdsForm.eq(2).find('input[type=hidden]').val(id);
    tdsForm.eq(3).html(tds.eq(3).html());

    $('#cancelSectionBtn').removeClass('invisible');
    $('#editFormRow').insertAfter(lastTr);
    $('#editFormRow').show();
    lastTr.insertAfter(lastTr.parent().find('tr').last())
    lastTr.hide();
}

$('.editButton').click(function(){

    revertEditForm();
    clearFormErrors()
    trId = $(this).closest('tr').attr('id');
    showEditForm('#'+trId);
});


$('#cancelSectionBtn').click(function() {

   revertEditForm();
   clearFormErrors();
});


$( document ).ready(function() {
    let id = $('#editFormRow input[type=hidden]').val();
    if (id) { showEditForm('#id'+id, true); }
});


function deleteSection(id, name) {

    $('#deleteSection').text(' '+name);
    $('#deleteSectionId').val(id);
    $('#deleteWarningModal').modal('show');
};