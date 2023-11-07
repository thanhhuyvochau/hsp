var today = new Date();
var dd = String(today.getDate()).padStart(2, '0');
var mm = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
var yyyy = today.getFullYear();
var formattedDate = yyyy + '-' + mm + '-' + dd;
var nextDay = new Date(today);
nextDay.setDate(nextDay.getDate() + 1);
var nextDayFormatted = nextDay.toISOString().split("T")[0];

// Lấy giá trị từ các trường nhập liệu
var checkInDateInput = document.getElementById("checkInDate");
var checkOutDateInput = document.getElementById("checkOutDate");
var numberOfPeopleInput = document.getElementById("numberOfPeople");

// Lấy giá trị từ trường "checkIn"
var checkInValue = checkInDateInput.value;

// Lấy giá trị từ trường "checkOut"
var checkOutValue = checkOutDateInput.value;

// Lấy giá trị số người đã chọn
var numberOfPeopleValue = numberOfPeopleInput.value;

// Kiểm tra và xử lý ngày
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


document.getElementById("searchForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Ngăn gửi yêu cầu GET mặc định

    var checkInDate = document.getElementById("checkInDate").value;
    var checkOutDate = document.getElementById("checkOutDate").value;
    var numberOfPeople = document.getElementById("numberOfPeople").value; // Lấy số người đã chọn

    // Tạo URL chứa thông tin ngày và số người, và thêm vào đó
    var url = '/room/search' +
        '?checkIn=' + encodeURIComponent(checkInDate) +
        '&checkOut=' + encodeURIComponent(checkOutDate) +
        '&numberOfPeople=' + encodeURIComponent(numberOfPeople);

    // Điều hướng đến URL mới với thông tin ngày và số người bằng yêu cầu GET
    window.location.href = url;
});

// Lấy các tham số truy vấn từ URL
var urlParams = new URLSearchParams(window.location.search);
var checkInParam = urlParams.get("checkIn");
var checkOutParam = urlParams.get("checkOut");
var numberOfPeopleParam = urlParams.get("numberOfPeople");

// Đặt lại giá trị trường "checkIn" nếu có tham số truy vấn
if (checkInParam) {
    document.getElementById("checkInDate").value = checkInParam;
}

// Đặt lại giá trị trường "checkOut" nếu có tham số truy vấn
if (checkOutParam) {
    document.getElementById("checkOutDate").value = checkOutParam;
}

// Đặt lại giá trị trường "numberOfPeople" nếu có tham số truy vấn
if (numberOfPeopleParam) {
    document.getElementById("numberOfPeople").value = numberOfPeopleParam;
}
