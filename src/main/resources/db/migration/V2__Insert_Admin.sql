INSERT INTO `salary_app`.`users` (`id`, `active`, `email`, `name`, `password`, `phone_number`)
VALUES (1, true, 'user@mail.com', 'user', '$2a$08$h9fXyuBPLowZzoRJ1erqOeBsGRPGYlV75PpEdIaY89Fy7KTvbLJMq', '123');

INSERT INTO `salary_app`.`user_role` (`user_id`, `roles`)
VALUES (1, 'ADMIN');