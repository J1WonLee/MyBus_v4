# MyBus
09-04 최초 등록 , 정류장 검색 , 최근 검색 저장
09-05 정류장 상세보기 화면 
==============================

09-16 기준 커밋 순서
MainFav -> ArrAlarm
==============================
AlarmActivity getDATAFROMINTENT 분기문 수정

09-19 기준 커밋 순서
ArrAlarm
=============================
AlarmArriveActivity -> initDialog -> stopArrAlarmService() {서비스 취소 메소드}
서비스 취소 메소드 테스트 해봐야함. (경기 -> 경기) , (경기 -> 서울) 역순도 포함.
현재는 dispose 시켜준 후 다시 서비스를 실행시키는 방식,
서비스 종료 후 재 실행 시 서비스가 바로 작동이 안된다. (dispose 시키는 방법이 안될 경우에는 alert창의 내용을 변경 정말 삭제하시겠습니까? 로)
