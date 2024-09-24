$(document).ready(function() {
    const joystick = $('#joystick');
    const container = $('.joystick-container');
    let isDragging = false;

    // 조이스틱 마우스 다운 이벤트
    joystick.on('mousedown', function(e) {
        isDragging = true;
    });

    // 마우스 업 이벤트 (조이스틱 드래그 종료)
    $(document).on('mouseup', function() {
        isDragging = false;
        joystick.css({ top: '50%', left: '50%' }); // 원래 위치로 되돌리기
    });

    // 마우스 이동 이벤트 (조이스틱 드래그 중)
    $(document).on('mousemove', function(e) {
        if (isDragging) {
            const rect = container[0].getBoundingClientRect();
            const offsetX = e.clientX - rect.left - rect.width / 2;
            const offsetY = e.clientY - rect.top - rect.height / 2;

            // 조이스틱의 움직임 제한 (원 내부로만 이동 가능)
            const distance = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
            const maxDistance = rect.width / 2 - joystick.width() / 2;

            if (distance > maxDistance) {
                const angle = Math.atan2(offsetY, offsetX);
                joystick.css({
                    left: `${maxDistance * Math.cos(angle) + rect.width / 2 - joystick.width() / 2}px`,
                    top: `${maxDistance * Math.sin(angle) + rect.height / 2 - joystick.height() / 2}px`
                });
            } else {
                joystick.css({
                    left: `${offsetX + rect.width / 2 - joystick.width() / 2}px`,
                    top: `${offsetY + rect.height / 2 - joystick.height() / 2}px`
                });
            }
        }
    });
});
