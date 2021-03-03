create table notification (
	id BINARY(16) not null, 
	channel varchar(10), 
	message varchar(2048),
	schedule_at datetime(6), 
	status varchar(10), 
	primary key (id)
) engine=InnoDB default charset=utf8;

create table recipient (
	notification_id BINARY(16) not null, 
	recipient varchar(255)
) engine=InnoDB default charset=utf8;

alter table recipient 
	add constraint FK_notification
	foreign key (notification_id) references notification (id);
