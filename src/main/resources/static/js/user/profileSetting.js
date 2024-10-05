let gameInfoCount = 0;

$(function() {

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
        $('.profile-info-game-box .profile-info-game-btn').removeClass('select');
        $(this).addClass('select');
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
        <button class="profile-info-game-btn ${gameCount === 0 ? 'select' : ''}" id="${gameId}">
            <img src="/images/gameImg/${gameId}.png" />
            <span class="profile-info-game-delete-btn" onclick="deleteGameInfo('${gameId}')">×</span>
        </button>
    `;
    $('#profile-info-game-box').append(addTag);


    openGameInfoPopupCheck();
    closeGameInfoPopup();


}

// 게임 제거
function deleteGameInfo(gameId){
    $('#profile-info-game-box').find(`#${gameId}`).remove();
    openGameInfoPopupCheck();
}

// 게임 선택 창 버튼 출력 여부
function openGameInfoPopupCheck(){
    if($('.profile-info-game-box .profile-info-game-btn').length === gameInfoCount ){
        $('#game-add-btn').hide();
    } else{
        $('#game-add-btn').show();
    }
}