***** CÁCH SỬ DỤNG CODE *****
-> Đẩy code lên git
1. tạo dự án trên git
2. vào thư mục code cần đẩy -> khởi tạo git bash
3. mở cmd/git init để khởi tạo git
4. git remote add origin https://github.com/username/****.git kết nối với git
5. git remote -v  hiển thị danh sách các kho lưu trữ từ xa (remote repositories) mà dự án của bạn đang liên kết
6. git add . thêm các thư mục có sự thay đổi lên git
7. git commit -m "" commit với chú thích các sự thay đổi
8. git push origin master đẩy code lên nhanh master

-> đẩy code lên nhánh
1.git checkout -b teennhanh Tạo nhánh mới
2. git push origin tennhanh đẩy code lên nhánh

-> clone code về
git clone https://github.com/hoang23hg/AppHomNayMacGi.git

******** Cách chạy code lần đầu **************
- Do trang chủ admin và user điều sử dụng "SELECT * from nhomsanpham order by random() limit 10" để lấy sản phẩm hiển thị 
nên lần đầu chạy code mặc định sẽ vào trang chủ và sẽ gặp lỗi "SELECT * from nhomsanpham order by random() limit 10"
1, hãy vào file AndroidManifest thay đổi thứ tự chạy ThemNhomSanPham_Activity trước để thêm danh mục và thêm sản phẩm
2, sau khi thêm xong thay đổi lại thứ tự trong AndroidManifest để MainActivity chạy đầu tiên đăng ký tài khoản để chạy bình thường
3, để thêm tài khoản admin có thể thêm ở trong code hoặc chạy App Inspection vào bảng taikhoan để thay đổi quyền 
