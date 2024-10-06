let gameInfoCount = 0;
let userGameInfo = [];
$(function() {

    userGameInfo = [];

    // 생년월일
    $('#year_of_birth').datepicker({
        changeYear: true,     // 년도 선택 가능하게
        dateFormat: 'yy',     // 년도만 표시
        yearRange: "c-100:c+10",  // 현재 년도 기준으로 100년 전부터 10년 후까지 선택 가능
        onClose: function(dateText, inst) {
            // 월/일을 선택하지 않도록 연도를 선택한 뒤, 날짜 선택기 닫기
            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
            $(this).datepicker('setDate', new Date(year, 1, 1));
        }
    });

    // select 스타일 조정
    $('#mbti, #start_time, #end_time').on('change', function() {
        if($(this).val() == ''){
            $(this).css('color', 'rgba(211, 211, 211, 0.5)');
        } else{
            $(this).css('color', 'lightgrey');
        }
    });
    $('#mbti, #start_time, #end_time').trigger('change');

    // 게임 정보 게임 선택
    $(document).on('click', '.profile-info-game-box .profile-info-game-btn', function() {
        // 변경된 경우에만 적용
        if($(this).hasClass('select'))
            return;

        // $('.profile-info-game-box .profile-info-game-btn').removeClass('select');
        // $(this).addClass('select');
        changeGameAttr($(this).attr('id'));
    });

    // 팝업창 닫기
    $("#gameInfo-popup").click(function(event) {
        if ($(event.target).is("#gameInfo-popup")) {
            closeGameInfoPopup();
        }
    });

    //  팝업 게임 선택
    $(document).on('click', '.gameInfo-popup-gamebox .profile-info-game-btn', function() {
        $('.gameInfo-popup-gamebox .profile-info-game-btn').removeClass('select');
        $(this).addClass('select');
    });


});

function changeStep(stepVal){
    $('.profile-info-box').css('display', 'none');  // info-box 제거
    $('#profile-info-box-step' + stepVal).css('display', 'flex');

    // step 버튼
    $('.profile-step-btn').removeClass('active');   // active 제거
    $('#profile-step-btn-'+stepVal).addClass('active');

    if(stepVal === 2 && $('.profile-info-game-box .profile-info-game-btn').length === 0){
        openGameInfoPopup()
    }
}

function openGameInfoPopup(){
    // 기존 박스 정보 조회
    var gameArr = [];
    $('#profile-info-game-box .profile-info-game-btn').each(function() {
        gameArr.push($(this).attr('id'));
    });

    $('#gameInfo-popup-gamebox').empty();
    // 게임 정보 조회
    $.ajax({
        url: '/gameInfo',
        type: 'get',
        dataType : 'json',
        async: false,
        success: function(res) {
            gameInfoCount = res.length;
            var addTag = ``;
            res.forEach(function(item, index) {
                // 이미 내 게임 목록에 있으면 추가x
                if(gameArr.includes(item.gameId)){
                    return;
                }
                addTag += `
                    <button class="profile-info-game-btn" id="popup-${item.gameId}">
                        <img src="/images/gameImg/${item.gameId}.png" />
                    </button>
                `;
            });
            $("#gameInfo-popup-gamebox").append(addTag);
            $("#gameInfo-popup").show();
        },
        error: function (data, status, err) {
            alert('게임 정보 조회 중 문제가 발생하였습니다. 잠시 후 다시 시도하세요.');
            closeGameInfoPopup();
        }
    });
}

function closeGameInfoPopup(){
    $("#gameInfo-popup").hide();
}

function addPopupGameInfo(){
    var selectGameInfo = $('.gameInfo-popup-gamebox .profile-info-game-btn.select');
    if(selectGameInfo.length === 0){
        alert('선호하는 게임을 선택하세요.');
        return;
    }

    var gameCount = $('#profile-info-game-box .profile-info-game-btn').length;
    var gameId = selectGameInfo.attr('id').split('-')[1];
    var addTag =`
        <button class="profile-info-game-btn" id="${gameId}">
            <img src="/images/gameImg/${gameId}.png" />
            <span class="profile-info-game-delete-btn" onclick="deleteGameInfo('${gameId}')">×</span>
        </button>
    `;
    $('#profile-info-game-box').append(addTag);

    // 추가한 첫번째 게임은 속성까지 같이 출력
    changeGameAttr(gameId);

    openGameInfoPopupCheck();   //  게임 추가 버튼 출력 여부
    closeGameInfoPopup();       //  팝업 게임 선택창 닫기

    var newGameInfo = {
        gameId: gameId,
        info: {}
    };
    userGameInfo.push(newGameInfo);
}

// 게임 제거
function deleteGameInfo(gameId){

    var delGameIndex = userGameInfo.findIndex(game => game.gameId === gameId);  // // 저장 데이터 삭제
    if(delGameIndex >= 0 ){
        userGameInfo.splice(delGameIndex, 1);
    }

    var delGameId = $('.profile-info-game-box .profile-info-game-btn.select').attr('id');
    $('#profile-info-game-box').find(`#${gameId}`).remove();    // 선호 게임 버튼 삭제
    openGameInfoPopupCheck();   // 게임 추가 버튼

    if(gameId === delGameId){
        $('.profile-info-gameAttr-box').empty();    // 게임 속성 초기화
        $('.profile-info-game-box .profile-info-game-btn').first().trigger('click');    // 첫번째 게임에 select 옵션 추가
    }

    // 게임 모두 삭제했을 경우 추가 팝업 띄우기
    if($('.profile-info-game-box .profile-info-game-btn').length === 0){
        openGameInfoPopup()
    }
}

// 게임 추가 버튼 출력 여부
function openGameInfoPopupCheck(){
    if($('.profile-info-game-box .profile-info-game-btn').length === gameInfoCount ){
        $('#game-add-btn').hide();
    } else{
        $('#game-add-btn').show();
    }
}


// 게임 속성 창 변경
function changeGameAttr(gameId){

    // 기존 입력값 저장
    if($('.profile-info-gameAttr-box').children().length > 0){
        var selectGameId = $('.profile-info-game-box .profile-info-game-btn.select').attr('id');
        var currentGameInfo = {
            gameId: selectGameId,
            info: {}
        };

        // 각 select 필드의 값을 저장
        $('.profile-info-gameAttr-box-col select').each(function() {
            var id = $(this).attr('id'); // select 요소의 id 값
            var value = $(this).val() || '';   // 사용자가 선택한 값
            currentGameInfo.info[id] = value; // 게임 정보에 저장
        });

        // 이미 저장된 게임이 있는지 확인 (배열에서 찾아서 업데이트)
        var existingGameIndex = userGameInfo.findIndex(game => game.gameId === selectGameId);
        userGameInfo[existingGameIndex] = currentGameInfo;
        // if (existingGameIndex !== -1) { // 기존에 저장된 게임 정보 업데이트
        //     userGameInfo[existingGameIndex] = currentGameInfo;
        // } else {    // 새로운 게임 정보 추가
        //     userGameInfo.push(currentGameInfo);
        // }
    }

    $('.profile-info-gameAttr-box').empty();    // 초기화


    // 선택한 게임 select 클래스 적용
    $('.profile-info-game-box .profile-info-game-btn').removeClass('select');
    $(`#${gameId}`).addClass('select');


    var savedGameInfo = userGameInfo.find(game => game.gameId === gameId);
    // select optiop 체크 여부
    function gameAttrSelectCheck(key, optionKey){
        // 기존에 저장된 게임 정보를 가져오는 로직
        if(!savedGameInfo)
            return '';

        var savedInfo = savedGameInfo.info;
        return savedInfo[key] === optionKey ? 'selected':'';
    }

    $.ajax({
        url: '/gameInfo/'+gameId,
        type: 'get',
        dataType : 'json',
        async: false,
        success: function(res){

            // 게임명
            var addTag = `
                <div class="profile-info-gameAttr-box-title">
                    | ${res.gameNm}
                </div>
            `;
            $('.profile-info-gameAttr-box').prepend(addTag);    // 맨 앞에 추가

            var infoData = Object.entries(res.info);    // info 객체에서 항목을 배열로 변환
            var $gameAttrBox = $('.profile-info-gameAttr-box');
            var $colDiv = $('<div class="profile-info-gameAttr-box-col"></div>');
            var count = 0;

            infoData.forEach(function([key, value]) {
                var $wrapperDiv = $('<div class="input-wrapper-ph"></div>');
                var $select = $('<select id="' + key + '" required><option value="" ></option></select>');
                var $label = $('<label>' + value.infoNm + '</label>');

                // 속성 추가
                Object.entries(value).forEach(function([optionKey, optionValue]) {
                    if (optionKey !== 'infoNm') {
                        $select.append(`<option value="${optionKey}" ${gameAttrSelectCheck(key, optionKey)} >${optionValue}</option>`);
                        // $select.append('<option value="' + optionKey + '" >' + optionValue + '</option>');
                    }
                });

                // wrapper에 select와 label 추가
                $wrapperDiv.append($select).append($label);
                $colDiv.append($wrapperDiv);

                count++;

                // 2개 아이템을 추가한 후 새로운 컬럼 div 생성
                if (count % 2 === 0) {
                    $gameAttrBox.append($colDiv);
                    $colDiv = $('<div class="profile-info-gameAttr-box-col"></div>'); // 새 컬럼 div
                }
            });

            // 마지막 컬럼 div가 비어있지 않으면 추가
            if ($colDiv.children().length > 0) {
                $gameAttrBox.append($colDiv);
            }
        },
        error: function (data, status, err) {

        }
    });
}