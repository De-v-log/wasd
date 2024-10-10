let gameInfoCount = 0;
let gameInfoList = [];
$(function () {

    // 가입된 사용자인지 확인
    $.ajax({
        url: `/user/${oauthUserInfo.id}`
        ,type: 'get'
        ,dataType: 'json'
        ,success: function (res) {
            if(res.userId){
                window.location.href = '/board';
            }
        },error: function (xhr) {
            const errorMessage = xhr.responseJSON?.message || '서버 오류가 발생했습니다. 다시 시도해 주세요.';
            alert(errorMessage);
        }
    });

    // 프로필 기본 정보 설정
    $('#email').val(oauthUserInfo.email);
    $('#nickname').val(oauthUserInfo.nickname);
    $('#profileImg').attr('src', oauthUserInfo.profileImg);
    gameInfoList = [];

    // select 스타일 조정
    $('#mbti, #yearOfBirth, #startTime, #endTime').on('change', function () {
        if ($(this).val() == '') {
            $(this).css('color', 'rgba(211, 211, 211, 0.5)');
        } else {
            $(this).css('color', 'lightgrey');
        }
    });
    $('#mbti, #yearOfBirth, #startTime, #endTime').trigger('change');

    // 게임 정보 게임 선택
    $(document).on('click', '.profile-info-game-box .profile-info-game-btn', function () {
        // 변경된 경우에만 적용
        if ($(this).hasClass('select'))
            return;

        // $('.profile-info-game-box .profile-info-game-btn').removeClass('select');
        // $(this).addClass('select');
        changeGameAttr($(this).attr('id'));
    });

    // 팝업창 닫기
    $("#gameInfo-popup").click(function (event) {
        if ($(event.target).is("#gameInfo-popup")) {
            closeGameInfoPopup();
        }
    });

    //  팝업 게임 선택
    $(document).on('click', '.gameInfo-popup-gamebox .profile-info-game-btn', function () {
        $('.gameInfo-popup-gamebox .profile-info-game-btn').removeClass('select');
        $(this).addClass('select');
    });

    // 파일 업로드 클릭 이벤트
    $('#profileImgUploadBtn').on('click', function () {
        $('#profileImgFile').click();
    });

    // 파일이 선택되면 Base64로 변환하여 미리보기
    $('#profileImgFile').on('change', function (event) {
        var file = event.target.files[0];
        if (file) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var base64Image = e.target.result;

                // 미리보기 이미지 src에 Base64 데이터 삽입
                $('#profileImg').attr('src', base64Image);
                $('#profileImg').show();

            }
            reader.readAsDataURL(file);
        }
    });

    // 게임 속성 변경 시 데이터 저장
    $(document).on('change', '.gameInfo-select', function () {
        saveGameAttr();
    });

    // 성격 유형 option 추가
    $.ajax({
        url: '/user/mbti',
        type: 'get',
        success: function (res) {
            res.forEach(function (mbti) {
                $('#mbti').append(`<option value="${mbti}">${mbti}</option>`);
            });
        }
    });

});

function changeStep(stepVal) {

    // 사용자 정보 유효성검사
    if (stepVal === 2) {
        const fields = [
            {
                id: 'email',
                name: '이메일',
                pattern: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
                message: '유효한 이메일 주소 형식이 아닙니다. 예시: test@test.com'
            },
            {
                id: 'nickname',
                name: '닉네임',
                pattern: /^.{1,20}$/, // 닉네임의 길이를 1~20자로 제한
                message: '닉네임은 1자 이상 20자 이하로 입력해야 합니다.'
            },
            {
                id: 'yearOfBirth',
                name: '생년월일',
                pattern: null, // 1900~2099 사이의 년도만 허용
                message: null
            }
        ];

        if (!validation(fields)) {
            return;
        }
    }

    $('.profile-info-box').css('display', 'none');  // info-box 제거
    $('#profile-info-box-step' + stepVal).css('display', 'flex');

    // step 버튼
    $('.profile-step-btn').removeClass('active');   // active 제거
    $('#profile-step-btn-' + stepVal).addClass('active');

    if (stepVal === 2 && $('.profile-info-game-box .profile-info-game-btn').length === 0) {
        openGameInfoPopup();
    }
}

function openGameInfoPopup() {
    // 기존 박스 정보 조회
    var gameArr = [];
    $('#profile-info-game-box .profile-info-game-btn').each(function () {
        gameArr.push($(this).attr('id'));
    });

    $('#gameInfo-popup-gamebox').empty();
    // 게임 정보 조회
    $.ajax({
        url: '/gameInfo',
        type: 'get',
        dataType: 'json',
        async: false,
        success: function (res) {
            gameInfoCount = res.length;
            var addTag = ``;
            res.forEach(function (item, index) {
                // 이미 내 게임 목록에 있으면 추가x
                if (gameArr.includes(item.gameId)) {
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
        error: function (err) {
            alert('게임 정보 조회 중 문제가 발생하였습니다. 잠시 후 다시 시도하세요.');
            closeGameInfoPopup();
        }
    });
}

function closeGameInfoPopup() {
    $("#gameInfo-popup").hide();
}

function addPopupGameInfo() {
    var selectGameInfo = $('.gameInfo-popup-gamebox .profile-info-game-btn.select');
    if (selectGameInfo.length === 0) {
        alert('선호하는 게임을 선택하세요.');
        return;
    }

    var gameCount = $('#profile-info-game-box .profile-info-game-btn').length;
    var gameId = selectGameInfo.attr('id').split('-')[1];
    var addTag = `
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


}

// 게임 제거
function deleteGameInfo(gameId) {

    var delGameIndex = gameInfoList.findIndex(game => game.gameId === gameId);  // // 저장 데이터 삭제
    if (delGameIndex >= 0) {
        gameInfoList.splice(delGameIndex, 1);
    }

    var delGameId = $('.profile-info-game-box .profile-info-game-btn.select').attr('id');
    $('#profile-info-game-box').find(`#${gameId}`).remove();    // 선호 게임 버튼 삭제
    openGameInfoPopupCheck();   // 게임 추가 버튼

    if (gameId === delGameId) {
        $('.profile-info-gameAttr-box').empty();    // 게임 속성 초기화
        $('.profile-info-game-box .profile-info-game-btn').first().trigger('click');    // 첫번째 게임에 select 옵션 추가
    }

    // 게임 모두 삭제했을 경우 추가 팝업 띄우기
    if ($('.profile-info-game-box .profile-info-game-btn').length === 0) {
        openGameInfoPopup()
    }
}

// 게임 추가 버튼 출력 여부
function openGameInfoPopupCheck() {
    if ($('.profile-info-game-box .profile-info-game-btn').length === gameInfoCount) {
        $('#game-add-btn').hide();
    } else {
        $('#game-add-btn').show();
    }
}

// 게임 속성 저장
function saveGameAttr(){
    var selectGameId = $('.profile-info-game-box .profile-info-game-btn.select').attr('id');
    var selectGameNm = $('#gameNm').text();
    var currentGameInfo = {
        gameId: selectGameId,
        gameNm: selectGameNm,
        info: {}
    };

    // 각 select 필드의 값을 저장
    $('.profile-info-gameAttr-box-col select').each(function () {
        var id = $(this).attr('id'); // select 요소의 id 값
        var value = $(this).val() || '';   // 사용자가 선택한 값
        currentGameInfo.info[id] = value; // 게임 정보에 저장
    });

    // 이미 저장된 게임이 있는지 확인 (배열에서 찾아서 업데이트)
    var existingGameIndex = gameInfoList.findIndex(game => game.gameId === selectGameId);
    if (existingGameIndex !== -1) { // 기존에 저장된 게임 정보 업데이트
        gameInfoList[existingGameIndex] = currentGameInfo;
    } else {    // 새로운 게임 정보 추가
        gameInfoList.push(currentGameInfo);
    }

}

// 게임 속성 창 변경
function changeGameAttr(gameId) {

    // 기존 입력값 저장
    if ($('.profile-info-gameAttr-box').children().length > 0) {
        saveGameAttr();
    }

    $('.profile-info-gameAttr-box').empty();    // 초기화


    // 선택한 게임 select 클래스 적용
    $('.profile-info-game-box .profile-info-game-btn').removeClass('select');
    $(`#${gameId}`).addClass('select');


    var savedGameInfo = gameInfoList.find(game => game.gameId === gameId);

    // select optiop 체크 여부
    function gameAttrSelectCheck(key, optionKey) {
        // 기존에 저장된 게임 정보를 가져오는 로직
        if (!savedGameInfo)
            return '';

        var savedInfo = savedGameInfo.info;
        return savedInfo[key] === optionKey ? 'selected' : '';
    }

    $.ajax({
        url: '/gameInfo/' + gameId,
        type: 'get',
        dataType: 'json',
        async: false,
        success: function (res) {

            // 게임명
            var addTag = `
                <div class="profile-info-gameAttr-box-title">
                    | <span id="gameNm">${res.gameNm}</span>
                </div>
            `;
            $('.profile-info-gameAttr-box').prepend(addTag);    // 맨 앞에 추가

            var infoData = Object.entries(res.info);    // info 객체에서 항목을 배열로 변환
            var $gameAttrBox = $('.profile-info-gameAttr-box');
            var $colDiv = $('<div class="profile-info-gameAttr-box-col"></div>');
            var count = 0;

            infoData.forEach(function ([key, value]) {
                var $wrapperDiv = $('<div class="input-wrapper-ph"></div>');
                var $select = $('<select id="' + key + '" class="gameInfo-select" required><option value="" ></option></select>');
                var $label = $('<label>' + value.infoNm + '</label>');

                // 속성 추가
                Object.entries(value).forEach(function ([optionKey, optionValue]) {
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
        }, complete(){
            saveGameAttr(); // 새로운 게임 option 생성 후 디폴트 값 저장
        }
    });
}


function userInfoSave(){

    if(gameInfoList.length === 0){
        alert('선호하는 게임을 선택하세요.');
        return;
    }

    var startTime = $('#startTime').val() ? $('#startTime').val().padStart(2, '0') + ":00" : null;
    var endTime = $('#endTime').val() ? $('#endTime').val().padStart(2, '0') + ":00" : null;

    // 사용자 정보
    var userInfoData = {
        nickname: $('#nickname').val(),
        yearOfBirth: parseInt($('#yearOfBirth').val(), 10),

        mbti: $('#mbti').val() === '' ? null : $('#mbti').val(),
        startTime: startTime,
        endTime: endTime,
        profileImg : $('#profileImg').attr('src'),
        gameInfoList : gameInfoList
    };


    $.ajax({
        url: '/user/',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(userInfoData),
        dataType: 'json',
        async: false,
        success: function (res) {
            window.location.href = '/board';
        },
        error: function (xhr) {
            const errorMessage = xhr.responseJSON?.message || '서버 오류가 발생했습니다. 다시 시도해 주세요.';
            alert(errorMessage);
        }
    });


}