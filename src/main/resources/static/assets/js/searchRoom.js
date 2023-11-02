// Ngăn người dùng chọn ngày trước đó
var today = new Date().toISOString().split("T")[0];
document.getElementById("checkInDate").min = today;
document.getElementById("checkOutDate").min = today;
