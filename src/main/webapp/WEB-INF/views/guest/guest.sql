show tables;

create table guest2 (
	idx int not null auto_increment primary key,	/* 고유번호 */
	name varchar(20) not null,					/* 방문자 성명 */
	email varchar(60),									/* 이메일 주소 (써도 되고 안써도 됨 낫널 뺀다) */
	homePage varchar(60),								/* 홈페이지 주소 */
	visitDate datetime default now(),		/* 방문일자 */
	hostIp varchar(50) not null,				/* 방문자 Ip */
	content text not null								/* 방문소감 */
);

drop table guest2;
desc guest2;

insert into guest2 values (default, '관리자', 'u_u2222@naver.com', 'http://naver.com', default, '192.168.50.250', '방명록 서비스를 개시합니다');

select * from guest2;