show tables;

create table schedule2 (
	idx		int not null auto_increment primary key,
	mid		varchar(20) not null,
	sDate datetime not null,		/* 일정 등록한 날짜 */
	part 	varchar(20) not null,	/* 1.모임 2.업무 3.학습 4.여행 0 기타 */
	content text not null 			/* 일정 상세 내역 */
);

desc schedule2;

insert into schedule2 values (default, 'ddd111', '2023-01-01', '모임', '가족회의, 장소:집, 시간:19시');
insert into schedule2 values (default, 'ddd111', '2023-01-03', '학습', '구몬2회, 장소:스터디카페, 시간:12시');
insert into schedule2 values (default, 'ddd111', '2023-01-04', '여행', '강릉, 장소:강릉터미널, 시간:09:30분');
insert into schedule2 values (default, 'ddd111', '2023-01-04', '여행', '강릉, 장소:강릉 보사노바, 시간:13:30분');
insert into schedule2 values (default, 'ddd111', '2023-01-09', '학습', '구몬3회, 장소:집, 시간:11시');
insert into schedule2 values (default, 'ddd111', '2023-01-12', '업무', '롤러코스터 만들기, 장소:식스플래그, 시간:10시');
insert into schedule2 values (default, 'ddd111', '2023-01-13', '업무', '롤러코스터 수리, 장소:식스플래그, 시간:11시');
insert into schedule2 values (default, 'ddd111', '2023-01-28', '기타', '쉬는날, 장소:집, 시간:08:30분');
insert into schedule2 values (default, 'ddd111', '2023-01-29', '모임', '효지만나는 날, 장소:강남역, 시간:13시');
insert into schedule2 values (default, 'ddd111', '2023-01-30', '업무', '롤러코스터 만들기, 장소:식스플래그 뒷동산, 시간:11시');
select * from schedule2;

-- drop table schedule2;

select * from schedule2 where mid='ddd111' order by sDate;
select * from schedule2 where mid='ddd111' and sDate='2023-1' order by sDate;  /* X */
select * from schedule2 where mid='ddd111' and sDate='2023-01' order by sDate; /* X */
select * from schedule2 where mid='ddd111' and sDate='2023-01-13' order by sDate;
select * from schedule2 where mid='ddd111' and substring(sDate,1,7)='2023-01' order by sDate;
select * from schedule2 where mid='ddd111' and date_format(sDate, '%Y-%m')='2023-01' order by sDate;
select * from schedule2 where mid='ddd111' and date_format(sDate, '%Y-%m')='2023-01' group by substring(sDate,1,7) order by sDate;
select sDate,count(*) from schedule2 where mid='ddd111' and date_format(sDate, '%Y-%m')='2023-01' group by substring(sDate,1,7) order by sDate;
select sDate,part from schedule2 where mid='ddd111' and date_format(sDate, '%Y-%m')='2023-01' order by sDate, part;



