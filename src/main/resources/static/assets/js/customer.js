

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

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.dropdown-item').forEach(item => {
    item.addEventListener('click', filterTable);
});
    const table = document.getElementById("myTable");
    const tbody = table.querySelector("tbody");
    const rows = tbody.getElementsByTagName("tr");
    const rowsPerPage = 4;
    let currentPage = 1;
    let currentStatusFilter = "all";
    // Tạo các nút phân trang
    function createPagination() {
        const totalPages = Math.ceil(rows.length / rowsPerPage);
        const pagination = document.getElementById("pagination");
        pagination.innerHTML = '';

        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement("li");
            li.classList.add("page-item");

            const a = document.createElement("a");
            a.classList.add("page-link");
            a.href = "#";
            a.textContent = i;
            a.setAttribute("data-page", i);

            a.addEventListener("click", function () {
                currentPage = parseInt(this.getAttribute("data-page"), 10);
                displayPage(currentPage);
            });

            li.appendChild(a);
            pagination.appendChild(li);
        }
    }

    // Hiển thị trang hiện tại
    function displayPage(pageNumber) {
    currentPage = pageNumber;
    const startIndex = (pageNumber - 1) * rowsPerPage;
    const endIndex = startIndex + rowsPerPage;

    for (let i = 0; i < rows.length; i++) {
        const statusCell = rows[i].querySelector("td:nth-child(8"); // Assuming "Trạng thái" is in the 8th column
        const statusValue = statusCell.textContent.trim();

        if (currentStatusFilter === "all" || statusValue === currentStatusFilter) {
            if (i >= startIndex && i < endIndex) {
                rows[i].style.display = "table-row";
            } else {
                rows[i].style.display = "none";
            }
        } else {
            rows[i].style.display = "none";
        }
    }
}

// Lọc bảng theo trạng thái được chọn
function filterTable(event) {
    const selectedStatus = event.target.getAttribute('data-value')
    currentStatusFilter = selectedStatus;

    for (let i = 0; i < rows.length; i++) {
        const statusCell = rows[i].querySelector("td:nth-child(8"); // Assuming "Trạng thái" is in the 8th column
        const statusValue = statusCell.textContent.trim();

        if (selectedStatus === "all" || statusValue === selectedStatus) {
            rows[i].classList.remove("hidden");
        } else {
            rows[i].classList.add("hidden");
        }
    }

    // Tính lại tổng số trang dựa trên số hàng đã filter
    const visibleRows = Array.from(tbody.querySelectorAll("tr:not(.hidden)"));
    createPagination(visibleRows);
    displayPage(1);
}

// Bắt đầu bằng việc hiển thị trang đầu tiên khi trang web được tải
createPagination(rows);
displayPage(1);

// Lọc bảng theo trạng thái được chọn
function filterTable(event) {
    const selectedStatus = event.target.getAttribute('data-value')
    currentStatusFilter = selectedStatus;

    for (let i = 0; i < rows.length; i++) {
        const statusCell = rows[i].querySelector("td:nth-child(8"); // Assuming "Trạng thái" is in the 8th column
        const statusValue = statusCell.textContent.trim();

        if (selectedStatus === "all" || statusValue === selectedStatus) {
            rows[i].classList.remove("hidden");
        } else {
            rows[i].classList.add("hidden");
        }
    }

    // Tính lại tổng số trang dựa trên số hàng đã filter
    const visibleRows = Array.from(tbody.querySelectorAll("tr:not(.hidden)"));
    createPagination(visibleRows);
    displayPage(1);
}

// Bắt đầu bằng việc hiển thị trang đầu tiên khi trang web được tải
createPagination(rows);
displayPage(1);

    // Lọc bảng theo trạng thái được chọn
    function filterTable(event) {
        const selectedStatus = event.target.getAttribute('data-value')
        currentStatusFilter = selectedStatus;

        for (let i = 0; i < rows.length; i++) {
            const statusCell = rows[i].querySelector("td:nth-child(8)"); // Assuming "Trạng thái" is in the 8th column
            const statusValue = statusCell.textContent.trim();

            if (selectedStatus === "all" || statusValue === selectedStatus) {
                rows[i].classList.remove("hidden");
            } else {
                rows[i].classList.add("hidden");
            }
        }

        createPagination();
        displayPage(1);
    }

    // Lắng nghe sự kiện click cho tất cả các phần tử có class 'dropdown-item'
    document.querySelectorAll('.dropdown-item').forEach(item => {
        item.addEventListener('click', function (event) {
            const selectedStatus = event.target.getAttribute('data-value');
            const dropdownToggle = document.querySelector('.btn-secondary');
            dropdownToggle.innerText = event.target.innerText;

            filterTable(selectedStatus);
        });
    });

    // Bắt đầu bằng việc hiển thị trang đầu tiên khi trang web được tải
    createPagination();
    displayPage(1);
});

