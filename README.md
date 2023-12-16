### Capstone Project - PR606
Cara Instalasi Aplikasi EcoScan


Melakukan Login / Register Untuk Masuk Kedalam Aplikasi
# Jika User Telah Memiliki Account
- User langsung memilih login
- Memasukkan email
- Memasukkan password

curl --location 'https://backend-dot-submission-ecoscan.et.r.appspot.com/auth/login' \
--data-urlencode 'email=john.doe@example.com' \
--data-urlencode 'password=securepassword'

#Jika User belum memiliki Account
- User memilih register
- Memasukkan fullname
- Memasukkan email
- Memasukkan password
- Memasukkan city/kota

curl --location 'https://backend-dot-submission-ecoscan.et.r.appspot.com/auth/register' \
--data-urlencode 'fullname=John Doe' \
--data-urlencode 'email=john.doe@example.com' \
--data-urlencode 'password=securepassword' \
--data-urlencode 'city=London'

Menghubungkan aplikasi dengan database
