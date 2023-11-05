
    // Lấy ngày hôm nay và định dạng nó thành chuỗi "yyyy-MM-dd"
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    var yyyy = today.getFullYear();
    var formattedDate = yyyy + '-' + mm + '-' + dd;

    // Đặt giá trị mặc định cho trường ngày "checkIn" bằng JavaScript
    var checkInDateInput = document.getElementById("checkInDate");
    checkInDateInput.value = formattedDate;

    // Lấy ngày hôm nay và thêm 1 ngày để đặt giá trị mặc định cho trường ngày "checkOut"
    var nextDay = new Date(today);
    nextDay.setDate(nextDay.getDate() + 1);
    var nextDayFormatted = nextDay.toISOString().split("T")[0];
    var checkOutDateInput = document.getElementById("checkOutDate");
    checkOutDateInput.value = nextDayFormatted;

    // Đặt ngày tối thiểu cho trường "checkOut" để đảm bảo nó lớn hơn ngày "checkIn" ít nhất 1 ngày
    checkOutDateInput.min = nextDayFormatted;

    // Xử lý khi người dùng nhập ngày
    checkInDateInput.addEventListener("input", function () {
    var checkInValue = checkInDateInput.value;
    if (!isValidDate(checkInValue)) {
    alert("Ngày nhận phòng không hợp lệ. Vui lòng nhập lại.");
    checkInDateInput.value = formattedDate; // Đặt lại ngày nhận phòng thành ngày hôm nay
}
});

    checkOutDateInput.addEventListener("input", function () {
    var checkOutValue = checkOutDateInput.value;
    if (!isValidDate(checkOutValue)) {
    alert("Ngày trả phòng không hợp lệ. Vui lòng nhập lại.");
    checkOutDateInput.value = nextDayFormatted; // Đặt lại ngày trả phòng thành ngày mai
}
});

    // Hàm kiểm tra định dạng ngày hợp lệ
    function isValidDate(dateString) {
    var pattern = /^\d{4}-\d{2}-\d{2}$/;
    return pattern.test(dateString) && !isNaN(Date.parse(dateString));
}

    // Xử lý khi người dùng nhấn nút "Tìm Phòng"
    var submitButton = document.querySelector("button[type='submit']");
    submitButton.addEventListener("click", function () {
    if (checkInDateInput.value >= checkOutDateInput.value) {
    alert("Ngày trả phòng phải sau ngày nhận phòng. Vui lòng nhập lại.");
    event.preventDefault(); // Ngăn chuyển đến trang tìm kiếm nếu có lỗi.
}
});

    // Ngăn người dùng chọn ngày trước đó
    var today = new Date().toISOString().split("T")[0];
    document.getElementById("checkInDate").min = today;
    document.getElementById("checkOutDate").min = today;
