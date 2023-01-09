show tables;

create table member2 (
	idx int not null auto_increment,				/* 회원 고유번호 */  /* 프라이머리키를 뒤에서 선언하기 위해서..?*/
	mid 		 varchar(20) not null,						/* 회원 아이디 (중복 불허) */
	pwd 		 varchar(100) not null,					/* 비밀번호(SHA암호화 처리) */
	nickName varchar(20) not null,					/* 별명(중복 불허/ 수정가능) */
	name 		 varchar(20) not null,					/* 회원성명 */
	gender 	 varchar(5) default '남자',			/* 성별 */
	birthday datetime default now(),				/* 생일 */
	tel 		 varchar(15),										/* 전화번호(010-1234-5678)*/  /*(선택사항이라 not null아님) */
	address  varchar(100),									/* 회원주소(상품 배달 시 기본 주소로 사용) */ 
	email 	 varchar(50) not null,					/* 이메일(아이디/비밀번호) 분실 시 사용 - 형식체크 필수 */
	homePage varchar(50) not null,        	/* 홈페이지(블로그 주소)*/
	job 		 varchar(20), 									/* 회원 직업 */
	hobby 	 varchar(20),										/* 취미(2개 이상은 '/'로 구분) */
	photo 	 varchar(100) default 'noimage.jpg',  /* 회원사진 */
	content text, 													/* 회원 자기소개 */
	userInfor char(6) default '공개', 				/* 회원정보 공개여부(공개/비공개)*/
	userDel   char(2) default 'NO',					/* 회원 탈퇴 신청 여부(OK:탈퇴신청한 회원, NO:현재 가입중인 회원) */
	point int default 100, 									/* 회원 누적 포인트(가입시 기본 100증정 방문 시 1회 10포인트 증가, 1일 최대 50포인트) */
	level int default 4,   								  /* 회원 등급(0:관리자, 1:운영자 2:우수회원 3:정회원 4:준회원) */
	visitCnt int default 0, 							  /* 방문 횟수 */
	startDate datetime default now(),       /* 최초 가입일 */
	lastDate datetime  default now(), 			/* 마지막 접속일 */
	todayCnt int default 0, 								/* 오늘 방문한 횟수 */
	primary key(idx,mid)										/* 주 키 : idx(고유번호), mid(아이디) 프라이머리키 두개 주겠따 */
);


--drop table member;
--delete 
desc member2;

--insert into member values (default,'admin','1234','관리맨','관리자',default,default,'010-1234-5678','충북 청주시 705','u_u2222@naver.com','uuu2u22@naver.com','프리랜서','등산/바둑',default,'관리자입니다',default,default,default,0,default,default,default,default);
insert into member2 values (default,'admin','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','관리맨','관리자',default,default,'010-1234-5678','충북 청주시 705','u_u2222@naver.com','uuu2u22@naver.com','프리랜서','등산/바둑',default,'관리자입니다',default,default,default,0,default,default,default,default);
insert into member2 values (default,'bbb111','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','오렌이','정회원',default,default,'010-0000-5678','부산 남포동 330','rrke22@naver.com','ee2@naver.com','변호사','등산/바둑',default,'test',default,default,default,2,default,default,default,default);

select * from member2;

select * from member2 order by idx desc;
select * from member2 order by name;
