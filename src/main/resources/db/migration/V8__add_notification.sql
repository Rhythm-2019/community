create table NOTIFICATION
(
	ID INT auto_increment,
	NOTIFIER INT not null,
	RECEIVER INT not null,
	OUTER_ID INT not null,
	TYPE INT not null,
	STATE INT not null,
	GMT_CREATE BIGINT,
	constraint NOTIFICATION_PK
		primary key (ID)
);
