package com.example.fastcampusmysql.utill;

import java.util.List;

public record PageCursor<T>(
    CursorRequest nextCursorRequest,
    List<T> body
) {


}
