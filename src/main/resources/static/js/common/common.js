function validation(fields) {
    if (fields == null) {
        return false;
    } else {
        for (const field of fields) {
            const {id, name, pattern, message} = field;
            const value = $('#' + id).val();

            if (!value) {
                alert(name + '은(는) 필수 입력 항목입니다.');
                $('#' + id).focus();
                return false;
            }

            if (pattern != null && !pattern.test(value)) {
                alert(message);
                $('#' + id).focus();
                return false;
            }
        }
        return true;
    }
}