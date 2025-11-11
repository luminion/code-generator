/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.luminion.generator.config.po;

import io.github.luminion.generator.config.enums.SqlLike;

/**
 * 表名拼接
 *
 * @author nieqiuqiu
 * @author luminion
 * @since 1.0.0
 */
public class LikeTable {

    private final String value;

    private SqlLike like = SqlLike.DEFAULT;

    public LikeTable(String value) {
        this.value = value;
    }

    public LikeTable(String value, SqlLike like) {
        this.value = value;
        this.like = like;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public String getValue() {
        return SqlLike.concatLike(this.value, like);
    }

}
