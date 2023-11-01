

//popover
document.addEventListener("DOMContentLoaded", function () {
    const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
    const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl));
});


//filter status
document.addEventListener("DOMContentLoaded", function () {
    // Lắng nghe sự kiện click cho tất cả các phần tử có class 'dropdown-item'
    document.querySelectorAll('.dropdown-item').forEach(item => {
        item.addEventListener('click', filterTable);
    });
});

function filterTable(event) {
    const selectedStatus = event.target.getAttribute('data-value');
    const dropdownToggle = document.querySelector('.btn-secondary');
    dropdownToggle.innerText = event.target.innerText;

    // Get all rows in the table
    const rows = document.querySelectorAll("table tbody tr");

    // Loop through the rows and hide/show based on the selected status
    rows.forEach(row => {
        const statusCell = row.querySelector("td:nth-child(8)"); // Assuming "Trạng thái" is in the 8th column

        if (selectedStatus === "all" || statusCell.textContent === selectedStatus) {
            row.style.display = "table-row";
        } else {
            row.style.display = "none";
        }
    });
}


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
