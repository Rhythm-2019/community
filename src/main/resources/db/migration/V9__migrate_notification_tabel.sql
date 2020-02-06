alter table NOTIFICATION alter column STATE rename to STATUS;

alter table NOTIFICATION alter column STATUS set default 0;

alter table NOTIFICATION
	add notifier_name varchar(100) not null;

alter table NOTIFICATION
	add outer_name varchar(256) not null;

