# back-end-ems

### KURULUMU INTELLIJ IDEA ILE YAPMANIZ GEREKMEKTEDİR. 

- IntelliJ ile dosyayı açmadan önce, PostgreSQL kurulumu yapmanız gerekmektedir. Bunun için 

### https://www.postgresql.org/download/

linkinden kurulum yapmanız gerekmektedir. Adının postgres ve portun da 5432 olmasına dikkat edin. 

- IntelliJ Idea ile projeyi version control ile git üzerinden açmanız gerekmektedir. 

- Veritabanı bağlanmalıdır. Bunun için intellijin veritabanı bölümü açılmalı ve datasource olarak postgresql eklenmelidir, ilgili yerlere postgre username ve şifresi de girilmelidir. Port 5432 olmalı. 

- PostgreSQL 'e bağlanabilmek için postgreSQL için driver eklenmelidir. Drivers kısmından ilgili driverın yüklenmesi gerekir. 

- PostgreSQL'e bağlandıktan sonra, ilgili bağlantıda

### `eventmanager`

adlı bir database açılmalıdır. 

- Bu aşamadan sonra ise maven dependency kurulumunun yapılması gerekmektedir. 
