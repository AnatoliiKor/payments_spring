delete
from pay_db_test.public.usr;
insert into usr(id, active, email, last_name, middle_name, name, password, phone_number, registered,
                                   role)
VALUES (1, true, 'admin@gmail.com', 'Kornienko', 'Ivanovich', 'Anatolii',
        '$2a$08$bkhamcSJh8VG1BILUQ8EROy3A9UZxiCS6ki18CWdzkh6JO/EOkVre', 1111111111, '2021-09-19 18:57:49.593', 'ADMIN'),
       (2, true, 'client@gmail.com', 'Ivanov', 'Ivanovich', 'Ivan',
        '$2a$08$bkhamcSJh8VG1BILUQ8EROy3A9UZxiCS6ki18CWdzkh6JO/EOkVre', 2222222222, '2021-09-19 18:57:49.593', 'CLIENT');