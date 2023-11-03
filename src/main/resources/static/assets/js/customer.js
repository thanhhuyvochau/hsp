
//popover
document.addEventListener("DOMContentLoaded", function () {
    const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
    const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl));
});


document.addEventListener("DOMContentLoaded", function () {
    const submitButton = document.querySelector("button[type='submit']");

    // Lắng nghe sự kiện click trên nút "Xác nhận đặt phòng"
    submitButton.addEventListener("click", function (event) {
        const inputName = document.getElementById("inputName").value;
        const inputEmail = document.getElementById("inputEmail").value;
        const inputPhone = document.getElementById("inputPhone").value;
        const inputAddress = document.getElementById("inputAddress").value;
        const gridCheck = document.getElementById("gridCheck");

        // Kiểm tra xem tất cả các trường thông tin bắt buộc đã được điền
        if (!inputName || !inputEmail || !inputPhone || !inputAddress) {
            event.preventDefault(); // Ngăn chặn việc gửi biểu mẫu
            alert("Vui lòng điền đầy đủ thông tin bắt buộc.");
        }

        // Kiểm tra xem checkbox đã được đánh dấu
        if (!gridCheck.checked) {
            event.preventDefault(); // Ngăn chặn việc gửi biểu mẫu
            alert("Vui lòng đọc và đánh dấu vào ô bên cạnh.");
        }
    });
});


$(document).ready(function () {
    // Setup - add a text input to each footer cell
    $('#theadtr')
        .clone(true)
        .addClass('filters')
        .appendTo('#thead');
 
    var table = $('#myTable').DataTable({
        orderCellsTop: true,
        fixedHeader: true,
        initComplete: function () {
            var api = this.api();
 
            // For each column
            api
                .columns()
                .eq(0)
                .each(function (colIdx) {
                    // Set the header cell to contain the input element
                    var cell = $('.filters th').eq(
                        $(api.column(colIdx).header()).index()
                    );
                    var title = $(cell).text();
                    $(cell).html('<input type="text" placeholder="' + title + '" />');
 
                    // On every keypress in this input
                    $(
                        'input',
                        $('.filters th').eq($(api.column(colIdx).header()).index())
                    )
                        .off('keyup change')
                        .on('change', function (e) {
                            // Get the search value
                            $(this).attr('title', $(this).val());
                            var regexr = '({search})'; //$(this).parents('th').find('select').val();
 
                            var cursorPosition = this.selectionStart;
                            // Search the column for that value
                            api
                                .column(colIdx)
                                .search(
                                    this.value != ''
                                        ? regexr.replace('{search}', '(((' + this.value + ')))')
                                        : '',
                                    this.value != '',
                                    this.value == ''
                                )
                                .draw();
                        })
                        .on('keyup', function (e) {
                            e.stopPropagation();
 
                            $(this).trigger('change');
                            $(this)
                                .focus()[0]
                                .setSelectionRange(cursorPosition, cursorPosition);
                        });
                });
        },
    });
});