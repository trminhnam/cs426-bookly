# 📚 cs426-bookly 📚
## Qui chuẩn tạo branch:

- Tạo các nhánh phụ là tên các thành viên. VD: duy, nam, nhat.
- Tạo nhánh tên: features/tên-task. VD: features/login.
- B1: checkout nhánh sang nhánh tên mình.
- B2: git checkout -b "features/..."
- B3: làm việc trên nhánh mình, git push origin 'nhánh mình' thoải mái
- B4: sau khi làm xong thì tạo merge request từ nhánh tên SANG MAIN, và chờ một người đứng ra merge vào main

### Notes:

- Khi nhận task mới thì nhớ checkout sang main trước và git fetch, git pull của main (để cập nhật code mới nhất) và tạo branch mới từ đây

## Qui chuẩn tạo biến

- Các biến UI đặt theo kí tự đầu: VD
    - EditText: Et
    - TextView: Tv
    - ListView: Lv
    - ImageView: Iv
    - Button: Btn
    - ...
- Tên biến + UI = tên biến UI. VD: login button -> loginBtn
- Các biến adapter đặt bình thường. VD: shopAdapter
- Các biến list đặt theo tên đặc trưng phía trước. VD: itemList
## Cấu trúc folder:

```
mobile-development-budgetmanagement
│   README.md
│       
└───app
    ├───java
    │   └───com
    │       └───example
    │           └───budgetlia
    │               ├───
    │               ├───
    │               ├───
    │               ├───
    │               ├───
    │               ├───
    │               └───
    └───res
        ├───drawable
        ├───layout
        ├───menu
        ├───mipmap
        ├───raw
        ├───values
        └───xml
```

## Defaut accounts:


## References

